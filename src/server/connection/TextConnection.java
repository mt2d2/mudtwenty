package server.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import util.InputParser;

import message.ClientMessage;
import message.ServerMessage;

/**
 * Implements the Communicable interface for a connection to telnet, which
 * passes raw text to and from the client.
 */
public class TextConnection implements Communicable
{
	/**
	 * Logging utility, globally addressed in entire program as "mudtwenty".
	 * This could be augmented by using a global properties file to localize the
	 * error strings.
	 */
	private static final Logger	logger	= Logger.getLogger("mudtwenty");

	// Text mode
	private BufferedReader		textIn;
	private PrintWriter			textOut;

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#openConnection(java.net.Socket)
	 */
	@Override
	public void openConnection(Socket socket) throws IOException
	{
		this.textIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.textOut = new PrintWriter(socket.getOutputStream(), true);
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
			String input = this.textIn.readLine();

			if (input != null)
				message = InputParser.parse(input.trim());
		}
		catch (IOException e)
		{
			logger.throwing("ServerThread", "getMessageTextMode", e);
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
			this.textOut.println(message.getPayload());
		}
		catch (NullPointerException e)
		{
			// pass
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.Communicable#terminate()
	 */
	@Override
	public void terminate()
	{
		try
		{
			this.textIn.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.textOut.close();
	}
}
