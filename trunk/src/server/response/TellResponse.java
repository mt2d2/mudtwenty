package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.universe.Creature;
import server.universe.Player;
import server.universe.Room;
import server.universe.mob.MOB;
import util.ArrayUtil;

/**
 * Responds to the say command as input by the user. This command sends a
 * (private) message to the specified user.
 */
public class TellResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 2)
		{
			return new ClientMessage("the proper syntax of say is: say <user> <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final Player sender = serverThread.getPlayer();
			final String receiverName = arguments.get(0);
			final String textSaid = ArrayUtil.joinArguments(arguments.subList(1, arguments.size()), " ").trim();

			Creature receiver = null;
			
			// check whether receiver is a player
			Player player = Server.getUniverse().getPlayer(receiverName);
			if (Server.getUniverse().isLoggedIn(player))
			{
				receiver = Server.getUniverse().getPlayer(receiverName);
			}
			else
			{
				Room room = Server.getUniverse().getRoomOfCreature(sender);
				for (MOB mob : Server.getUniverse().getMOBsInRoom(room))
					if (mob.getName().equals(receiverName))
						receiver = mob;
			}

			if (receiver != null)
			{
				Server.getUniverse().sendMessageToCreature(sender, receiver, new ClientMessage(textSaid, Status.CHAT, Server.MESSAGE_TEXT_COLOR));
				return new ClientMessage("You said \"" + textSaid + "\" to " + receiverName + ".");
			}
			else
			{
				return new ClientMessage("No such player or MOB.", Server.ERROR_TEXT_COLOR);
			}
			
		}
	}
}
