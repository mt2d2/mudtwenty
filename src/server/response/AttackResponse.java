package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Creature;
import server.universe.Player;
import server.universe.Room;
import server.universe.mob.MOB;
import server.universe.mob.Troll;
import util.ArrayUtil;

/**
 * Responds to the attack command as input by the user.
 */
public class AttackResponse implements ServerResponse
{

	/**
	 * Attack the given MOB, if possible.
	 * 
	 * PRECONDITION: currently, the only attackable mob is Troll.
	 * This is because the only mob with an attack
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("The proper syntax is: attack <mob name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			final Player attacker = serverThread.getPlayer();
			final String attackeeName = ArrayUtil.joinArguments(arguments, " ").trim();
			
			Creature attackee = null;
			Room room = attacker.getRoom();
			for (MOB mob : Server.getUniverse().getMOBsInRoom(room))
				if (mob.getName().equals(attackeeName) && mob instanceof Troll)
					attackee = mob;

			// Notify user if there is no such mob.
			if (attackee == null)
				return new ClientMessage("No such MOB to attack.", Server.ERROR_TEXT_COLOR);
			
			// Attack.
			return ((Troll) attackee).attack(attacker);
		}
	}
}
