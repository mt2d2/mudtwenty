package server.universe.item;

/**
 * This is an extension of Weapon that provides properties that a Sword should
 * have, i.e., more expensive and a better weapon.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Sword extends Weapon
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Sole constructor for spear, sets its price and damage at 30.
	 */
	public Sword()
	{
		setName("Sword");
		setDescription("An instrument of death, which has 30 damage points");
		setPrice(30);
		this.setDamage(30);
	}
}
