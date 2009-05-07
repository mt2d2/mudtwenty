package server.universe.item;

import message.ClientMessage;
import server.Server;
import server.universe.Creature;
import server.universe.Room;

/**
 * Keys are special items that can unlock rooms.
 * 
 * Note that currently, keys unlock a room from any exit, not a particular exit.
 */
public class Key extends Item
{

	private static final long serialVersionUID = 2L;
	
	/**
	 * Note: this is the room that they key unlocks, not the room that the key is in.
	 */
	private Room room;
	
	/**
	 * Construct a key with the given name that applies to the given room.
	 * Each key should probably have a unique name.
	 * 
	 * @param name The name of the key.
	 * @param room The room that the key unlocks (not necessarily the room that the key is in!)
	 */
	public Key(String name, Room room)
	{
		setName(name);
		this.room = room;
		setDescription("It's a key. Presumably, it unlocks some exit when you use it.");
	}

	/**
	 * Unlock the room if the room that the key can be used on is adjacent. Otherwise, report that the key doesn't work.
	 */
	public ClientMessage use(Creature creature)
	{
		Room roomOfCreature = Server.getUniverse().getRoomOfCreature(creature);
		
		if (roomOfCreature.isAdjacentTo(room))
		{
			room.unlockExit();
			return new ClientMessage("The room, " + room.getName() + ", has been unlocked!", Server.SYSTEM_TEXT_COLOR);
		}
		else
		{
			return new ClientMessage("The key does not work for opening any adjacent rooms!", Server.ERROR_TEXT_COLOR);
		}
	}
}
