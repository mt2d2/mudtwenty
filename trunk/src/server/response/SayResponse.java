package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.SystemColor;
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
			return new ClientMessage("The proper syntax is: say <message>", SystemColor.ERROR);
		}
		else
		{
			final Player sender = serverThread.getPlayer();
			final Room room = sender.getRoom();
			final String textSaid = ArrayUtil.joinArguments(arguments, " ").trim();
			ClientMessage message = new ClientMessage(sender.getName() + " says to room: " + textSaid, Status.CHAT, SystemColor.MESSAGE);
			Server.getUniverse().sendMessageToCreaturesInRoom(sender, room, message);

			return new ClientMessage("You said \"" + textSaid + "\" to everyone in the room.");
		}
	}
}
