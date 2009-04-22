/**
 * 
 */
package server.response;

import java.awt.Color;
import java.util.List;

import message.ClientMessage;
import server.Server;
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
		if (arguments.size() != 2)
		{
			return new ClientMessage("invalid use of register command, the syntax is register <username> <password>", Color.RED);
		}
		else
		{
			if (serverThread.register(arguments.get(0), arguments.get(1)))
				return new ClientMessage("success in registering, please use the login command to login", Server.SYSTEM_TEXT_COLOR);
			else
				return new ClientMessage("there was an error in registering, the provided name is already in use", Color.RED);
		}
	}
}
