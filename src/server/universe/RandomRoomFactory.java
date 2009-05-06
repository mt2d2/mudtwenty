package server.universe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import server.Server;
import server.universe.item.Hydes;
import server.universe.item.Item;
import server.universe.item.Spear;
import server.universe.mob.Bunny;
import server.universe.mob.Deer;
import server.universe.mob.MOB;
import server.universe.mob.RoomThiefMob;

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
		
		Random random = new Random();
		
		public String makeName() {
			return "Forest";
		}
		
		public String makeDescription() {
			return "This part of the forest looks just like every other part of the forest.";
		}

		public List<Item> makeItems() {
			List<Item> possibleItems = Arrays.asList(new Hydes(), new Spear());
			List<Item> items = new ArrayList<Item>();
			int numItems = random.nextInt(3);
			for (int i = 0; i < numItems; i++)
			{
				int indexOfChosenItem = random.nextInt(possibleItems.size());
				items.add(possibleItems.get(indexOfChosenItem).clone());
			}
			return items;
		}

		public List<MOB> makeMOBs() {
			List<MOB> possibleMobs = Arrays.asList(new Deer("bambi"), new Bunny("flufftail"),
					new RoomThiefMob("bandit"));
			List<MOB> mobs = new ArrayList<MOB>();
			int numMobs = random.nextInt(1);
			for (int i = 0; i < numMobs; i++)
			{
				int indexOfChosenMob = random.nextInt(possibleMobs.size());
				mobs.add(possibleMobs.get(indexOfChosenMob).clone());
			}
			return mobs;
		}

	}

}
