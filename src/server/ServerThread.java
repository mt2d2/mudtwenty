package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

 class ServerThread implements Runnable
{
	private Socket	socket;
	private State	state;
	private BufferedReader in;
	private PrintWriter out;

	public ServerThread(Socket socket)
	{
		this.socket = socket;
		this.state = State.OK;
		
		try
		{
			this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run()
	{
		while (this.getState() == State.OK)
		{			
			String input = null;
			
			try
			{
				input = this.in.readLine().trim();
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
				{
					try
					{
						this.socket.close();
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
					
					this.setState(State.DONE);
				}
			}		
		}
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
		this.out.println(message);
		this.out.flush();
	}
}
