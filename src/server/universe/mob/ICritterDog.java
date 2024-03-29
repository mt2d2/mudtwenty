package server.universe.mob;

/**
 * The familiar ICritterCat, in a new world.
 */
public class ICritterDog extends ICritter
{

	private static final long serialVersionUID = 2L;

	/**
	 * Create a new ICritterDog with the given name.
	 */
	public ICritterDog(String name)
	{
		super(name);
		this.setDescription("It has sharp claws, and it will mate with any other land animal!");
	}

}

