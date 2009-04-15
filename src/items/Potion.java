package items;

import base.Creature;
import base.Item;

/**
 * Heals the creature the action is performed on some amount of health.
 */
public class Potion extends Item {
	// Number of health points to heal
	private int healingPower;

	/**
	 * Constructor of super.
	 * @param name
	 * @param description
	 */
	public Potion(String name, int healingPower) {
		super(name);
		this.healingPower = healingPower;
	}

	/**
	 * Increases health and destroys potion.
	 */
	public void performActionOn(Creature creature) {
		creature.increaseHealth(this.healingPower);
		creature.removeItem(this);
	}

	@Override
	public String toString() {
		return "Consume this magical elixer to heal " + healingPower + " health points.";
	}
}
