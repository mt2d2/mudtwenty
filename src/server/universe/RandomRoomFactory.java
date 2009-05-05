package server.universe;

import java.util.List;

import server.universe.RoomType;
import server.universe.Room;
import server.universe.item.Item;
import server.universe.mob.MOB;


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
		return null;
	}

	/**
	 * Get the factory for the given room type.
	 */
	private static RoomFactory getFactory(RoomType type)
	{
		return null;
	}

	private interface RoomFactory
	{
		public String makeName();
		public String makeDescription();
		public List<Item> makeItems();
		public List<MOB> makeMOBs();
	}

}
