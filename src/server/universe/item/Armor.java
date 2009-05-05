package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;

/**
 * This class represents some generic armor. More specific types of armor can be
 * created by setting the attributes of this armor and cloning.
 */
public abstract class Armor extends Item
{
	private static final long	serialVersionUID	= 2L;

	private int					defensePoints;

	public int getDefensePoints()
	{
		return defensePoints;
	}

	public void setDefensePoints(int defensePoints)
	{
		this.defensePoints = defensePoints;
	}

	/**
	 * Creates armor with default characteristics.
	 */
	public Armor()
	{
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
