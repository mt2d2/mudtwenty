package server.universe.item;

import message.ClientMessage;
import server.universe.Player;

/**
 * Armor can be equipped to creatures.
 * 
 * @author Simon
 * 
 */
public class Armor implements Item
{
	private static final long	serialVersionUID	= 1L;

	@Override
	public int getPrice()
	{
		return 5;
	}

	@Override
	public String getDescription()
	{
		return "Protect yourself from enemy attacks";
	}

	@Override
	public String getName()
	{
		return "leather";
	}

	@Override
	public ClientMessage use(Player player)
	{
		player.equip(this);
		return new ClientMessage("You equipped " + this.getName());
	}
}
