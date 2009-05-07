package server.universe;

import server.universe.item.Book;
import server.universe.item.Cannon;
import server.universe.item.CheapTreat;
import server.universe.item.Cloth;
import server.universe.item.FancyTreat;
import server.universe.item.Key;
import server.universe.item.LargePotion;
import server.universe.item.SmallPotion;
import server.universe.item.Spear;
import server.universe.item.SteelMesh;
import server.universe.item.Sword;
import server.universe.mob.Merchant;
import server.universe.mob.Troll;
import server.universe.mob.RoomGiftMob;
import server.universe.mob.RoomThiefMob;
import server.universe.mob.Kitten;
import server.universe.mob.Bunny;

/**
 * A simple default universe that can be loaded for a demo, or when the universe
 * file isn't there. This condition is the exception. In a normal case, the
 * universe is loaded from a file.
 */
public class DefaultUniverse extends Universe
{
	private static final long	serialVersionUID	= 1L;

	private Room secretRoom;

	/**
	 * Make a default universe with many rooms!
	 */
	public DefaultUniverse()
	{
		super();
		this.setStartRoom(this.spawnRooms());
	}

	private Room spawnRooms()
	{
		Room rootRoom = new Room("Start room", "You are at the entrance to a grand castle, to the north.\n"
			+ "There is a dark forest to the west and a friendly village to the south.", false);
		rootRoom.addItem(new Cannon());
		rootRoom.addItem(new CheapTreat());
		rootRoom.addItem(new Cloth());
		rootRoom.addItem(new FancyTreat());
		rootRoom.addItem(new LargePotion());
		rootRoom.addItem(new SmallPotion());
		rootRoom.addItem(new Spear());
		rootRoom.addItem(new SteelMesh());
		rootRoom.addItem(new Sword());

		// add a locked room
		secretRoom = new Room("Secret Room", "This is a secret room, which was locked from prying eyes.", true);
		rootRoom.addExit(Direction.EAST, new Exit(secretRoom, null, "A secret passageway..."));
		secretRoom.addExit(Direction.WEST, new Exit(rootRoom, null, "A nondescript passageway back to the start room."));
		rootRoom.addItem(new Key("gold key", secretRoom));

		addCastle(rootRoom);
		addVillage(rootRoom);
		addForest(rootRoom);

		return rootRoom;
	}

	/**
	 * Add the castle section of the default universe to the north of the start room.
	 */
	private void addCastle(Room startRoom)
	{
		Room entrance = new Room("Castle Entrance", "This is the entrance to the castle.", false);
		startRoom.addExit(Direction.NORTH, new Exit(entrance, null, "A large stairway to a forboding castle."));
		entrance.addExit(Direction.SOUTH, new Exit(startRoom, null, "A stairway leading out of the forboding castle."));

		// Add a bunch of random rooms
		Room northNode = entrance;
		for (int i = 0; i < 4; i++)
		{
			northNode.addRoom(Direction.EAST, RandomRoomFactory.getRandomRoom(RoomType.CASTLE));
			northNode.addRoom(Direction.WEST, RandomRoomFactory.getRandomRoom(RoomType.CASTLE));
			Room nextNorthNode = RandomRoomFactory.getRandomRoom(RoomType.CASTLE);
			northNode.addRoom(Direction.NORTH, nextNorthNode);
			northNode = nextNorthNode;
		}

		this.spawnMob(new RoomThiefMob("thief"), northNode);
		this.spawnMob(new RoomGiftMob("santa"), northNode);

	}

	/**
	 * Add the village section of the default universe to the west of the start room.
	 */
	private void addVillage(Room startRoom)
	{
		Room entrance = new Room("Village entrance", "This is the entrance to the village.", false);
		startRoom.addExit(Direction.WEST, new Exit(entrance, null, "A dirt pathway to a very strange village."));
		entrance.addExit(Direction.EAST, new Exit(startRoom, null, "The pathway to the castle entrance."));

		Merchant merch = new Merchant("vendor");
		merch.addItem(new Spear());
		merch.addItem(new SteelMesh());
		merch.addItem(new Book());
		merch.addItem(new Cannon());
		merch.addItem(new SmallPotion());
		merch.addItem(new Key("gold key", secretRoom));
		merch.addItem(new Key("rusty key", null));

		this.spawnMob(merch, entrance);

		// Add a bunch of random rooms
		Room westNode = entrance;
		for (int i = 0; i < 4; i++)
		{
			westNode.addRoom(Direction.NORTH, RandomRoomFactory.getRandomRoom(RoomType.VILLAGE));
			westNode.addRoom(Direction.SOUTH, RandomRoomFactory.getRandomRoom(RoomType.VILLAGE));
			Room nextWestNode = RandomRoomFactory.getRandomRoom(RoomType.VILLAGE);
			westNode.addRoom(Direction.WEST, nextWestNode);
			westNode = nextWestNode;
		}
	}

	/**
	 * Add the forest section of the default universe to the south of the start room.
	 */
	private void addForest(Room startRoom)
	{
		Room entrance = new Room("Forest entrance", "This is the entrance into the forest.", false);
		startRoom.addExit(Direction.SOUTH, new Exit(entrance, null, "A dark and forboding entrance to a dark and forboding forest."));
		entrance.addExit(Direction.NORTH, new Exit(startRoom, null, "The path out of the forest."));

		this.spawnMob(new Troll("gurk"), entrance);

		this.spawnMob(new Kitten("fluffy"), entrance);


		// Add a bunch of random rooms
		Room southNode = entrance;
		for (int i = 0; i < 4; i++)
		{
			southNode.addRoom(Direction.EAST, RandomRoomFactory.getRandomRoom(RoomType.FOREST));
			southNode.addRoom(Direction.WEST, RandomRoomFactory.getRandomRoom(RoomType.FOREST));
			Room nextSouthNode = RandomRoomFactory.getRandomRoom(RoomType.FOREST);
			southNode.addRoom(Direction.SOUTH, nextSouthNode);
			southNode = nextSouthNode;
		}

		this.spawnMob(new Bunny("cottontail"), southNode);

	}

}
