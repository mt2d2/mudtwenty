package server.universe;

import java.util.List;

/**
 * The Creature interface represents any living or life-like agent in the
 * universe. Creatures can move between rooms, get hurt, and attack others.
 */
public abstract class Creature
{
	protected int			maxHealth;
	protected int			health;
	protected List<Item>	items;
	protected List<Skill>	skills;

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

}
