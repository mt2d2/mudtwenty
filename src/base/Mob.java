package base;

/**
 * Mob NPC that interacts with human players.
 */
public class Mob extends Creature {
	/**
	 * Super constructor.
	 * @param name
	 * @param description
	 */
	public Mob(String name) {
		super(name);
	}

	/**
	 * What happens when a Mob dies.
	 */
	public void die() {
		// TODO Auto-generated method stub
	}

	@Override
	public void noInteraction(Creature creature) {
		// TODO
		// let user know he is not a high enough level to interact
	}

	@Override
	public void yesInteraction(Creature creature) {
		// TODO
		// start fighting? chatting? trading?
	}

	@Override
	public String toString() {
		// TODO
		return "this is a level 32 tigress, maybe";
	}
}
