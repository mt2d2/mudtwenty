package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;

/**
 * This class represents some generic armor.
 * More specific types of armor can be created by setting
 * the attributes of this armor and cloning.
 */
public class Armor extends Item
{
	private static final long	serialVersionUID	= 2L;

	/**
	 * Creates armor with default characteristics.
	 */
	public Armor()
	{
		setName("leather armor");
		setDescription("Protect yourself from attacks.");
		setPrice(5);
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
