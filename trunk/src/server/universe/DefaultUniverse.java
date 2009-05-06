package server.universe;

import server.universe.item.CheapTreat;
import server.universe.item.FancyTreat;
import server.universe.item.LargePotion;
import server.universe.item.SmallPotion;
import server.universe.mob.Kitten;
import server.universe.mob.RoomGiftMob;
import server.universe.mob.RoomThiefMob;

/**
 * A simple default universe that can be loaded for a demo, or when the universe
 * file isn't there. This condition is the exception. In a normal case, the
 * universe is loaded from a file.
 */
public class DefaultUniverse extends Universe
{
	private static final long	serialVersionUID	= 1L;

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
// 		// use the node maker to add on to the east
// 		Room appendRoom = rootRoom;
// 		for (int i = 0; i < 9; i++)
// 		{
// 			Room node = this.makeRoomNode(i);
// 			appendRoom.addRoom(Direction.EAST, node);
// 			appendRoom = node;
// 		}

		Room rootRoom = new Room("Start room", "You are at the entrance to a grand castlem, to the north.\n"
			+ "There is a dark forest to the west and a friendly village to the south.", false);

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
		for (int i = 0; i < 10; i++)
		{
			northNode.addRoom(Direction.EAST, RandomRoomFactory.getRandomRoom(RoomType.CASTLE));
			northNode.addRoom(Direction.WEST, RandomRoomFactory.getRandomRoom(RoomType.CASTLE));
			Room nextNorth = RandomRoomFactory.getRandomRoom(RoomType.CASTLE);
			northNode.addRoom(Direction.NORTH, nextNorth);
			northNode = nextNorth;
		}
	}

	/**
	 * Add the village section of the default universe to the west of the start room.
	 */
	private void addVillage(Room startRoom)
	{
	}

	/**
	 * Add the forest section of the default universe to the south of the start room.
	 */
	private void addForest(Room startRoom)
	{
	}

// 	private Room makeRoomNode(int num)
// 	{
// 		Room rootRoom = new Room("Node root room: " + num, "the " + num + " node root", false);
//
// 		// west is already taken
// 		Room northRoom = new Room("North Room part of node " + num, "A boring, empty place in the north.", false);
// 		Room southRoom = new Room("South Room part of node " + num, "A boring, empty place in the south.", false);
// 		rootRoom.addRoom(Direction.NORTH, northRoom);
// 		rootRoom.addRoom(Direction.SOUTH, southRoom);
//
// 		return rootRoom;
// 	}
}
