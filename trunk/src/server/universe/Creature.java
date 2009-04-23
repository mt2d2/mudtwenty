package server.universe;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * The Creature interface represents any living or life-like agent in the
 * universe. Creatures can move between rooms, get hurt, and have items.
 * 
 * This abstract class deals with all of this simple behavior that is common to
 * both MOBs and Players.
 */
public abstract class Creature implements Entity
{
	private String				name;
	private String				description;
	private int					maxHealth;
	private int					health;
	private List<Item>			items;
	private Map<Skill, Integer>	skills;

	/**
	 * We should change this later.
	 */
	private static final int	defaultMaxHealth	= 100;

	/**
	 * Construct a creature with the given name. Other attributes of the
	 * creature that are not specified in the constructor can be set with
	 * mutator methods.
	 */
	protected Creature(String name)
	{
		this.name = name;
		this.description = "";
		this.maxHealth = defaultMaxHealth;
		this.health = maxHealth;
		this.items = new ArrayList<Item>();
		// TODO replace type of map with EnumMap because it's a lot more
		// efficient.
		this.skills = new HashMap<Skill, Integer>();
	}

	/**
	 * Return the name of the Creature.
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Return the description of the Creature.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Set the description of the Creature.
	 */
	public void setDescription(String newDescription)
	{
		this.description = newDescription;
	}

	/**
	 * @return The maximum health of the creature.
	 */
	public int getMaxHealth()
	{
		return maxHealth;
	}

	/**
	 * Set the maximum health of the Creature to the given amount. If current
	 * health is less than this, the creature might die?
	 */
	public void setMaxHealth(int amount)
	{
		maxHealth = amount;
	}

	/**
	 * @return The current health of the creature.
	 */
	public int getHealth()
	{
		return health;
	}

	/**
	 * Increase the creature's health by some amount.
	 */
	public void increaseHealth(int amount)
	{
		if (health + amount >= maxHealth)
		{
			health = maxHealth;
		}
		else
		{
			health += amount;
		}
	}

	/**
	 * Decrease the creature's health by some amount. If health goes to zero,
	 * the Creature should die?
	 */
	public void decreaseHealth(int amount)
	{
		health -= amount;
	}

	/**
	 * Check the value of a skill. If the skill is not in the map of skills, it
	 * is zero.
	 * 
	 * @return The value of the given skill.
	 */
	public int getSkillValue(Skill skill)
	{
		return 0;
	}

	/**
	 * Practice the skill. This has a chance of increasing the value of the
	 * skill. The result of practicing a skill should depend on the current
	 * skill level and it should have a chance of increasing the skill.
	 * 
	 * If the skill is not in the map, it is zero. If it becomes nonzero through
	 * practice, put it into the map with the new value.
	 */
	public void practice(Skill skill)
	{
	}

	/**
	 * Set the value of a skill. If the skill is not in the map, create it with
	 * the given value.
	 */
	public void setSkill(Skill skill)
	{
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
	 * Remove an item from the creature's inventory.
	 */
	public void removeItem(Item item)
	{
		items.remove(item);
	}

}
