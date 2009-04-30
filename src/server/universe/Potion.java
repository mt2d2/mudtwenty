package server.universe;

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
}
