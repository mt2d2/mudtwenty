package client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class ClientThread implements Runnable
{
	private Client			client;
	private Socket			socket;
	private BufferedReader	in;
	private PrintWriter		out;
	private boolean			done;

	public ClientThread(Client client, String server, int port)
	{
		try
		{
			this.client = client;
			this.socket = new Socket(server, port);
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out = new PrintWriter(socket.getOutputStream(), true);
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

	public void sendMessage(String message)
	{
		this.out.println(message);
		this.out.flush();
	}

	public String getLine()
	{
		try
		{
			return this.in.readLine();
		}
		catch(NullPointerException e)
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
	}

	public boolean hasInput()
	{
		return true;
	}

	@Override
	public void run()
	{
		while (!this.done)
		{
			String input = this.getLine();

			if (input == null)
			{
				this.displayServerMessage("Server connection has been terminated.", Color.RED);
				break;
			}
			else
				this.displayServerMessage(input.trim());
		}
	}

	private void displayServerMessage(final String input)
	{
		this.displayServerMessage(input, null);
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
}
