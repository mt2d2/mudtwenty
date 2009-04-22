package server;

import java.io.BufferedReader;
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
import message.InputParser;
import message.ServerMessage;
import server.universe.InvalidLoginException;
import server.universe.Player;

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

		this.sendMessage(new ClientMessage(WELCOME_STRING, Server.SYSTEM_TEXT_COLOR));
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
			ServerMessage message = null;

			if (this.isTextMode())
			{
				try
				{
					String input = this.textIn.readLine();

					if (input == null)
					{
						this.terminateConnection();
						break;
					}
					else
					{
						message = InputParser.parse(input.trim());
					}
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				try
				{
					message = (ServerMessage) this.in.readObject();
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

			if (message == null)
			{
				this.terminateConnection();
				break;
			}
			else
			{
				this.sendMessage(this.server.getServerResponse(message.getCommand()).respond(this, message.getArguments()));
			}
		}
	}

	/**
	 * Terminates the connection. This terminates the appropriate Input- and
	 * OutputStreams and the Socket itself.
	 */
	public void terminateConnection()
	{
		try
		{
			if (this.isTextMode())
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
			this.savePlayerToDisk(this.player);
			this.server.getUniverse().removePlayer(this.getPlayer());
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
		if (this.isTextMode())
		{
			try
			{
				this.sendMessage(message.getPayload());
			}
			catch (NullPointerException e)
			{
				// user no longer active, terminate connection
				this.terminateConnection();
			}
		}
		else
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
	}

	/**
	 * Convenience method to send a string to the client when running in text
	 * mode. This method should not be used; it is instead preferred that
	 * ClientMessage be sent.
	 * 
	 * @see #sendMessage(ClientMessage)
	 * @param message
	 *            string will be sent to the client running in text mode
	 */
	private void sendMessage(String message)
	{
		this.textOut.println(message);
	}

	/**
	 * @return <code>true</code> if the client is running using text mode, or
	 *         <code>false</code>
	 */
	private boolean isTextMode()
	{
		return this.textMode;
	}

	/**
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

				if (player.getUsername().equals(username) && player.confirmPasswordHash(password))
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
	 * Registers a user in the system. This first checks to see if the username
	 * is already taken, then writes a new Player to disk with the new username
	 * and password.
	 * 
	 * @param username
	 *            user input of the username
	 * @param password
	 *            password user input of the password, MD5-hashed
	 * @return <code>true</code> if the registration were successful or
	 *         <code>false</code>, i.e., the username is already in use.
	 */
	public boolean register(String username, String password)
	{
		final String dataRoot = conf.getProperty("data.root");
		final File sessionPath = new File(dataRoot + File.separatorChar + "sessions" + File.separatorChar + username + ".dat");

		if (!sessionPath.exists())
		{			
			if (this.savePlayerToDisk(new Player(username, password)))
				return true;
		}

		return false;
	}

	/**
	 * Saves a given Player to disk. This is useful during registration, when
	 * the player is exiting, but also because the server will periodically save
	 * all players to disk.
	 * 
	 * @return <code>true</code> if the save were successful or
	 *         <code>false</code>
	 */
	private boolean savePlayerToDisk(Player player)
	{	
		final File sessionPath = new File(conf.getProperty("data.root") + File.separatorChar + "sessions" + File.separatorChar + player.getUsername() + ".dat");

		logger.info("saving " + player.getUsername() + " to disk");
		logger.fine("saving to file: " + sessionPath.getAbsolutePath());
		
		if (!sessionPath.canWrite())
		{
			try
			{
				sessionPath.createNewFile();
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(sessionPath));
				os.writeObject(player);
				os.close();

				// there was success in writing
				return true;
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
		}

		return false;
	}
}
