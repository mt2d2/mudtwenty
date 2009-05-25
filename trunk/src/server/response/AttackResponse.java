package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.SystemColor;
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
	 * Attacks the target if possible.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 1)
		{
			return new ClientMessage("The proper syntax is: attack <mob name>", SystemColor.ERROR);
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

			for (Player player : Server.getUniverse().getPlayersInRoom(room))
				if (player.getName().equals(attackeeName))
					attackee = player;

			if (attackee == null)
				return new ClientMessage("No such MOB or player to attack; some MOBS cannot be attacked", SystemColor.ERROR);

			if (attackee instanceof Troll)
			{
				return ((Troll) attackee).attack(attacker);
			}
			else if (attackee instanceof Player)
			{
				if (attackee.getHealth() == 0)
					return new ClientMessage(attackee.getName() + " has no health left! No need to attack.");

				int attack = attacker.getAttack();
				attackee.decreaseHealth(attack);
				Server.getUniverse().sendMessageToCreature(attacker, attackee, new ClientMessage(attacker.getName() + " just hit you for " + attack + " points"));
				if (attackee.getHealth() == 0)
					return new ClientMessage("You killed " + attackee.getName());
				else
					return new ClientMessage("You attack " + attackee.getName() + " for " + attack + " points");
			}

			return null;
		}
	}
}