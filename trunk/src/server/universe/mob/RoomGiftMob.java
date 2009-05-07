package server.universe.mob;

import server.universe.item.Cannon;
import server.universe.item.CheapTreat;
import server.universe.item.Cloth;
import server.universe.item.FancyTreat;
import server.universe.item.Item;
import server.universe.item.LargePotion;
import server.universe.item.SmallPotion;
import server.universe.item.Spear;
import server.universe.item.SteelMesh;
import server.universe.item.Sword;

/**
 * This MOB is a very generous one, in that it drops items throughout rooms. He
 * has a NullDialog strategy, which means he never talks. He drops random items
 * in rooms, making him something like Santa. That is, if Santa dropped random
 * items in rooms. He has a 30% of dropping an item; else he just moves around.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class RoomGiftMob extends MOB
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Make a room gift mob with the given name.
	 */
	public RoomGiftMob(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a very generous MOB. It gifts the room with various items. It would be unwise to harm him.");
		this.setDialog(new NullDialog());
	}

	/**
	 * Drop or move.
	 */
	public void takeTurn()
	{
		double random = Math.random();
		if (random < .3)
			this.setBehavior(new DropBehavior());
		else if (random < .5)
			this.setBehavior(new MoveBehavior());
		else
			this.setBehavior(new NullBehavior());
	}

	/**
	 * Randomly drops an item into the mobs current room.
	 *
	 * @author Michael Tremel (mtremel@email.arizona.edu)
	 */
	private class DropBehavior implements BehaviorStrategy
	{
		private static final long	serialVersionUID	= 1L;

		/**
		 * Generate items and drop them.
		 */
		public void doAction(MOB mob)
		{
			Item itemToGive = null;

			final double random = Math.random();

			if (random < .02)
				itemToGive = new Cannon();
			else if (random < .05)
				itemToGive = new Sword();
			else if (random < .1)
				itemToGive = new SteelMesh();
			else if (random < .2)
				itemToGive = new Cloth();
			else if (random < .3)
				itemToGive = new Spear();
			else if (random < .4)
				itemToGive = new LargePotion();
			else if (random < .5)
				itemToGive = new CheapTreat();
			else if (random < .6)
				itemToGive = new FancyTreat();
			else
				itemToGive = new SmallPotion();

			mob.getRoom().addItem(itemToGive);
		}
	}
}
