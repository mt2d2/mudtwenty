package server.universe.item;

/**
 * Extends Armor to provide protection to a wearer. Cloth is weaker, and
 * has a defense and cost value of 5.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Cloth extends Armor
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Sole constructor.
	 */
	public Cloth()
	{
		setName("cloth");
		setDescription("A light fabric that will protect you in battle.");
		setDefensePoints(5);
		setPrice(5);
	}
}
