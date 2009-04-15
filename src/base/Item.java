package base;

/**
 * Item objects that can do work on Players and Mobs
 */
public abstract class Item extends Entity {
	/**
	 * Super constructor.
	 * @param name
	 */
	public Item(String name) {
		super(name);
	}
	
	/**
	 * Should never be called, as players can interact
	 * with all other players.
	 */
	public void noInteraction(Creature creature) {
		// TODO
		// let user know he needs a higher level to interact
		// with this object
	}

	/**
	 * What happens when two players interact.
	 */
	public void yesInteraction(Creature creature) {
		// TODO
		this.performActionOn(creature);
		// possibly destroy item?
	}
	
	/**
	 * Sets up method that will do some action to some creature.
	 * @param creature
	 */
	public abstract void performActionOn(Creature creature);
}