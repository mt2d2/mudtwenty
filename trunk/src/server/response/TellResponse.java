package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import util.ArrayUtil;

/**
 * Responds to the say command as input by the user. This command sends a
 * (private) message to the specified user.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class TellResponse implements ServerResponse
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
		if (arguments.size() < 2)
		{
			return new ClientMessage("the proper syntax of say is: say <user> <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final String reciever = arguments.get(0);
			arguments.remove(0);
			final String message = ArrayUtil.joinArguments(arguments, " ");

			serverThread.getServer().sendMessageToPlayer(reciever,
					new ClientMessage(serverThread.getPlayer().getName() + " says: " + message, Server.MESSAGE_TEXT_COLOR));
			return new ClientMessage("you said \"" + message + "\" to " + reciever);
		}
	}
}
