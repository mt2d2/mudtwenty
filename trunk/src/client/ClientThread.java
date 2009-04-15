package client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import message.ClientMessage;

public class ClientThread implements Runnable
{
	private Client				client;
	private Socket				socket;
	private ObjectInputStream	in;
	private ObjectOutputStream	out;
	private boolean				done;

	public ClientThread(Client client, String server, int port)
	{
		try
		{
			this.client = client;
			this.socket = new Socket(server, port);
			this.out = new ObjectOutputStream(this.socket.getOutputStream());
			this.in = new ObjectInputStream(this.socket.getInputStream());
			this.done = false;
		}
		catch (ConnectException e)
		{
			JOptionPane.showMessageDialog(this.client, "A problem connecting to the server was encourtered");
		}
		catch (UnknownHostException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendMessage(String input)
	{
		try
		{
			this.out.writeObject(InputParser.parse(input));
			this.out.flush();

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ClientMessage getMessage()
	{
		// TODO some of these exceptions need to be looked at
		try
		{
			return (ClientMessage) this.in.readObject();
		}
		catch (NullPointerException e)
		{
			return null;
		}
		catch (SocketException ex)
		{
			return null; // Disconnected
		}
		catch (IOException ioe)
		{
			return null;
		}
		catch (ClassNotFoundException e)
		{
			return null;
		}
	}

	@Override
	public void run()
	{
		while (!this.done)
		{
			final ClientMessage message = this.getMessage();

			if (message == null)
			{
				this.displayServerMessage("Server connection has been terminated.", Color.RED);
				break;
			}
			else
				this.displayServerMessage(
						DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date(message.getTime())) + "> " + message.getPayload().trim(), message
								.getColor());
		}
	}

	private void displayServerMessage(final String input, final Color color)
	{
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				ClientThread.this.client.appendServerText(input, color);
			}
		});
	}

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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}