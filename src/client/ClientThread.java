package client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import message.ClientMessage;
import message.ServerMessage;
import message.Status;

/**
 * This class is responsible for initiating a socket connection to the given
 * server, sending messages, and recieving messages from Server.
 * 
 * The ClientThread is created and controlled by {@link Client}, which is the
 * GUI.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ClientThread implements Runnable
{
	private Client				client;
	private Socket				socket;
	private ObjectInputStream	in;
	private ObjectOutputStream	out;
	private boolean				done;

	/**
	 * Sole constructor. Attempts to open a socket connection on server port.
	 * 
	 * @param client
	 *            GUI owner of this
	 * @param server
	 *            host to open socket to
	 * @param port
	 *            port on which the socket will be opened
	 * @throws UnknownHostException
	 */
	public ClientThread(Client client, String server, int port) throws ConnectException, UnknownHostException
	{
		try
		{
			this.client = client;
			this.socket = new Socket(InetAddress.getByName(server), port);
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.done = false;
		}
		catch (UnknownHostException e)
		{
			throw new UnknownHostException();
		}
		catch (ConnectException e)
		{
			throw new ConnectException();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Send a message to the server.
	 */
	public void sendMessage(ServerMessage message)
	{
		try
		{
			this.out.writeObject(message);
			this.out.flush();

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Get a message from the server and return it. Return null if something
	 * went wrong.
	 * 
	 * @return message sent from Server
	 */
	public ClientMessage getMessage()
	{
		// TODO some of these exceptions need to be looked at?
		// Returning null from here terminates the connection;
		// Some of these might not require terminating the connection?
		try
		{
			return (ClientMessage) this.in.readObject();
		}
		catch (Exception e)
		{
			// Possible exceptions:
			// NullPointerException, SocketException, IOException,
			// ClassNotFoundException.
			return null;
		}
	}

	/**
	 * Run in a loop of getting messages from the server and displaying them.
	 */
	public void run()
	{
		while (!this.done)
		{
			final ClientMessage message = this.getMessage();

			if (message == null)
			{
				this.client.renewInterface();

				JOptionPane.showMessageDialog(this.client, "The server connection has been terminated.");
				this.client.switchCard(Client.CONNECTOR_CARD);

				break;
			}
			else
			{
				String dateText = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(message.getTime()));
				String textToDisplay = dateText + "> " + message.getPayload().trim();
				this.displayServerMessage(textToDisplay, message.getColor(), message.getStatus());
			}
		}
	}

	/**
	 * Appends text to the terminal emulator on the EDT.
	 * 
	 * @param input
	 *            message to be added
	 * @param color
	 *            color in which the message will be added
	 */
	private void displayServerMessage(final String input, final Color color, final Status status)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run()
			{
				if (status == Status.CHAT)
					ClientThread.this.client.appendChatText(input, color);
				else
					ClientThread.this.client.appendGameText(input, color);
			}
		});
	}

	/**
	 * Closes the connection to the server. The InputStreams and OutputStreams
	 * are closed, and finally the socket is closed.
	 */
	public void closeConnection()
	{
		try
		{
			this.in.close();
			this.out.close();
			this.socket.close();
		}
		catch (NullPointerException e)
		{
			// pass
			// TODO, really pass?
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
