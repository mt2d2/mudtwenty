package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
	private static final int	PORT	= 8080;

	private ServerSocket		serverSocket;
	private boolean				done;
	private List<ServerThread>	clients;

	public Server() throws IOException
	{
		// Print the welcome message to the sever console
		// TODO debugging?
		this.printWelcomeMessage();

		this.serverSocket = new ServerSocket(PORT);
		this.done = false;
		this.clients = new ArrayList<ServerThread>();

		// main loop accepts clients, spawns new threads to handle each
		this.acceptClients();

		// close the server socket when finished
		this.serverSocket.close();
	}

	private void printWelcomeMessage()
	{
		System.out.println("Booting server on localhost:" + PORT);
	}

	private void sendMessageToAllClients(String message)
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
				newClient = new ServerThread(socket);
				this.clients.add(newClient);
				new Thread(newClient).start();

				// TODO debugging
				System.out.println("Got connection: " + socket);
			}
			catch (IOException e)
			{
				e.printStackTrace();
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
						{
							if (st.getState() == State.DONE)
							{
								toRemove.add(st);
								Server.this.sendMessageToAllClients("Removed client: " + st);
							}
						}
						
						Server.this.clients.removeAll(toRemove);
						toRemove.clear();
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
			e.printStackTrace();
		}
	}
}
