package server.universe.mob;

import server.universe.item.Armor;
import server.universe.item.Item;
import server.universe.item.Potion;
import server.universe.item.Weapon;

/**
 * This MOB is a very generous one, in that it drops items throughout rooms. He
 * has a NullDialog strategy, which means he never talks. He drops random items
 * in rooms, making him something like Santa. He has a 10% of dropping an item
 * or just moves around.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class RoomGiftMob extends MOB
{
	private static final long	serialVersionUID	= 1L;

	public RoomGiftMob(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a very generous MOB. It gifts the room with various items. It would be unwise to harm him.");
		this.setDialog(new NullDialog());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.universe.mob.MOB#takeTurn()
	 */
	@Override
	public void takeTurn()
	{
		if (Math.random() < .1)
			this.setBehavior(new DropBehavior());
		else
			this.setBehavior(new MoveBehavior());
	}

	private class DropBehavior implements BehaviorStrategy
	{
		private static final long	serialVersionUID	= 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * server.universe.mob.BehaviorStrategy#doAction(server.universe.mob
		 * .MOB)
		 */
		@Override
		public void doAction(MOB mob)
		{
			Item itemToGive = null;
			
			if (Math.random() < .1)
				itemToGive = new Armor();
			else if (Math.random() < .2)
				itemToGive = new Weapon();
			else 
				itemToGive = new Potion();
						
			mob.getRoom().addItem(itemToGive);
		}
	}
}
