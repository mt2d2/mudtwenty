package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;

/**
 * This class represents some generic armor. More specific types of armor can be
 * created by setting the attributes of this armor and cloning.
 */
public abstract class Armor extends Item
{
	private static final long	serialVersionUID	= 3L;

	private int					defensePoints;

	/**
	 * Get the defense rating of this armor.
	 */
	public int getDefensePoints()
	{
		return defensePoints;
	}

	/**
	 * Set the defense rating.
	 */
	public void setDefensePoints(int defensePoints)
	{
		this.defensePoints = defensePoints;
	}

	/**
	 * Equip the armor.
	 */
	public ClientMessage use(Creature creature)
	{
		creature.equip(this);
		return new ClientMessage("You equipped " + this.getName());
	}
}
