package server.universe.mob;

/**
 * Deers are similar to kittens and bunnies: not very interesting.
 */
public class Deer extends MOB
{

	private static final long serialVersionUID = 2L;

	/**
	 * Make a deer with the given name. 
	 */
	public Deer(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("A great, noble animal of the forest.");
	}

	/**
	 * Move sometimes, else do nothing.
	 */
	public void takeTurn()
	{
		if (Math.random() > 0.25)
			setBehavior(new MoveBehavior());
		else
			setBehavior(new NullBehavior());
	}
}
