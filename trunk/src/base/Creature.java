package base;

import java.util.LinkedList;
import java.util.List;

/**
 * Model for how players and mobs act
 * @author Simon
 *
 */
public abstract class Creature extends Entity {
	protected int totalHealth;
	protected int currentHealth;
	protected List<Item> items;
	
	/**
	 * Default constructor.
	 * @param name
	 * @param description
	 */
	public Creature(String name) {
		super(name);
		this.items = new LinkedList<Item>();
		this.level = 1;
		this.generateStats();
	}
	
	/**
	 * Overload constructor takes in stats parameters.
	 * @param name
	 * @param description
	 */
	public Creature(String name, int level) {
		super(name);
		this.items = new LinkedList<Item>();
		this.level = level;
		generateStats();
	}
	
	/**
	 * Figures out what health, money, other stats
	 * will be.
	 */
	public void generateStats() {
		this.totalHealth = this.level * 10;
		this.currentHealth = this.totalHealth;
	}
	
	/**
	 * Returns a list of items currently in possession.
	 * @return
	 */
	public List<Item> listItems() {
		return this.items;
	}
	
	/**
	 * Adds an item to the creature's collection.
	 * @param item
	 */
	public boolean addItem(Item item) {
		return this.items.add(item);
	}
	
	/**
	 * Removes an item from the creature's collection.
	 */
	public boolean removeItem(Item item) {
		return this.items.remove(item);
	}

	/**
	 * Increases currentHealth by some amount, not to exceed
	 * the totalHealth though.
	 * @param healingPower
	 */
	public void increaseHealth(int healingPower) {
		this.currentHealth += healingPower;
		if (this.currentHealth > this.totalHealth)
			this.currentHealth = this.totalHealth;
	}
	
	/**
	 * Decreases currentHealth by some amount, calling Die() if below 0.
	 * @param healingPower
	 */
	public void decreaseHealth(int harmingPower) {
		this.currentHealth -= harmingPower;
		if (this.currentHealth <= 0)
			this.die();
	}
	
	/**
	 * What happens when a creature dies.
	 */
	public abstract void die();
}
