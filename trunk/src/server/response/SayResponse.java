package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.universe.Player;
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

	/**
	 * Send a message to everyone in the room.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("proper syntax of say is say <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final Player sender = serverThread.getPlayer();
			final Room room = Server.getUniverse().getRoomOfCreature(sender);
			final String textSaid = ArrayUtil.joinArguments(arguments, " ").trim();
			
			Server.getUniverse().sendMessageToCreaturesInRoom(sender, room, new ClientMessage(textSaid, Status.CHAT, Server.MESSAGE_TEXT_COLOR));
			
			return new ClientMessage("You said \"" + textSaid + "\" to everyone in the room.");
		}
	}
}
