package server.universe.item;

/**
 * Hydes are a type of armor.
 */
public class Hydes extends Armor implements Cloneable {
	
	private static final long	serialVersionUID	= 3L;

	/**
	 * Sole constructor.
	 */
	public Hydes()
	{
		setName("hydes");
		setDescription("Animal hydes that will protect you in battle.");
		setDefensePoints(10);
		setPrice(10);
	}
	
}
