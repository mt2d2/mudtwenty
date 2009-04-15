package base;

/**
 * Keeps track of exits, which are portals to future rooms.
 */
public class Exit extends Entity {
	// Note that this is the room the exit leads to
	// not the room it is located in
	private Room nextRoom;
	
	/**
	 * Default constructor.
	 * @param name
	 * @param nextRoom
	 */
	public Exit(String name, Room nextRoom, int level) {
		super(name, level);
		this.nextRoom = nextRoom;
	}

	/**
	 * What happens when the creature's level is not high enough to go through.
	 */
	public void noInteraction(Creature creature) {
		// TODO
		// tell user sorry, but level not high enough
	}

	/**
	 * What happens when creature's level is indeed high enough.
	 */
	public void yesInteraction(Creature creature) {
		// TODO
		// add in-between steps to confirm or deny the movement
		creature.moveRoom(this.nextRoom);
	}

	@Override
	public String toString() {
		return	this.getName() +
				" leads to " + this.nextRoom.toString() +
				" and requires a level of " + this.getLevel() +
				" to pass.";
	}
}
