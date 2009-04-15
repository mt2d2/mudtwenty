package base;

import java.util.LinkedList;
import java.util.List;

/**
 * Keeps track of all the components in one room.
 */
public class Room {
	private String name;
	private List<Entity> contents;
	
	/**
	 * Default constructor takes no arguments.
	 */
	public Room() {
		this.contents = new LinkedList<Entity>();
	}
	
	/**
	 * Overload constructor sets the name of the room.
	 */
	public Room(String name) {
		this.name = name;
		this.contents = new LinkedList<Entity>();
	}

	/**
	 * Adds some entity to the room.
	 * @param toAdd
	 */
	public void addEntity(Entity toAdd) {
		this.contents.add(toAdd);
		
	}

	/**
	 * Removes some entity from the room.
	 * @param toRemove
	 */
	public void removeEntity(Entity toRemove) {
		this.contents.remove(toRemove);
	}
	
	/**
	 * Returns a list of all the items in the room.
	 * @return
	 */
	public List<Item> listItems() {
		List<Item> items = new LinkedList<Item>();
		
		for (Entity item : this.contents)
			if (item instanceof Item)
				items.add((Item) item);
		
		return items;
	}
	
	/**
	 * Returns a list of all the exits in the room.
	 * @return
	 */
	public List<Exit> listExits() {
		List<Exit> exits = new LinkedList<Exit>();
		
		for (Entity exit : this.contents)
			if (exit instanceof Exit)
				exits.add((Exit) exit);
		
		return exits;
	}
	
	/**
	 * Returns a list of all the human players in the room.
	 * @return
	 */
	public List<Player> listPlayers() {
		List<Player> players = new LinkedList<Player>();
		
		for (Entity player : this.contents)
			if (player instanceof Player)
				players.add((Player) player);
		
		return players;
	}
	
	/**
	 * Returns a list of all the Mobs in the room.
	 * @return
	 */
	public List<Mob> listMobs() {
		List<Mob> mobs = new LinkedList<Mob>();
		
		for (Entity mob : this.contents)
			if (mob instanceof Mob)
				mobs.add((Mob) mob);
		
		return mobs;
	}
	
	public String getName() {return this.name;}
}
