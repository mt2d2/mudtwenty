package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.universe.Room;
import util.ArrayUtil;

/**
 * Responds to the say command from the user. This sends a message to everyone
 * that is in the same room as that user.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class SayResponse implements ServerResponse
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
			return new ClientMessage("proper syntax of say is say <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final Room roomOfPlayer = serverThread.getServer().getUniverse().getRoomOfCreature(serverThread.getPlayer());
			final String message = ArrayUtil.joinArguments(arguments, " ");

			serverThread.getServer().sendMessageToAllClientsInRoom(roomOfPlayer,
					new ClientMessage(serverThread.getPlayer().getName() + " says to the room: " + message, Status.CHAT, Server.MESSAGE_TEXT_COLOR));
			return new ClientMessage("you said \"" + message + "\" to everyone in the room");
		}
	}
}
