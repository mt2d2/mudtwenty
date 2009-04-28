/**
 *
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import util.ArrayUtil;

/**
 * Responds to the ooc command as directed by the user.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class OocResponse implements ServerResponse
{

	/**
	 * Send a message to every client which is connected to the server,
	 * regardless of where they are in the Universe.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("the proper syntax of say is: ooc <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final String textSaid = ArrayUtil.joinArguments(arguments, " ");
			ClientMessage message = new ClientMessage("Broadcast from " + serverThread.getPlayer().getName() + ": "
				+ textSaid , Status.CHAT, Server.MESSAGE_TEXT_COLOR);
			Server.sendMessageToAllClients(message);

			return new ClientMessage("you said \"" + textSaid + "\" to everyone.");
		}
	}
}
