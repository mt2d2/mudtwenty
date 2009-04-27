package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
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

	/**
	 * Send message to receiver and notify sender.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 2)
		{
			return new ClientMessage("the proper syntax of say is: say <user> <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			// Get and remove the receiver from the argument list.
			final String reciever = arguments.get(0);
			arguments.remove(0);

			final String textSaid = ArrayUtil.joinArguments(arguments, " ");
			ClientMessage message = new ClientMessage(serverThread.getPlayer().getName()
				+ " says: " + textSaid, Status.CHAT, Server.MESSAGE_TEXT_COLOR);
			Server.sendMessageToPlayer(reciever, message);

			return new ClientMessage("you said \"" + textSaid + "\" to " + reciever);
		}
	}
}
