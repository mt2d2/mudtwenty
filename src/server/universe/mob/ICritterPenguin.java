package server.universe.mob;

/**
 * The familiar ICritterPenguin, in a new world.
 */
public class ICritterPenguin extends ICritter
{

	private static final long serialVersionUID = 2L;

	/**
	 * Create a new ICritterPenguin with the given name.
	 */
	public ICritterPenguin(String name)
	{
		super(name);
		this.setDescription("It has large, bulging eyes and a sharp beak." +
				"Its interests include mating and receiving FancyTreats.");
	}

}

