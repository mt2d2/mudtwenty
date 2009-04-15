package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import message.ClientMessage;
import message.InputParser;
import message.ServerMessage;

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
	private static final Logger	logger			= Logger.getLogger("mudtwenty");

	/**
	 * This is the default login message users see upon connecting to the
	 * server.
	 */
	private static final String	WELCOME_STRING	= "welcome to mudtwenty";

	private Server				server;
	private Socket				socket;
	private State				state;

	// MessageProtocol mode
	private ObjectInputStream	in;
	private ObjectOutputStream	out;

	// text mode
	private boolean				textMode;
	private BufferedReader		textIn;
	private PrintWriter			textOut;

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
	@Override
	public void run()
	{
		while (this.getState() == State.OK)
		{
			if (this.isTextMode())
			{
				try
				{
					String input = this.textIn.readLine();

					if (input == null || input.length() == 0)
					{
						this.terminateConnection();
						break;
					}
					else
					{
						final ServerMessage message = InputParser.parse(input.trim());

						try
						{
							this.sendMessage(this.server.getServerResponse(message.getCommand()).respond(this, message.getArguments()).getPayload());
						}
						catch (NullPointerException e)
						{
							// TODO this is really hacky ignoring this exception
							// pass, ignore sending null when exiting
						}
					}
				}
				catch (IOException e)
				{
					this.terminateConnection();
					break;
				}
			}
			else
			{
				try
				{
					ServerMessage input = (ServerMessage) this.in.readObject();

					if (input == null)
					{
						this.terminateConnection();
						break;
					}
					else
						this.sendMessage(this.server.getServerResponse(input.getCommand()).respond(this, input.getArguments()));
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
			this.sendMessage(message.getPayload());
		}
		else
		{
			try
			{
				this.out.writeObject(message);
			}
			catch (IOException e)
			{
				// pass, ignore sending null when exiting
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
}
