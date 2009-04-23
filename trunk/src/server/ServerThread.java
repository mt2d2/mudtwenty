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
import server.InvalidLoginException;
import server.universe.Player;
import server.universe.Universe;
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
	private static final String		WELCOME_STRING	= "welcome to mudtwenty";

	private Server					server;
	private Socket					socket;
	private State					state;

	// MessageProtocol mode
	private ObjectInputStream		in;
	private ObjectOutputStream		out;

	// text mode
	private boolean					textMode;
	private BufferedReader			textIn;
	private PrintWriter				textOut;

	// user associated with this thread
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
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
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

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Runnable#run()
	 */
	public void run()
	{
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
			message = (ServerMessage) this.in.readObject();
		}
		catch (EOFException e)
		{
			// pass
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				this.in.close();
				this.out.close();
			}

			this.socket.close();
		}
		catch (IOException e)
		{
			logger.throwing("ServerThread", "terminateConnection", e);
		}

		logger.info("client terminated connection: " + this);

		this.setState(State.DONE);

		if (this.isLoggedIn())
		{
			// remove the player from the unvierse

			//this.savePlayerToDisk(this.player);

			Universe.getInstance().removePlayer(this.getPlayer());
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
			this.out.writeObject(message);
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
	 * @param username
	 *            user input for username
	 * @param password
	 *            user input for password, MD5-hased
	 * @return <code>true</code> if login were successful or <code>false</code>
	 * @throws InvalidLoginException
	 *             thrown to provide helpful error messages to the user, i.e.,
	 *             user doesn't exist or invalid password
	 */
	public boolean login(String username, String password) throws InvalidLoginException
	{
		final String dataRoot = conf.getProperty("data.root");
		final File sessionPath = new File(dataRoot + File.separatorChar + "sessions" + File.separatorChar + username + ".dat");

		if (sessionPath.canRead())
		{
			try
			{
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(sessionPath));
				Player player = (Player) is.readObject();

				if (player.getName().equals(username) && player.confirmPasswordHash(password))
				{
					// add this player to the universe
					this.server.getUniverse().addPlayer(player);

					// associate this player with this thread
					this.player = player;

					return true;
				}
				else
				{
					throw new InvalidLoginException("the username or password was invalid");
				}
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			throw new InvalidLoginException("invalid user, " + username + ", please use the register command instead");
		}

		return false;
	}

	/**
	 * Registers a new Player in the system. This first checks to see if the name
	 * is already taken before it does anything. Then, it adds the new Player
	 * to the Universe. When the Universe is saved, so is the new Player.
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
		if (this.playerNameAvailable(name))
		{
			Player player = new Player(name, passwordHash);
			Universe.getInstance().addNewPlayer(player);
			return true;
		}
		return false;

// 		final String dataRoot = conf.getProperty("data.root");
// 		final File sessionPath = new File(dataRoot + File.separatorChar + "sessions" + File.separatorChar + username + ".dat");
//
// 		if (!sessionPath.exists())
// 		{
// 			if (this.savePlayerToDisk(new Player(username, password)))
// 				return true;
// 		}
//
// 		return false;
	}

	/**
	 * Test whether the given name is taken by another Player in the world.
	 */
	private boolean playerNameAvailable(String name)
	{
		for (Player player : Universe.getInstance().getAllPlayers())
		{
			if (player.getName().equals(name))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * Saves a given Player to disk. This is useful during registration, when
	 * the player is exiting, but also because the server will periodically save
	 * all players to disk.
	 *
	 * @return <code>true</code> if the save were successful or
	 *         <code>false</code>
	 */
	 // If Universe keeps track of all players and their locations, then players
	 // shouldn't be saved individually -- but the universe should be saved every
	 // so often and when it shuts down.
// 	private boolean savePlayerToDisk(Player player)
// 	{
// 		final File sessionPath = new File(conf.getProperty("data.root") + File.separatorChar
// 			+ "sessions" + File.separatorChar + player.getName() + ".dat");
//
// 		logger.info("saving " + player.getName() + " to disk");
// 		logger.fine("saving to file: " + sessionPath.getAbsolutePath());
//
// 		if (!sessionPath.canWrite())
// 		{
// 			try
// 			{
// 				sessionPath.createNewFile();
// 				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sessionPath));
// 				os.writeObject(player);
// 				os.close();
//
// 				// there was success in writing
// 				return true;
// 			}
//
// 			catch (FileNotFoundException e)
// 			{
// 				// TODO Auto-generated catch block
// 				e.printStackTrace();
// 			}
// 			catch (IOException e)
// 			{
// 				// TODO Auto-generated catch block
// 				e.printStackTrace();
// 			}
// 		}
//
// 		return false;
// 	}
}
