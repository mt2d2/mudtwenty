/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Responds to the register command by the user.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class RegisterResponse implements ServerResponse
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
			response.append("invalid use of register command, the syntax is register <username> <password>");
		}
		else
		{
			if (serverThread.register(arguments.get(0), arguments.get(1)))
				response.append("success in registering, please use the login command to login");
			else
				response.append("there was an error in registering, contact the administrator");
		}

		return new ClientMessage(response.toString());
	}
}
