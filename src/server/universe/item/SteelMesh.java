package server.universe.item;

/**
 * Extends Armor to provide protection to a wearer. SteelMesh is stronger, and
 * has a defense and cost value of 10.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class SteelMesh extends Armor
{
	private static final long	serialVersionUID	= 1L;

	/**
	 * Sole constructor.
	 */
	public SteelMesh()
	{
		setName("SteelMesh");
		setDescription("A tough metal mesh that protects you when worn.");
		setDefensePoints(10);
		setPrice(10);
	}
}
