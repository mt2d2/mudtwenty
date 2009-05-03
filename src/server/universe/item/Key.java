package server.universe.item;

import message.ClientMessage;
import server.universe.Player;
import server.universe.Room;

public class Key implements Item
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Room	room;
	String	name;
	
	public Key(String name, Room room)
	{
		this.name = name;
		this.room = room;
	}
	
	@Override
	public int getPrice()
	{
		return 10;
	}

	@Override
	public ClientMessage use(Player player)
	{
		this.room.unlockExit();
		return new ClientMessage("The exit leading to " + this.room.getName() + " has been unlocked temporarily");
	}

	@Override
	public String getDescription()
	{
		return "Use this key to unlock " + this.room.getName();
	}

	@Override
	public String getName()
	{
		return name;
	}
}
