package server;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import message.ClientMessage;
import message.Command;
import server.response.CommandsResponse;
import server.response.DropResponse;
import server.response.EchoResponse;
import server.response.GetResponse;
import server.response.GiveResponse;
import server.response.InventoryResponse;
import server.response.LoginResponse;
import server.response.LookResponse;
import server.response.MoveResponse;
import server.response.OocResponse;
import server.response.QuitResponse;
import server.response.RegisterResponse;
import server.response.SayResponse;
import server.response.ScoreResponse;
import server.response.ServerResponse;
import server.response.ShutdownResponse;
import server.response.TellResponse;
import server.response.UnknownResponse;
import server.response.UseResponse;
import server.response.WhoResponse;
import server.universe.DefaultUniverse;
import server.universe.Player;
import server.universe.Room;
import server.universe.Universe;
import util.PropertyLoader;

/**
 * This is the Server of mudtwenty. It handles incoming socket connections on
 * PORT and delegates each client to a separate thread. Server maintains a list
 * of associated clients (which it periodically prunes) to allow messages to be
 * send to each connected client.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Server
{
	/**
	 * System messages are associated with a single color when sent to the
	 * client. This is a dark green.
	 */
	public static final Color				SYSTEM_TEXT_COLOR	= new Color(0, 51, 0);

	/**
	 * Error messages ought to be red, it's what people expect.
	 */
	public static final Color				ERROR_TEXT_COLOR	= Color.RED;

	/**
	 * Messages from other users should appear blue. It'll balance the world
	 * out.
	 */
	public static final Color				MESSAGE_TEXT_COLOR	= Color.BLUE;

	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger				logger				= Logger.getLogger("mudtwenty");

	/**
	 * Server configuration and properties, used for setting up the server and
	 * its universe.
	 */
	private static final Properties			conf				= PropertyLoader.loadProperties("server/configuration.properties");

	/**
	 * Port the server will run on this local host.
	 */
	private static final int				PORT				= Integer.parseInt(conf.getProperty("server.port"));

	private static List<ServerThread>		clients;
	private ServerSocket					serverSocket;
	private boolean							done;
	private Map<Command, ServerResponse>	actions;
	private Timer							timer;
	private static Universe					universe;

	/**
	 * Default constructor for Server. It attempts to establish a ServerSocket,
	 * and will throw an IOException in the case that this is impossible. It
	 * also makes sure the universe is up and loaded and starts anything else
	 * that the server needs to have started (e.g., ReaperTask)
	 * 
	 * After that, it enters a blocking loop waiting for connections.
	 * 
	 * @throws IOException
	 *             indicates problem starting server, most likely a different
	 *             service running on the same port
	 */
	public Server() throws IOException
	{
		// Display welcome message
		logger.info("booting server on localhost:" + PORT);

		// Initial setup
		Server.clients = new ArrayList<ServerThread>();
		this.serverSocket = new ServerSocket(PORT);
		this.done = false;
		this.timer = new Timer();

		// Prefer latency over bandwith, over connection time
		this.serverSocket.setPerformancePreferences(0, 2, 1);

		// Install responses
		this.installServerResponses();

		// Set up the universe
		logger.info("loading universe");
		this.loadUniverse();

		// Schedule background maintenance tasks
		timer.schedule(new ReaperTask(), 0, 1000);
		timer.schedule(new UniverseSaveTask(), 0, 3000);

		// Main loop accepts clients, spawns new threads to handle each
		this.acceptClients();

		// When the main loop finishes, close the server socket.
		this.serverSocket.close();
	}

	/**
	 * Initialize the universe. If the universe can be read from a file, do so.
	 * Otherwise, create a default universe as a fallback.
	 */
	private void loadUniverse()
	{
		String dataRoot = conf.getProperty("data.root");
		File universeFile = new File(dataRoot + File.separatorChar + "universe.dat");

		try
		{
			ObjectInputStream fileIn = new ObjectInputStream(new FileInputStream(universeFile));
			Server.universe = (Universe) fileIn.readObject();
			fileIn.close();
		}
		catch (FileNotFoundException e)
		{
			// This is expected on first ever startup
			logger.info("generating new universe");
			Server.universe = new DefaultUniverse();
		}
		catch (IOException e)
		{
			logger.throwing("Server", "loadUniverse", e);
			Server.universe = new DefaultUniverse();
		}
		catch (ClassNotFoundException e)
		{
			logger.throwing("Server", "loadUniverse", e);
			Server.universe = new DefaultUniverse();
		}
	}

	/**
	 * Try to save the universe to a file.
	 */
	private void saveUniverse()
	{
		String dataRoot = conf.getProperty("data.root");
		File universeFile = new File(dataRoot + File.separatorChar + "universe.dat");
		try
		{
			universeFile.createNewFile();
			ObjectOutputStream fileOut = new ObjectOutputStream(new FileOutputStream(universeFile));
			fileOut.writeObject(Server.universe);
			fileOut.close();
		}
		catch (FileNotFoundException e)
		{
			logger.throwing("Server", "saveUniverse", e);
		}
		catch (IOException e)
		{
			logger.throwing("Server", "saveUniverse", e);
		}
	}

	/**
	 * Installs a list of acceptable commands and their associated action for
	 * each ServerThread to run. This should be edited to register new
	 * functionality.
	 */
	private void installServerResponses()
	{
		this.actions = new HashMap<Command, ServerResponse>();
		this.actions.put(Command.ECHO, new EchoResponse());
		this.actions.put(Command.UNKNOWN, new UnknownResponse());
		this.actions.put(Command.QUIT, new QuitResponse());
		this.actions.put(Command.COMMANDS, new CommandsResponse());
		this.actions.put(Command.WHO, new WhoResponse());
		this.actions.put(Command.LOGIN, new LoginResponse());
		this.actions.put(Command.REGISTER, new RegisterResponse());
		this.actions.put(Command.TELL, new TellResponse());
		this.actions.put(Command.OOC, new OocResponse());
		this.actions.put(Command.SAY, new SayResponse());
		this.actions.put(Command.DROP, new DropResponse());
		this.actions.put(Command.USE, new UseResponse());
		this.actions.put(Command.LOOK, new LookResponse());
		this.actions.put(Command.INVENTORY, new InventoryResponse());
		this.actions.put(Command.SCORE, new ScoreResponse());
		this.actions.put(Command.MOVE, new MoveResponse());
		this.actions.put(Command.SHUTDOWN, new ShutdownResponse());
		this.actions.put(Command.GIVE, new GiveResponse());
		this.actions.put(Command.GET, new GetResponse());
		
		// aliases
		this.actions.put(Command.CD, new MoveResponse());
	}

	/**
	 * Returns a ServerResponse appropriate to a given Command. This is useful
	 * for quickly parsing incoming ClientMessages for its appropriate action.
	 * 
	 * @param input
	 *            selected command
	 * @return input's associated ServerResponse
	 */
	public ServerResponse getServerResponse(Command input)
	{
		return this.actions.get(input);
	}

	/**
	 * Sends a message to all clients in a given color.
	 * 
	 * @param message
	 *            message to be sent to clients
	 */
	public static synchronized void sendMessageToAllClients(ClientMessage message)
	{
		for (ServerThread st : Server.clients)
			st.sendMessage(message);
	}

	/**
	 * Sends a message to all players in a room.
	 * 
	 * This should also send to MOBs -- and it should be renamed.
	 * 
	 * @param room
	 *            room to target
	 * @param message
	 *            message to send
	 */
	public static synchronized void sendMessageToAllClientsInRoom(Room room, ClientMessage message)
	{
		final List<Player> playersInRoom = Server.universe.getPlayersInRoom(room);

		for (ServerThread st : Server.clients)
			if (playersInRoom.contains(st.getPlayer()))
				st.sendMessage(message);
	}

	/**
	 * Sends a message to a specific player. This player is identified by his
	 * username only, which might be kind of brittle.
	 * 
	 * @param username
	 *            Player that this message will be sent is represented by this
	 *            String, his username
	 * @param message
	 *            message that will be sent player ot player
	 */
	public static synchronized void sendMessageToPlayer(String username, ClientMessage message)
	{
		for (ServerThread st : Server.clients)
			if (st.isLoggedIn() && st.getPlayer().getName().equals(username))
				st.sendMessage(message);
	}

	/**
	 * Main blocking loop of the Server. Waits for new incoming connections and
	 * spawns a new ServerThread to handle that client.
	 */
	private void acceptClients()
	{
		Socket socket;
		ServerThread newClient;

		while (!this.done)
		{
			try
			{
				socket = serverSocket.accept();
				newClient = new ServerThread(this, socket);
				Server.clients.add(newClient);
				new Thread(newClient).start();

				logger.info("client connected to server: " + socket);
			}
			catch (IOException e)
			{
				logger.warning("error establishing socket connection.");
				logger.throwing("Server", "acceptClients", e);
			}
		}
	}

	/**
	 * Get the Universe object that the server has loaded.
	 * 
	 * @return universe associated with this server
	 */
	public static Universe getUniverse()
	{
		return Server.universe;
	}

	/**
	 * An extension of TimerTask that periodically prunes finished clients. That
	 * is, this removes clients whose State is DONE from the list of active
	 * clients.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class ReaperTask extends TimerTask
	{
		public void run()
		{
			List<ServerThread> toRemove = new ArrayList<ServerThread>();

			for (ServerThread st : Server.clients)
				if (st.getState() == State.DONE)
					toRemove.add(st);

			Server.clients.removeAll(toRemove);

			for (ServerThread st : toRemove)
				Server.sendMessageToAllClients(new ClientMessage("client terminated connection: " + st, Server.SYSTEM_TEXT_COLOR));

			toRemove.clear();
		}
	}

	/**
	 * A TimerTask that saves the universe.
	 */
	private class UniverseSaveTask extends TimerTask
	{
		public void run()
		{
			logger.fine("the universe and players are being saved");
			Server.this.saveUniverse();

			for (ServerThread st : Server.clients)
				if (st.isLoggedIn())
					st.savePlayerToDisk(st.getPlayer());
		}
	}

	/**
	 * Shuts the server down, saving the universe and all players, terminates
	 * each player's connection, and finally exits the program.
	 */
	public void shutdown()
	{
		logger.info("server shutdown command recieved");

		logger.info("saving universe");
		this.saveUniverse();

		logger.info("saving players");
		for (ServerThread st : Server.clients)
		{
			st.savePlayerToDisk(st.getPlayer());
			st.terminateConnection();
		}

		// final shutdown, success exit status
		logger.info("server shutting down");
		System.exit(0);
	}

	/**
	 * Main entrance to the Server. This creates a new Server object (which
	 * starts running the server). This also catches severe exceptions.
	 * 
	 * @param args
	 *            there are no arguments for Server (this parameter is ignored)
	 */
	public static void main(String[] args)
	{
		try
		{
			new Server();
		}
		catch (IOException e)
		{
			logger.severe("error establishing server on port " + PORT);
			logger.throwing("Server", "main", e);
		}
	}
}
