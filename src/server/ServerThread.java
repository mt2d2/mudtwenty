package server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import message.ClientMessage;

class ServerThread implements Runnable
{
	private static final Logger	logger	= Logger.getLogger("mudtwenty");

	private Socket				socket;
	private State				state;
	private BufferedReader		in;
	private ObjectOutputStream	out;
	private Server				server;

	public ServerThread(Server server, Socket socket)
	{
		this.server = server;
		this.socket = socket;
		this.state = State.OK;

		try
		{
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		// send a welcome message to the client
		this.sendMessage("welcome to mudtwenty", Server.SYSTEM_TEXT_COLOR);
	}

	@Override
	public void run()
	{
		while (this.getState() == State.OK)
		{
			String input = null;

			try
			{
				input = this.in.readLine();

				if (input != null)
					input = input.trim();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			if (input == null)
				break; // client closed the connection
			else
			{
				this.sendMessage("You said: " + input);

				if (input.equalsIgnoreCase("exit"))
					this.terminateConnection();

				if (input.equalsIgnoreCase("ooc"))
					this.server.sendMessageToAllClients("client" + this + " invoked ooc");
			}
		}
	}

	/**
	 * 
	 */
	private void terminateConnection()
	{
		try
		{
			this.socket.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		logger.info("client terminated connection: " + this);
		this.setState(State.DONE);
	}

	public State getState()
	{
		return this.state;
	}

	public void setState(State state)
	{
		this.state = state;
	}

	public void sendMessage(String message)
	{
		this.sendMessage(message, null);
	}

	public void sendMessage(String message, Color color)
	{
		try
		{
			this.out.writeObject(new ClientMessage(message, color));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
