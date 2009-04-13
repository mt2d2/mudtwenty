package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Server
{
	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger	logger	= Logger.getLogger("mudtwenty");

	/**
	 * Port the server will run on this local host. In the future, this could be
	 * read from a configuration file.
	 */
	private static final int	PORT	= 8080;

	private List<ServerThread>	clients;
	private ServerSocket		serverSocket;
	private boolean				done;

	public Server() throws IOException
	{
		// Display welcome message
		logger.info("booting server on localhost:" + PORT);

		this.clients = new ArrayList<ServerThread>();
		this.serverSocket = new ServerSocket(PORT);
		this.done = false;

		// main loop accepts clients, spawns new threads to handle each
		this.acceptClients();

		// close the server socket when finished
		this.serverSocket.close();
	}

	public void sendMessageToAllClients(String message)
	{
		for (ServerThread st : this.clients)
			st.sendMessage(message);
	}

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

			// reaper thread
			new Thread(new Runnable() {
				@Override
				public void run()
				{
					List<ServerThread> toRemove = new ArrayList<ServerThread>();

					while (true)
					{
						for (ServerThread st : Server.this.clients)
							if (st.getState() == State.DONE)
								toRemove.add(st);

						Server.this.clients.removeAll(toRemove);
						toRemove.clear();

						// TODO thread sleeping every second? Timer instead?
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
					}
				}

			}).start();
		}
	}

	/**
	 * @param args
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
