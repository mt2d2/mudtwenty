package server.universe.item;

/**
 * Extends Armor to provide protection to a wearer. SteelMesh is stronger
 * than cloth.
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
		setName("steel mesh");
		setDescription("A tough metal mesh that protects you when worn.");
		setDefensePoints(15);
		setPrice(15);
	}
}
