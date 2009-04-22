package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.universe.InvalidLoginException;

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
		StringBuilder response = new StringBuilder();

		if (arguments.size() != 2)
		{
			response.append("invalid use of login command, the syntax is login <username> <password>");
		}
		else
		{
			try
			{
				if (serverThread.login(arguments.get(0), arguments.get(1)))
					response.append("login successful, you can confirm this with the who command");
				else
					response.append("login was unsuccessful, the username is already in use");
			}
			catch (InvalidLoginException e)
			{
				response.append(e.getMessage());
			}
		}

		return new ClientMessage(response.toString());
	}
}
