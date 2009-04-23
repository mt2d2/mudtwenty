package server.response;

import java.awt.Color;
import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.InvalidLoginException;

/**
 * Handles the login request from a user. This command requires two arguments, a
 * username and password.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class LoginResponse implements ServerResponse
{
	/*
	 * (non-Javadoc)
	 *
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() != 2)
		{
			return new ClientMessage("invalid use of login command, the syntax is login <username> <password>", Color.RED);
		}
		else
		{
			try
			{
				if (serverThread.login(arguments.get(0), arguments.get(1)))
					return new ClientMessage("login successful, you can confirm this with the who command", Server.SYSTEM_TEXT_COLOR);
				else
					return new ClientMessage("login was unsuccessful, contact administrator", Color.RED);
			}
			catch (InvalidLoginException e)
			{
				return new ClientMessage(e.getMessage(), Color.RED);
			}
		}
	}
}
