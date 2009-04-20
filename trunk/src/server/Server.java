package server;

import java.awt.Color;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import message.ClientMessage;
import message.Command;
import server.response.EchoResponse;
import server.response.ExitResponse;
import server.response.HelpResponse;
import server.response.ServerResponse;
import server.response.UnknownResponse;
import server.response.WhoResponse;

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
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger				logger				= Logger.getLogger("mudtwenty");

	/**
	 * Port the server will run on this local host. In the future, this could be
	 * read from a configuration file.
	 */
	private static final int				PORT				= 8080;

	private List<ServerThread>				clients;
	private ServerSocket					serverSocket;
	private boolean							done;
	private Map<Command, ServerResponse>	actions;
	private Timer							timer;

	/**
	 * Default constructor for Server. It attempts to establish a ServerSocket
	 * on PORT, and will throw an IOException in the case that this is
	 * impossible. After, it enters a blocking loop waiting for connections.
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
		this.clients = new ArrayList<ServerThread>();
		this.serverSocket = new ServerSocket(PORT);
		this.done = false;
		this.timer = new Timer();

		// prefer latency over bandwith, over connection time
		this.serverSocket.setPerformancePreferences(0, 2, 1);

		// install responses
		this.installServerResponses();

		// spawn reaper thread
		timer.schedule(new ReaperTask(), 0, 1000);

		// main loop accepts clients, spawns new threads to handle each
		this.acceptClients();

		// close the server socket when finished
		this.serverSocket.close();
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
		this.actions.put(Command.EXIT, new ExitResponse());
		this.actions.put(Command.HELP, new HelpResponse());
		this.actions.put(Command.WHO, new WhoResponse());
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
	 * Sends a message to all clients. This is a convince method for defaulting
	 * to the default color.
	 * 
	 * @param message
	 *            message to be sent to clients
	 */
	public void sendMessageToAllClients(String message)
	{
		this.sendMessageToAllClients(message, null);
	}

	/**
	 * Sends a message to all clients in a given color.
	 * 
	 * @param message
	 *            message to be sent to clients
	 * @param color
	 *            color clients will see the message in
	 */
	public void sendMessageToAllClients(String message, Color color)
	{
		for (ServerThread st : this.clients)
			st.sendMessage(new ClientMessage(message, color));
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
				this.clients.add(newClient);
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
	 * @return description of users on the MUD server, including the users and
	 *         guests logged in
	 */
	public String getUsersOnline()
	{
		StringBuilder message = new StringBuilder();
		int guests = 0;

		for (ServerThread st : this.clients)
		{
			if (st.isLoggedIn())
				message.append(st.getPlayer().getName() + ", ");
			else
				guests++;
		}

		// remove trailing comma
		if (message.length() > 0)
			message.substring(0, message.length() - 2);
		else
			message.append("no users");

		// add guests to that this
		if (guests > 0)
			message.append(" and " + guests + ((guests == 1) ? " guest" : " guests"));

		return message.toString();
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
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run()
		{
			List<ServerThread> toRemove = new ArrayList<ServerThread>();

			for (ServerThread st : Server.this.clients)
				if (st.getState() == State.DONE)
					toRemove.add(st);

			Server.this.clients.removeAll(toRemove);

			for (ServerThread st : toRemove)
				Server.this.sendMessageToAllClients("client terminated connection: " + st, Server.SYSTEM_TEXT_COLOR);

			toRemove.clear();
		}
	}

	/**
	 * Main entrance to the Server. This creates a new Server, catching severe
	 * exceptions.
	 * 
	 * @param args
	 *            there are no arguments for Server
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
