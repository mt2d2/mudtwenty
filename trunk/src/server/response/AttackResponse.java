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
import server.universe.mob.Troll;
import util.ArrayUtil;

/**
 * Responds to the say command as input by the user. This command sends a
 * (private) message to the specified user.
 */
public class AttackResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		final Player sender = serverThread.getPlayer();
		final String attackee = ArrayUtil.joinArguments(arguments, " ").trim();
		
		Creature receiver = null;

		Room room = Server.getUniverse().getRoomOfCreature(sender);
		for (MOB mob : Server.getUniverse().getMOBsInRoom(room))
			if (mob.getName().equals(attackee))
				receiver = mob;

		if (receiver != null)
		{
			return ((Troll)receiver).attack(sender);
		}
		else
		{
			return new ClientMessage("No such MOB to attack.", Server.ERROR_TEXT_COLOR);
		}
	}
}
