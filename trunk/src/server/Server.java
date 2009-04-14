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
import server.response.ServerResponse;
import server.response.UnknownResponse;

public class Server {
	public static final Color SYSTEM_TEXT_COLOR = new Color(0, 51, 0);

	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger logger = Logger.getLogger("mudtwenty");

	/**
	 * Port the server will run on this local host. In the future, this could be
	 * read from a configuration file.
	 */
	private static final int PORT = 8080;

	private List<ServerThread> clients;
	private ServerSocket serverSocket;
	private boolean done;
	private Map<Command, ServerResponse> actions;
	private Timer timer;

	public Server() throws IOException {
		// Display welcome message
		logger.info("booting server on localhost:" + PORT);

		this.clients = new ArrayList<ServerThread>();
		this.serverSocket = new ServerSocket(PORT);
		this.installServerResponses();
		this.done = false;
		this.timer = new Timer();

		// reaper thread
		timer.schedule(new ReaperTask(), 0, 1000);

		// main loop accepts clients, spawns new threads to handle each
		this.acceptClients();

		// close the server socket when finished
		this.serverSocket.close();
	}

	private void installServerResponses() {
		this.actions = new HashMap<Command, ServerResponse>();
		this.actions.put(Command.ECHO, new EchoResponse());
		this.actions.put(Command.UNKNOWN, new UnknownResponse());
	}

	public void sendMessageToAllClients(String message) {
		this.sendMessageToAllClients(message, null);
	}

	public void sendMessageToAllClients(String message, Color color) {
		for (ServerThread st : this.clients)
			st.sendMessage(new ClientMessage(message, color));
	}

	public ServerResponse getServerResponse(Command input) {
		return this.actions.get(input);
	}

	private void acceptClients() {
		Socket socket;
		ServerThread newClient;

		while (!this.done) {
			try {
				socket = serverSocket.accept();
				newClient = new ServerThread(this, socket);
				this.clients.add(newClient);
				new Thread(newClient).start();

				logger.info("client connected to server: " + socket);
			} catch (IOException e) {
				logger.warning("error establishing socket connection.");
				logger.throwing("Server", "acceptClients", e);
			}
		}
	}

	private class ReaperTask extends TimerTask {
		@Override
		public void run() {
			List<ServerThread> toRemove = new ArrayList<ServerThread>();

			for (ServerThread st : Server.this.clients)
				if (st.getState() == State.DONE)
					toRemove.add(st);

			Server.this.clients.removeAll(toRemove);

			for (ServerThread st : toRemove)
				Server.this.sendMessageToAllClients(
						"client terminated connection: " + st,
						Server.SYSTEM_TEXT_COLOR);

			toRemove.clear();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new Server();
		} catch (IOException e) {
			logger.severe("error establishing server on port " + PORT);
			logger.throwing("Server", "main", e);
		}
	}
}
