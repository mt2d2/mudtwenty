package server.connection;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import message.ClientMessage;
import message.ServerMessage;

/**
 * Implements the Communicable interface for a connection to the GUI, which uses
 * Message as its communication Protocol.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class MessageConnection implements Communicable
{
	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger	logger	= Logger.getLogger("mudtwenty");

	// MessageProtocol mode
	private ObjectInputStream	objectIn;
	private ObjectOutputStream	objectOut;

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#openConnection()
	 */
	@Override
	public void open(Socket socket) throws IOException
	{
		this.objectIn = new ObjectInputStream(socket.getInputStream());
		this.objectOut = new ObjectOutputStream(socket.getOutputStream());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#getMessage()
	 */
	@Override
	public ServerMessage getMessage()
	{
		ServerMessage message = null;

		try
		{
			message = (ServerMessage) this.objectIn.readObject();
		}
		catch (EOFException e)
		{
			logger.throwing("MessageConnection", "getMessage", e);
		}
		catch (IOException e)
		{
			logger.throwing("MessageConnection", "getMessage", e);
		}
		catch (ClassNotFoundException e)
		{
			logger.throwing("MessageConnection", "getMessage", e);
		}

		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#sendMessage(message.ClientMessage)
	 */
	@Override
	public void sendMessage(ClientMessage message)
	{
		try
		{
			this.objectOut.writeObject(message);
		}
		catch (IOException e)
		{
			logger.throwing("MessageConnection", "sendMessage", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#terminate()
	 */
	@Override
	public void close()
	{
		try
		{
			this.objectIn.close();
			this.objectOut.close();
		}
		catch (IOException e)
		{
			logger.throwing("MessageConnection", "close", e);
		}
	}

}
