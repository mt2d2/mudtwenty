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
 * Responds to the ooc command as directed by the user. ooc will send a message
 * to every client which is connected to the server, regardless of status.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class OocResponse implements ServerResponse
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
		if (arguments.size() < 1)
		{
			return new ClientMessage("the proper syntax of say is: ooc <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final String message = ArrayUtil.joinArguments(arguments, " ");
			serverThread.getServer().sendMessageToAllClients(
					new ClientMessage("Broadcast from " + serverThread.getPlayer().getName() + ": " + message, Status.CHAT, Server.MESSAGE_TEXT_COLOR));

			return new ClientMessage("you said \"" + message + "\" to everyone.");
		}
	}
}
