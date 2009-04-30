package server.universe.item;

import message.ClientMessage;
import server.universe.Player;


/**
 * Potion class outlines everything a potion does.
 */
public class Potion implements Item
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
		return "Use potions to recover health points";
	}

	@Override
	public String getName()
	{
		return "small potion";
	}

	public int getHealingPower()
	{
		return 10;
	}

	@Override
	public ClientMessage use(Player player)
	{
		player.increaseHealth(this.getHealingPower());
		player.removeItem(this);
		return new ClientMessage("You use " + this.getName() + " and heal " + ((Potion)this).getHealingPower() + " points");
	}
}
