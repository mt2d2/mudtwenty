package server.universe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import server.Server;
import server.universe.item.Cannon;
import server.universe.item.CheapTreat;
import server.universe.item.Cloth;
import server.universe.item.FancyTreat;
import server.universe.item.Hydes;
import server.universe.item.Item;
import server.universe.item.Spear;
import server.universe.item.SteelMesh;
import server.universe.item.Sword;
import server.universe.mob.MOB;

/**
 * This class is a builder that uses an abstract factory. It makes random rooms
 * of the specified type.
 */
public class RandomRoomFactory
{

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
		// Note. Server.getUniverse() returns the current universe,
		// so adding mobs will only work properly if the universe
		// that the room is being generated for is going to exist
		// in the current universe. This could be changed.
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
		case FOREST:
			return new ForestRoomFactory();
		case CASTLE:
			return new CastleRoomFactory();
		case VILLAGE:
			return new VillageRoomFactory();
		default:
			return new ForestRoomFactory();
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
	 * This is a concrete factory for making forest rooms.
	 */
	private static class ForestRoomFactory implements RoomFactory
	{

		private Random random = new Random();

		private static int count = 0;
		
		public String makeName()
		{
			count++;
			return "Random Forest Room " + count;
		}

		public String makeDescription()
		{
			return "This part of the forest looks just like every other part of the forest.";
		}

		public List<Item> makeItems() {
			List<Item> possibleItems = Arrays.asList(new Hydes(), new Spear());
			List<Item> items = new ArrayList<Item>();
			int numItems = random.nextInt(3);
			for (int i = 0; i < numItems; i++) {
				int indexOfChosenItem = random.nextInt(possibleItems.size());
				items.add(possibleItems.get(indexOfChosenItem).clone());
			}
			return items;
		}

		public List<MOB> makeMOBs()
		{
			return new ArrayList<MOB>();
		}
	}

	/**
	 * This is a concrete factory for making village rooms.
	 */
	private static class VillageRoomFactory implements RoomFactory
	{

		private Random random = new Random();

		private static int count = 0;
		
		public String makeName()
		{
			count++;
			return "Random Village Room " + count;
		}

		public String makeDescription()
		{
			return "There are many small houses and shops around here. What a nice, cozy town.\n"
					+ "There are no distinguishing landmarks, however. It is easy to get lost.";
		}

		public List<Item> makeItems()
		{
			List<Item> possibleItems = Arrays.asList(new Hydes(), new Spear());
			List<Item> items = new ArrayList<Item>();
			int numItems = random.nextInt(3);
			for (int i = 0; i < numItems; i++) {
				int indexOfChosenItem = random.nextInt(possibleItems.size());
				items.add(possibleItems.get(indexOfChosenItem).clone());
			}
			return items;
		}

		public List<MOB> makeMOBs()
		{
			return new ArrayList<MOB>();
		}
	}

	/**
	 * This is a concrete factory for making castle rooms.
	 */
	private static class CastleRoomFactory implements RoomFactory
	{
		private Random random = new Random();

		private static int count = 0;
		
		public String makeName()
		{
			count++;
			return "Random Castle Room " + count;
		}

		public String makeDescription()
		{
			return "There are grand statues and paintings on the wall.\n"
					+ "Yet, there are no guards. It is a very spooky place.";
		}

		public List<Item> makeItems()
		{
			List<Item> possibleItems = Arrays.asList(new Cloth(), new Sword(),
					new SteelMesh(), new Cannon(), new CheapTreat(),
					new FancyTreat());
			List<Item> items = new ArrayList<Item>();
			int numItems = random.nextInt(4);
			for (int i = 0; i < numItems; i++)
			{
				int indexOfChosenItem = random.nextInt(possibleItems.size());
				items.add(possibleItems.get(indexOfChosenItem).clone());
			}
			return items;
		}

		public List<MOB> makeMOBs() {
			return new ArrayList<MOB>();
		}
	}

}
