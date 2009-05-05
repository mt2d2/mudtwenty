package server.universe.item;

/**
 * Cheap treats increase a player's maximum health by 10 points.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class CheapTreat extends FancyTreat
{
	private static final long	serialVersionUID	= 1L;

	public CheapTreat()
	{
		setName("CheapTreat");
		setDescription("Use this to increase your maximum health by 10 points");
		setPrice(10);
		setIncreaseAmount(10);
	}
}
