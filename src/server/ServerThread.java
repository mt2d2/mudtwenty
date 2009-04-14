package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import message.ClientMessage;
import message.ServerMessage;

class ServerThread implements Runnable
{
	private static final Logger	logger	= Logger.getLogger("mudtwenty");

	private Socket				socket;
	private State				state;
	private ObjectInputStream	in;
	private ObjectOutputStream	out;
	private Server				server;

	public ServerThread(Server server, Socket socket)
	{
		this.server = server;
		this.socket = socket;
		this.state = State.OK;

		try
		{
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
		}
		catch (IOException e)
		{
			logger.info("error establishing in and out streams on ServerThread: " + this);
			logger.throwing("ServerThread", "ServerThread", e);
		}

		// send a welcome message to the client
		this.sendMessage(new ClientMessage("welcome to mudtwenty", Server.SYSTEM_TEXT_COLOR));
	}

	@Override
	public void run()
	{
		while (this.getState() == State.OK)
		{
			try
			{
				ServerMessage input = (ServerMessage) this.in.readObject();

				if (input == null)
					break;
				else
					this.sendMessage(this.server.getServerResponse(input.getCommand()).respond(input.getArguments()));
			}
			catch (IOException e)
			{
				this.terminateConnection();
				break;
			}
			catch (ClassNotFoundException e)
			{
				this.terminateConnection();
				break;
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
			this.in.close();
			this.out.close();
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

	public void sendMessage(ClientMessage message)
	{
		try
		{
			this.out.writeObject(message);
		}
		catch (IOException e)
		{
			logger.warning("error writing object on ServerThread: " + this);
			logger.throwing("ServerThread", "sendMessage", e);
		}
	}
}
