package server.universe.mob;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.SystemColor;
import server.universe.Creature;
import server.universe.Room;

/**
 * This Mob steals from rooms. It has a 20% chance of taking an item from a
 * room, or simply moving on to the next one.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class RoomThiefMob extends MOB
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Make a thief with the given name.
	 */
	public RoomThiefMob(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a very mean MOB. He steals the first item he can find a room and moves on." + "It might be a good idea to kill him.");
		this.setDialog(new IntimidationDialog());
	}

	/**
	 * Steal or move.
	 */
	public void takeTurn()
	{
		double random = Math.random();
		if (random < .2)
			this.setBehavior(new StealBehavior());
		else if (random < .4)
			this.setBehavior(new MoveBehavior());
		else
			this.setBehavior(new NullBehavior());
	}

	/**
	 * Let all other players in the room know this Mob's true intentions.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class IntimidationDialog implements DialogStrategy
	{
		private static final long	serialVersionUID	= 1L;

		public void tell(Creature sender, String message)
		{
			Server.getUniverse().sendMessageToCreature(RoomThiefMob.this, sender,
					new ClientMessage("Thief says muhaha!", Status.CHAT, SystemColor.MESSAGE));
		}
	}

	/**
	 * Takes the first item from the mob's current room.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class StealBehavior implements BehaviorStrategy
	{
		private static final long	serialVersionUID	= 1L;

		public void doAction(MOB mob)
		{
			Room room = mob.getRoom();

			if (!room.getItems().isEmpty())
				room.removeItem(room.getItems().get(0));
		}
	}
}
