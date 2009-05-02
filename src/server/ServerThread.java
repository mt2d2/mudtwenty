package server;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import message.ClientMessage;
import message.ServerMessage;
import server.connection.Communicable;
import server.connection.MessageConnection;
import server.connection.TextConnection;
import server.response.LookResponse;
import server.response.ServerResponse;
import server.universe.Player;
import util.PropertyLoader;

/**
 * Handles each client in a non-blocking fashion, reading input and responding
 * appropriately. ServerThread has two modes of operation, the default uses
 * MessageProtocol, sending and receiving ClientMessage and ServerMessage. In
 * the event that an ObjectStream cannot be established, the system falls back
 * on basic text streams. This makes connecting to and using the server via
 * telnet possible.
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
	private static final String		WELCOME_STRING	= "Welcome to mudtwenty.";

	private Communicable			connection;
	private Server					server;
	private Socket					socket;
	private State					state;
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
		this.player = null;

		try
		{
			// by default, we should run using the gui
			this.connection = new MessageConnection();
			this.connection.openConnection(this.socket);
		}
		catch (IOException e)
		{
			logger.info("error establishing in and out streams on ServerThread: " + this);
			logger.throwing("ServerThread", "ServerThread", e);
			logger.info("client is probably not using MessageProtocol, defaulting to basic text streams");

			try
			{
				// and fall back on text mode
				this.connection = new TextConnection();
				this.connection.openConnection(this.socket);
			}
			catch (IOException ex)
			{
				// and complain if that doesn't work, loudly
				logger.severe("error establishing in and out streams on ServerThread: " + this + " in text mode");
				logger.throwing("ServerThread", "ServerThread", ex);
			}
		}

		// send a greeting to our good friend
		this.connection.sendMessage(new ClientMessage(WELCOME_STRING, Server.SYSTEM_TEXT_COLOR));
	}

	/**
	 * The run method of the thread. In this method, the ServerThread should get
	 * the name of the user and login or register a player, and then start the
	 * main loop, where it waits for input and responds.
	 */
	public void run()
	{
		this.processLogin();
		while (this.getState() == State.OK)
		{
			ServerMessage toServer = this.connection.getMessage();

			if (toServer == null)
			{
				this.terminateConnection();
				break;
			}
			else
			{
				ServerResponse response = ResponseFactory.getResponse(toServer.getPayload());
				ClientMessage toClient = response.respond(this, getArguments(toServer.getPayload()));
				this.connection.sendMessage(toClient);
			}
		}
	}

	/**
	 * Get the list of arguments.
	 */
	private List<String> getArguments(String input)
	{
		List<String> words = Arrays.asList(input.split(" "));
		return words.subList(1, words.size());
	}
	
	/**
	 * Get the user's name and password. If they're new, register and create a new player.
	 * Otherwise log them in. If something goes wrong during this process, start again.
	 */
	private void processLogin()
	{
		this.connection.sendMessage(new ClientMessage("What is your name?"));
		String name = this.connection.getMessage().getPayload();
		
		if (Server.getUniverse().isRegistered(name))
		{
			this.connection.sendMessage(new ClientMessage("Welcome back. Please enter your password."));
			String password = this.connection.getMessage().getPayload();
			try
			{
				if (this.login(name, password))
				{
					this.connection.sendMessage(new ClientMessage("Login was successful", Server.SYSTEM_TEXT_COLOR));
					this.connection.sendMessage(new LookResponse().respond(this, new ArrayList<String>()));
				}
				else
				{
					this.connection.sendMessage(new ClientMessage("Login was unsuccessful.", Color.RED));
					this.processLogin();
				}
			}
			catch (InvalidLoginException e)
			{
				this.connection.sendMessage(new ClientMessage(e.getMessage(), Color.RED));
			}
		}
		else
		{
			this.connection.sendMessage(new ClientMessage("Welcome, new player. Please enter a password."));
			String password = this.connection.getMessage().getPayload();
			if (this.register(name, password))
			{
				try {
					this.login(name, password);
				} catch (InvalidLoginException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.connection.sendMessage(new ClientMessage("Registration and login complete.", Server.SYSTEM_TEXT_COLOR));
				this.connection.sendMessage(new LookResponse().respond(this, new ArrayList<String>()));
			}
			else
			{
				this.connection.sendMessage(new ClientMessage("Registration unsuccessful.", Color.RED));
				this.processLogin();
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
			this.connection.terminate();
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
				// remove the player from the universe
				Server.getUniverse().logout(this.getPlayer());
			}
		}
	}

	/**
	 * Get the state; if a client is still running, then <code>State.OK</code>,
	 * or if the user has quit, <code>State.DONE</code>
	 *
	 * @return the State of a client.
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
	 * Test whether the user is logged on.
	 *
	 * @return <code>true</code> if the user is logged in, or <code>false</code>
	 */
	public boolean isLoggedIn()
	{
		return this.player != null;
	}

	/**
	 * Logs a user into the system. This entails a lengthy process: first, the
	 * session data is checked to see if such a user exists, it is loaded from
	 * the filesystem, and finally user and password are compared. If all checks
	 * out, the resultant Player object is associated with this ServerThread,
	 * and the Player is added to the Universe.
	 *
	 * @param name
	 *            user input for name
	 * @param password
	 *            user input for password
	 * @return <code>true</code> if login were successful or <code>false</code>
	 * @throws InvalidLoginException
	 *             thrown to provide helpful error messages to the user, i.e.,
	 *             user doesn't exist or invalid password
	 */
	private boolean login(String name, String password) throws InvalidLoginException
	{
		Player player = loadPlayer(name);
		if (player == null || !player.getName().equals(name))
		{
			throw new InvalidLoginException("Player does not exist.");
		}

		if (!player.confirmPasswordHash(password))
		{
			throw new InvalidLoginException("Incorrect password");
		}
		// add this player to the universe
		Server.getUniverse().login(player);

		// associate this player with this thread
		this.player = player;
		return true;
	}

	/**
	 * Load a player from their player file if possible. If something goes wrong, return null.
	 */
	private Player loadPlayer(String name)
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
				return player;
			}
			catch (FileNotFoundException e)
			{
				// There is no player file?
				// TODO notify user and unregister the name with universe
				logger.throwing("ServerThread", "loadPlayer", e);
				return null;
			}
			catch (IOException e)
			{
				logger.throwing("ServerThread", "loadPlayer", e);
				return null;
			}
			catch (ClassNotFoundException e)
			{
				logger.throwing("ServerThread", "loadPlayer", e);
				return null;
			}
		}
		return null;
	}

	/**
	 * Registers a new Player in the system. This first checks to see if the
	 * name is already taken before it does anything. Then, it registers the
	 * player with the universe, which records the player's location as the
	 * starting location. This does not log the user in.
	 *
	 * @param username
	 *            user input of the username
	 * @param password
	 *            password user input of the password
	 * @return <code>true</code> if the registration were successful or
	 *         <code>false</code>, i.e., the username is already in use.
	 */
	private boolean register(String name, String passwordHash)
	{
		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + name + ".dat");

		// TODO check whether the player is registered with the universe

		if (!playerFile.exists())
		{
			boolean saveSuccess = this.savePlayerToDisk(new Player(name, passwordHash));
			if (saveSuccess)
			{
				Server.getUniverse().register(name);
				return true;
			}
		}

		return false;
	}

	/**
	 * @return Server parent of this thread
	 */
	public Server getServer()
	{
		return this.server;
	}

	/**
	 * Saves a given Player to disk. This is useful during registration,
	 * possibly when the player is exiting, but also because the server will
	 * periodically save all players to disk.
	 *
	 * @return <code>true</code> if the save were successful or
	 *         <code>false</code>
	 */
	public boolean savePlayerToDisk(Player player)
	{
		final File playerFile = new File(conf.getProperty("data.root") + File.separatorChar + "players" + File.separatorChar + player.getName() + ".dat");

		logger.fine("saving " + player.getName() + " to disk at " + playerFile.getAbsolutePath());

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
			// Wait -- this will not be thrown if the file's not found?
			// TODO fix this.
		}
		catch (IOException e)
		{
			logger.throwing("ServerThread", "savePlayerToDisk", e);
		}

		return false;
	}

	/**
	 * Convenience method for sending messages from outside this class.
	 *
	 * @param message
	 *            the message to be sent
	 */
	public void sendMessage(ClientMessage message)
	{
		this.connection.sendMessage(message);
	}
}
