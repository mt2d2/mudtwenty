package server;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
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
	private static final String		WELCOME_STRING	= "Welcome to mudtwenty!";

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
			this.connection.open(this.socket);
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
				this.connection.open(this.socket);
			}
			catch (IOException ex)
			{
				// and complain if that doesn't work, loudly
				logger.severe("error establishing in and out streams on ServerThread: " + this + " in text mode");
				logger.throwing("ServerThread", "ServerThread", ex);
			}
		}

		if (connection instanceof TextConnection)
		{
			String asciiArtWelcome = "Welcome to\n" + "                     _ _                      _           _\n"
					+ " _ __ ___  _   _  __| | |___      _____ _ __ | |_ _   _  / \\\n"
					+ "| '_ ` _ \\| | | |/ _` | __\\ \\ /\\ / / _ \\ '_ \\| __| | | |/  /\n"
					+ "| | | | | | |_| | (_| | |_ \\ V  V /  __/ | | | |_| |_| /\\_/ \n"
					+ "|_| |_| |_|\\__,_|\\__,_|\\__| \\_/\\_/ \\___|_| |_|\\__|\\__, \\/\n" + "                                                  |___/\n";
			this.connection.sendMessage(new ClientMessage(asciiArtWelcome, SystemColor.DEFAULT));
		}
		else
		{
			this.connection.sendMessage(new ClientMessage(WELCOME_STRING, SystemColor.DEFAULT));
		}
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
	 * Get the user's name and password. If they're new, register and create a
	 * new player. Otherwise log them in. If something goes wrong during this
	 * process, start again.
	 */
	private void processLogin()
	{
		this.connection.sendMessage(new ClientMessage("What is your name?"));
		ServerMessage message = this.connection.getMessage();

		if (message == null)
			this.terminateConnection();
		else if (playerExists(message.getPayload()))
			processOldPlayer(message.getPayload());
		else
			processNewPlayer(message.getPayload());
	}

	/**
	 * Greet, get the password of, and try to register and log in a new player.
	 */
	private void processNewPlayer(String name)
	{
		this.connection.sendMessage(new ClientMessage("Welcome, new player. Please enter a password."));
		String password = this.connection.getMessage().getPayload();
		if (this.register(name, password))
		{
			try
			{
				this.login(name, password);
			}
			catch (InvalidLoginException e)
			{
				this.connection.sendMessage(new ClientMessage(e.getMessage(), Color.RED));
				processLogin();
			}

			this.connection.sendMessage(new ClientMessage("Registration and login complete.", SystemColor.DEFAULT));
			this.connection.sendMessage(new LookResponse().respond(this, new ArrayList<String>()));
		}
		else
		{
			this.connection.sendMessage(new ClientMessage("Registration unsuccessful.", Color.RED));
			processLogin();
		}
	}

	/**
	 * Greet, get the password of, and try to log in an already existing player.
	 */
	private void processOldPlayer(String name)
	{
		this.connection.sendMessage(new ClientMessage("Welcome back. Please enter your password."));
		String password = this.connection.getMessage().getPayload();
		try
		{
			if (this.login(name, password))
			{
				this.connection.sendMessage(new ClientMessage("Login was successful.", SystemColor.DEFAULT));
				this.connection.sendMessage(new LookResponse().respond(this, new ArrayList<String>()));
			}
			else
			{
				this.connection.sendMessage(new ClientMessage("Login was unsuccessful.", Color.RED));
				processLogin();
			}
		}
		catch (InvalidLoginException e)
		{
			this.connection.sendMessage(new ClientMessage(e.getMessage(), Color.RED));
			processLogin();
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
			this.connection.close();
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

			if (Server.getUniverse().isLoggedIn(this.player))
				Server.getUniverse().logout(this.player);
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
	 * Test whether a player exists and rectify any inconsistency between
	 * universe and player file.
	 */
	private boolean playerExists(String name)
	{
		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + name + ".dat");
		if (!playerFile.exists())
		{
			if (Server.getUniverse().isRegistered(name))
				Server.getUniverse().unregister(name);
			return false;
		}
		if (!Server.getUniverse().isRegistered(name))
		{
			playerFile.delete();
			// Notify user?
			return false;
		}
		return true;
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
			throw new InvalidLoginException("Player does not exist.");

		if (!player.confirmPassword(password))
			throw new InvalidLoginException("Incorrect password");

		// add this player to the universe
		Server.getUniverse().login(player);

		// associate this player with this thread
		this.player = player;
		return true;
	}

	/**
	 * Load a player from their player file if possible. If something goes
	 * wrong, return null.
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
	 * This registers the player with the universe. It creates a player and
	 * registers the player with the universe, which records the player's
	 * location as the starting location. This does not log the user in.
	 * 
	 * Precondition: player does not exist yet.
	 * 
	 * @return <code>true</code> if the registration was successful, false
	 *         otherwise.
	 */
	private boolean register(String name, String password)
	{
		Player newPlayer = new Player(name, password);

		if (this.savePlayerToDisk(newPlayer))
		{
			Server.getUniverse().register(name);
			return true;
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
		if (player == null)
			return false;

		final String dataRoot = conf.getProperty("data.root");
		final File playerFile = new File(dataRoot + File.separatorChar + "players" + File.separatorChar + player.getName() + ".dat");
		logger.fine("saving " + player.getName() + " to disk at " + playerFile.getAbsolutePath());

		try
		{
			playerFile.createNewFile();
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(playerFile));
			fileOut.writeObject(player);
			fileOut.close();
			return true;
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
