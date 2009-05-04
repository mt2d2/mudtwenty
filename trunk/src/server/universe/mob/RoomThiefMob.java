package server.universe.mob;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.universe.Creature;
import server.universe.Room;

/**
 * This Mob steals from rooms. It has a 10% chance of taking an item from a
 * room, or simply moving on to the next one.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class RoomThiefMob extends MOB
{
	private static final long	serialVersionUID	= 1L;

	public RoomThiefMob(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a very mean MOB. He steals the first item he can find a room and moves on. It might be a good idea to kill him.");
		this.setDialog(new IntimidationDialog());
	}

	@Override
	public void takeTurn()
	{
		if (Math.random() < .1)
			this.setBehavior(new DropBehavior());
		else
			this.setBehavior(new MoveBehavior());
	}

	/**
	 * Let's all other players in the room know this Mob's true intentions.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class IntimidationDialog implements DialogStrategy
	{
		private static final long	serialVersionUID	= 1L;

		@Override
		public void tell(Creature sender, String message)
		{
			Server.getUniverse().sendMessageToCreature(RoomThiefMob.this, sender,
					new ClientMessage("Thief says muhaha!", Status.CHAT, Server.MESSAGE_TEXT_COLOR));
		}
	}

	/**
	 * Takes the first item from the mob's current room.
	 * 
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class DropBehavior implements BehaviorStrategy
	{
		private static final long	serialVersionUID	= 1L;

		@Override
		public void doAction(MOB mob)
		{
			Room room = mob.getRoom();

			if (room.getItems().size() > 0)
				room.removeItem(room.getItems().get(0));
		}
	}
}
