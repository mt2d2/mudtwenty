package server.universe.item;

/**
 * This is an extension of Weapon that provides properties that a Spear should
 * have, i.e., cheap and not that good.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Spear extends Weapon
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Sole constructor for spear, sets its price and damage at 10.
	 */
	public Spear()
	{
		setName("Spear");
		setDescription("An instrument of death, which has 10 damage points");
		setPrice(10);
		this.setDamage(10);
	}
}
