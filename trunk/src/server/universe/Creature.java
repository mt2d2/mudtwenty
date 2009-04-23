package server.universe;

import java.util.List;

/**
 * The Creature interface represents any living or life-like agent in the
 * universe. Creatures can move between rooms, get hurt, and have items.
 *
 * This abstract class deals with all of this simple behavior that is common
 * to both MOBs and Players.
 */
public abstract class Creature
{
	private int			maxHealth;
	private int			health;
	private List<Item>	items;
	private List<Skill>	skills;

	/**
	 * @return The maximum health of the creature.
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * @return The current health of the creature.
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * @return The list of skills that the creature has.
	 */
	public List<Skill> getSkills()
	{
		return skills;
	}

	/**
	 * @return The list of items that the creature has.
	 */
	public List<Item> getItems()
	{
		return items;
	}

	/**
	 * Add an item to the creature's inventory.
	 */
	public void addItem(Item item)
	{
		items.add(item);
	}

	/**
	 * Add an item to the creature's inventory.
	 */
	public void removeItem(Item item)
	{
		items.add(item);
	}

	/**
	 * Increase the creature's health by some amount.
	 */
	public void increaseHealth(int amount)
	{
		health += amount;
	}

}
