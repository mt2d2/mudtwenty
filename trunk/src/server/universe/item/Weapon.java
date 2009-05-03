package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;


/**
 * This class represents some generic weapon.
 * More specific types of armor could be created by setting attributes.
 */
public class Weapon extends Item
{
	private static final long	serialVersionUID	= 1L;

	private int damage;
	
	/**
	 * Construct a weapon with default characteristics.
	 */
	public Weapon()
	{
		setName("sword");
		setDescription("An instrument of death. Not very useful in times of peace.");
		setPrice(5);
	}
	
	/**
	 * Get the damage of the weapon.
	 * Later, this could return varying amounts of damage.
	 */
	public int getDamage()
	{
		return this.damage;
	}
	
	/**
	 * Set the damage of the weapon to some specific constant value.
	 */
	public void setDamage(int damage)
	{
		this.damage = damage;
	}
	
	/**
	 * Equip the weapon.
	 */
	public ClientMessage use(Creature creature)
	{
		creature.equip(this);
		return new ClientMessage("You equipped " + this.getName());
	}
}
