package items;

/**
 * Heals the creature the action is performed on some amount of health.
 */
public class SmallPotion extends Potion {
	private final static String NAME = "Small Potion";
	private final static int HEALING_POWER = 5;
	
	/**
	 * Constructor of super.
	 * @param name
	 * @param description
	 */
	public SmallPotion() {
		super(NAME, HEALING_POWER);
	}
}
