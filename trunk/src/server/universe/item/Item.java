package server.universe.item;

import java.io.Serializable;

import message.ClientMessage;
import server.universe.Creature;
import server.universe.Entity;

/**
 * Item represents anything in the world that's not a Creature --
 * anything that has no intelligence or ability to do thing like talking and
 * fighting.
 * 
 * Item is an abstract class because there exists some methods that
 * are common to all subclasses -- getters and setters.
 */
public abstract class Item implements Entity, Serializable
{
	
	private String name;
	private String description;
	private int price;

	/**
	 * Create an item with default characteristics.
	 * The attributes of the item should probably always be set,
	 * and should not be left at these default values.
	 */
	public Item()
	{
		this.name = "item";
		this.description = "This strange item defies all description.";
		this.price = 0;
	}
	
	
	/**
	 * Get the name of an item. This is the name that can be used
	 * to reference the item.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Get the description of an item. This is what it looks like and what it is.
	 */
	public String getDescription()
	{
		return this.description;
	}
	
	/**
	 * Get the price of an item, in the standard money system
	 */
	public int getPrice()
	{
		return this.price;
	}
	
	/**
	 * Set the name.
	 */
	public void setName(String name)
	{
		this.name = name; 
	}

	/**
	 * Set the description.
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	/**
	 * Set the price.
	 */
	public void setPrice(int price)
	{
		this.price = price;
	}
	
	/**
	 * Uses a specified item on the specified creature.
	 *   
	 * @param creature that the item is used on
	 * @return A ClientMessage that will be used to notify user of the item
	 *         if the item is a player, but will be ignored if the user is a mob.
	 */
	public abstract ClientMessage use(Creature creature);
	
}