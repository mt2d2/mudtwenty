package server.universe.item;

/**
 * This is an extension of Weapon that provides properties that a Cannon should
 * have, i.e., it's the best.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Cannon extends Weapon
{
	private static final long	serialVersionUID	= 1L;

	public Cannon()
	{
		setName("Cannon");
		setDescription("An instrument of death, which has 50 damage points");
		setPrice(50);
		this.setDamage(50);
	}
}
