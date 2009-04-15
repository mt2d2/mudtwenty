package base;

/**
 * All variable objects to be stored as entities.
 */
public abstract class Entity {
	private String name;
	private Room room;
	protected int level;
	
	/**
	 * Default constructor sets name and description.
	 * @param name
	 * @param description
	 */
	public Entity(String name) {
		this.name = name;
		this.level = 1;
	}
	
	public Entity(String name, int level) {
		this.name = name;
		this.level = level;
	}

	/**
	 * Removes Entity from current room and places it in another.
	 * @param room
	 */
	public void moveRoom(Room nextRoom) {
		if (this.room != null) {
			this.room.removeEntity(this);
		}
		this.setRoom(nextRoom);
		this.room.addEntity(this);
	}

	/**
	 * Increase level by 1.
	 */
	public void increaseLevel() {
		this.level++;
	}
	
	/**
	 * Sets level
	 * @param level
	 */
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * Sets room entity belongs to.
	 * @param room
	 */
	public void setRoom(Room room) {
		this.room = room;
	}
	
	/**
	 * Determines whether the creatures level is high enough to interact
	 * with this entity, pointing it to the right method to follow.
	 * Automatically interacts with other players.
	 * @param creature
	 */
	public boolean Interact(Creature creature) {
		// automatically interact with other players if called on
		if (this instanceof Player) {
			this.yesInteraction(creature);
			return true;
		}
		
		if (creature.getLevel() >= this.level) {
			this.yesInteraction(creature);
			return true;
		}
		else {
			this.noInteraction(creature);
			return false;
		}
	}
	
	public abstract void yesInteraction(Creature creature);
	public abstract void noInteraction(Creature creature);
	
	/**
	 * Default toString() that prints out a description of the entity.
	 */
	public abstract String toString();
	
	public String getName() {return this.name;}
	public Room getRoom() {return this.room;}
	public int getLevel() {return this.level;}
}