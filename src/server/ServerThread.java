package server;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Properties;
import java.util.logging.Logger;

import message.ClientMessage;
import message.ServerMessage;
import server.response.ServerResponse;
import server.universe.Player;
import util.InputParser;
import util.PropertyLoader;

/**
 * Handles each client in a non-blocking fashion, reading input and responding
 * appropriately. ServerThread has two modes of operation, the default uses
 * MessageProtocol, sending and receiving ClientMessage and ServerMessage. In
 * the event that an ObjectStream cannot be established, the system falls back
 * on basic text streams. This makes connecting to and using the server via
 * telnet possible.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ServerThread implements Runnable
{
	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger		logger			= Logger.getLogger("mudtwenty");

	/**
	 * Server configuration and properties, used for setting up the server and
	 * its universe.
	 */
	private static final Properties	conf			= PropertyLoader.loadProperties("server/configuration.properties");

	/**
	 * This is the default login message users see upon connecting to the
	 * server.
	 */
	private static final String		WELCOME_STRING	= "welcome to mudtwenty\nuse register or login to join the game";

	private Server					server;
	private Socket					socket;
	private State					state;

	// Whether the ServerThread is operating in text mode.
	private boolean					textMode;

	// MessageProtocol mode
	private ObjectInputStream		objectIn;
	private ObjectOutputStream		objectOut;

	// Text mode
	private BufferedReader			textIn;
	private PrintWriter				textOut;

	// Player object associated with this thread
	private Player					player;

	/**
	 * Sole constructor for this class. Takes server for reference and a
	 * connecting client on socket. After the welcome message is sent to the
	 * user. At this point, the mode of communication is established (either via
	 * MessageProtocol or text streams) and the thread enters its run loop.
	 *
	 * @param server
	 *            Server parent of this thread, useful for getting a list of
	 *            other, connected clients
	 * @param socket
	 *            Socket on which the client is connecting through
	 */
	public ServerThread(Server server, Socket socket)
	{
		this.server = server;
		this.socket = socket;
		this.state = State.OK;
		this.textMode = false;
		this.player = null;

		try
		{
			this.objectIn = new ObjectInputStream(this.socket.getInputStream());
			this.objectOut = new ObjectOutputStream(this.socket.getOutputStream());
		}
		catch (IOException e)
		{
			logger.info("error establishing in and out streams on ServerThread: " + this);
			logger.throwing("ServerThread", "ServerThread", e);
			logger.info("client is probably not using MessageProtocol, defaulting to basic text streams");
			this.textMode = true;

			try
			{
				this.textIn = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				this.textOut = new PrintWriter(this.socket.getOutputStream(), true);
			}
			catch (IOException ex)
			{
				logger.severe("error establishing in and out streams on ServerThread: " + this + " in text mode");
				logger.throwing("ServerThread", "ServerThread", ex);
			}
		}
		ClientMessage welcomeMessage = new ClientMessage(WELCOME_STRING, Server.SYSTEM_TEXT_COLOR);
		this.sendMessage(welcomeMessage);
	}

	/**
	 * The run method of the thread. In this method, the ServerThread should get
	 * the name of the user and login or register a player, and then start the
	 * main loop, where it waits for input and responds.
	 */
	public void run()
	{
		// Get the user's name.
		// TODO
		// Log in or register the player.
		// TODO
		// Enter the main input-getting loop of the server
		while (this.getState() == State.OK)
		{
			ServerMessage toServer;

			if (this.textMode)
				toServer = getMessageTextMode();
			else
				toServer = getMessageProtocolMode();

			if (toServer == null)
			{
				this.terminateConnection();
				break;
			}
			else
			{
				ServerResponse response = this.server.getServerResponse(toServer.getCommand());
				ClientMessage toClient = response.respond(this, toServer.getArguments());
				this.sendMessage(toClient);
			}
		}
	}

	/**
	 * Get and return a message to the server in text mode. Return null if
	 * something went wrong.
	 */
	private ServerMessage getMessageTextMode()
	{
		ServerMessage message = null;

		try
		{
			String input = this.textIn.readLine();

			if (input != null)
				message = InputParser.parse(input.trim());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return message;
	}

	/**
	 * Get and return a message to the server in MessageProtocol mode. Return
	 * null if something went wrong.
	 */
	private ServerMessage getMessageProtocolMode()
	{
		ServerMessage message = null;

		try
		{
			message = (ServerMessage) this.objectIn.readObject();
		}
		catch (EOFException e)
		{
			logger.throwing("ServerThread", "getMessageProtocolMode", e);
		}
		catch (IOException e)
		{
			logger.throwing("ServerThread", "getMessageProtocolMode", e);
		}
		catch (ClassNotFoundException e)
		{
			logger.throwing("ServerThread", "getMessageProtocolMode", e);
		}

		return message;
	}

	/**
	 * Terminates the connection. This terminates the appropriate Input- and
	 * OutputStreams and the Socket itself.
	 */
	public void terminateConnection()
	{
		try
		{
			if (this.textMode)
			{
				this.textIn.close();
				this.textOut.close();
			}
			else
			{
				this.objectIn.close();
				this.objectOut.close();
			}

			this.socket.close();
		}
		catch (IOException e)
		{
			logger.throwing("ServerThread", "terminateConnection", e);
		}
		finally
		{
			logger.info("client terminated connection: " + this);

			this.setState(State.DONE);

			if (this.isLoggedIn())
			{
				// remove the player from the unvierse
				this.server.getUniverse().logout(this.getPlayer());
		}
		}
	}

	/**
	 * @return the State of a client. e.g., if a client is still running, then
	 *         State.OK, or if the user has quit, State.DONE/
	 */
	public State getState()
	{
		return this.state;
	}

	/**
	 * Sets the state of the thread.
	 *
	 * @param state
	 *            new state for this thread
	 */
	public void setState(State state)
	{
		this.state = state;
	}

	/**
	 * Sends a message to the client. This is the default (and only accessible)
	 * method of communicating with the client. That means that it will
	 * automatically choose the correct OutputStream for the current mode of
	 * operation, either via MessageProtocol or text.
	 *
	 * @param message
	 *            this message will be sent to the client, but when running in
	 *            text mode, only the ClientMessage's payload will be sent.
	 */
	public void sendMessage(ClientMessage message)
	{
		if (this.textMode)
		{
			sendMessageTextMode(message);
		}
		else
		{
			sendMessageProtocolMode(message);
		}
	}

	/**
	 * Sends a message to the client in text mode.
	 */
	private void sendMessageTextMode(ClientMessage message)
	{
		try
		{
			this.textOut.println(message.getPayload());
		}
		catch (NullPointerException e)
		{
			// user no longer active, terminate connection
			this.terminateConnection();
		}
	}

	/**
	 * Sends a message to the client in MessageProtocol mode.
	 */
	private void sendMessageProtocolMode(ClientMessage message)
	{
		try
		{
			this.objectOut.writeObject(message);
		}
		catch (IOException e)
		{
			// user no longer active, terminate connection
			this.terminateConnection();
		}
	}

	/**
	 * Return the player associated with this thread, if possible. If no player
	 * has logged in, this returns null.
	 *
	 * @return Player associated with this ServerThread
	 */
	public Player getPlayer()
	{
		return this.player;
	}

	/**
	 * @return <code>true</code> if the user is logged in, or <code>false</code>
	 */
	public boolean isLoggedIn()
	{
		return this.player != null;
	}

	/**
	 * @return Server parent of this sub-thread
	 */
	public Server getServer()
	{
		return this.server;
	}

	/**
	 * Logs a user into the system. This entails a lengthy process: first, the
	 * session data is checked to see if such a suer exists, it is loaded from
	 * the filesystem, and finally user and password are compared. If all checks
	 * out, the resultant Player object is associated with this ServerThread,
	 * and the Player is added to the Universe.
	 *
	 * Later, this should be made private and ServerThread should have sole
	 * responsibility for logging in and registering players.
	 *
	 * @param username
	 *            user input for username
	 * @param password
	 *            user input for password, MD5-hased
	 * @return <code>true</code> if login were successful or <code>false</code>
	 * @throws InvalidLoginException
	 *             thrown to provide helpful error messages to the user, i.e.,
	 *             user doesn't exist or invalid password
	 */
	public boolean login(String name, String password) throws InvalidLoginException
	{
		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + name + ".dat");

		if (playerFile.canRead())
		{
			try
			{
				ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(playerFile));
				Player player = (Player) fileIn.readObject();
				fileIn.close();

				if (player.getName().equals(name) && player.confirmPasswordHash(password))
				{
					// add this player to the universe
					this.server.getUniverse().login(player);

					// associate this player with this thread
					this.player = player;

					return true;
				}
				else
				{
					throw new InvalidLoginException("the provided password was invalid");
				}
			}
			catch (FileNotFoundException e)
			{
				// There is no player file?
				// TODO notify the user and unregister the user name with universe
				logger.throwing("ServerThread", "login", e);
			}
			catch (IOException e)
			{
				// Problem reading the player file?
				// TODO inform the user?
				throw new InvalidLoginException("there was a problem reading the player file");
			}
			catch (ClassNotFoundException e)
			{
				// There is no Player class?
				logger.throwing("ServerThread", "login", e);
			}
		}
		else
		{
			throw new InvalidLoginException("invalid user, " + name + ", please use the register command instead");
		}

		return false;
	}

	/**
	 * Registers a new Player in the system. This first checks to see if the
	 * name is already taken before it does anything. Then, it registers
	 * the player with the universe, which records the player's location
	 * as the starting location. This does not log the user in.
	 *
	 * Later, this should be made private and ServerThread should have sole
	 * responsibility for logging in and registering players.
	 *
	 * @param username
	 *            user input of the username
	 * @param password
	 *            password user input of the password, MD5-hashed
	 * @return <code>true</code> if the registration were successful or
	 *         <code>false</code>, i.e., the username is already in use.
	 */
	public boolean register(String name, String passwordHash)
	{
		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + name + ".dat");

		// TODO check whether the player is registered with the universe

		if (!playerFile.exists())
		{
			boolean saveSuccess = this.savePlayerToDisk(new Player(name, passwordHash));
			if (saveSuccess)
			{
				this.server.getUniverse().register(name);
				return true;
			}
		}

		return false;
	}

	/**
	 * Saves a given Player to disk. This is useful during registration, possibly
	 * when the player is exiting, but also because the server will periodically save
	 * all players to disk.
	 *
	 * @return <code>true</code> if the save were successful or
	 *         <code>false</code>
	 */
	private boolean savePlayerToDisk(Player player)
	{
		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + player.getName() + ".dat");

		logger.info("saving " + player.getName() + " to disk at " + playerFile.getAbsolutePath());

		try
		{
			playerFile.createNewFile();
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(playerFile));
			fileOut.writeObject(player);
			fileOut.close();
			
			// there was success in writing
			return true;
		}
		catch (FileNotFoundException e)
		{
			System.out.println("here in filenotfound");
			
			// The player file didn't previously exist?
			// This is not a problem, and user file should still be saved.
			// TODO fix this.
		}
		catch (IOException e)
		{			
			logger.throwing("ServerThread", "savePlayerToDisk", e);
		}

		return false;
	}
}
