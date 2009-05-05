package server.universe.item;

/**
 * Large potion extends Potion, increasing its price and healing power.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class LargePotion extends Potion
{
	private static final long	serialVersionUID	= 1L;

	public LargePotion()
	{
		setName("large potion");
		setDescription("Use potions to recover health points");
		setPrice(20);
		this.setHealingPower(20);
	}
}
