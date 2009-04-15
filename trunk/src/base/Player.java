package base;

/**
 * Human playable characters.
 */
public class Player extends Creature {
	/**
	 * Super constructor.
	 * @param name
	 */
	public Player(String name) {
		super(name);
	}

	/**
	 * What happens when a Player dies.
	 */
	public void die() {
		// TODO Auto-generated method stub
	}

	/**
	 * Should never be called, as players can interact
	 * with all other players.
	 */
	public void noInteraction(Creature creature) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * What happens when two players interact.
	 */
	public void yesInteraction(Creature creature) {
		// TODO
		// trade? chat? kill?!?
		
	}

	/**
	 * Returns a string of all the player's stats.
	 */
	@Override
	public String toString() {
		// TODO
		// print out player's stats
		return	"Name: " + this.getName() +
				", Level: " + this.getLevel() +
				", Health: " + this.currentHealth + "/" + this.totalHealth;
	}
}