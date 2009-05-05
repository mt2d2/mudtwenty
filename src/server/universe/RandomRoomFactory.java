package server.universe;

import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.universe.item.Item;
import server.universe.mob.MOB;
import server.universe.RoomType;

/**
 * This class is a builder that uses an abstract factory. It makes random rooms of the specified type.
 */
public class RandomRoomFactory {

	private static final long serialVersionUID = 2L;

	/**
	 * Get a random room of the given type.
	 */
	public static Room getRandomRoom(RoomType type)
	{
		RoomFactory factory = getFactory(type);
		String name = factory.makeName();
		String description = factory.makeDescription();
		Room room = new Room(name, description, false);
		for (Item item : factory.makeItems())
			room.addItem(item);
		for (MOB mob : factory.makeMOBs())
			Server.getUniverse().spawnMob(mob, room);
		return room;
	}

	/**
	 * Get the factory for the given room type.
	 */
	private static RoomFactory getFactory(RoomType type)
	{
		switch (type)
		{
		case FOREST: return new ForestRoomFactory();
		default: return new ForestRoomFactory();
		}
	}

	/**
	 * This is the abstract factory.
	 */
	private static interface RoomFactory
	{
		public String makeName();
		public String makeDescription();
		public List<Item> makeItems();
		public List<MOB> makeMOBs();
	}
	
	/**
	 * This is a concrete factory subtype to the abstract factory.
	 */
	private static class ForestRoomFactory implements RoomFactory
	{
		
		public ForestRoomFactory()
		{
		}
		
		public String makeName() {
			return "Forest";
		}
		
		public String makeDescription() {
			return "This part of the forest looks just like every other part of the forest.";
		}

		public List<Item> makeItems() {
			List<Item> items = new ArrayList<Item>();
			return items;
		}

		public List<MOB> makeMOBs() {
			List<MOB> mobs = new ArrayList<MOB>();
			return mobs;
		}

	}

}
