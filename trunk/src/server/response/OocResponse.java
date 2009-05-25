/**
 *
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.SystemColor;
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
			return new ClientMessage("The proper syntax is: ooc <message>", SystemColor.ERROR);
		}
		else
		{
			final String textSaid = ArrayUtil.joinArguments(arguments, " ").trim();
			ClientMessage message = new ClientMessage("Broadcast from " + serverThread.getPlayer().getName() + ": " + textSaid, Status.CHAT,
					SystemColor.MESSAGE);
			Server.sendMessageToAllClients(message);

			return new ClientMessage("You said \"" + textSaid + "\" to everyone.");
		}
	}
}
