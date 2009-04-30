package server.connection;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

import message.ClientMessage;
import message.ServerMessage;
import server.Server;
import util.InputParser;

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
			final String textColor = this.translateColor(message.getColor());
			// add black to the end
			this.textOut.println(textColor + message.getPayload() + "\u001B[30m");
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

	/**
	 * Translates a java Color object to the proper ANSI escape code for color.
	 * Defaults to black.
	 * 
	 * @param color
	 *            Color to mimic
	 * @return String escape code representing that color
	 */
	private String translateColor(Color color)
	{
		if (color == Server.ERROR_TEXT_COLOR)
		{
			return "\u001B[31m";
		}
		else if (color == Server.SYSTEM_TEXT_COLOR)
		{
			return "\u001B[32m";
		}
		else if (color == Server.MESSAGE_TEXT_COLOR)
		{
			return "\u001B[34m";
		}
		else
		{
			return "\u001B[30m";
		}
	}
}
