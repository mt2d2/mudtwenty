package server.universe.mob;

/**
 * The familiar ICritterCat, in a new world.
 */
public class ICritterCat extends ICritter
{
	private static final long serialVersionUID = 2L;

	/**
	 * Create a new ICritterCat with the given name.
	 */
	public ICritterCat(String name)
	{
		super(name);
		this.setDescription("That's not a real cat! It's a monster!");
	}

}

