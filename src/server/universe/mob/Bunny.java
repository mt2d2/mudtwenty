package server.universe.mob;

/**
 * A bunny is a small, peaceful animal. It does very little but hop around.
 */
public class Bunny extends MOB
{

	private static final long serialVersionUID = 2L;

	/**
	 * Make a bunny with the given name.
	 */
	public Bunny(String name)
	{
		super(name);
		this.setMaxHealth(1);
		this.setDescription("This is a cute, little, white, fluffy bunny. It looks hungry.");
	}

	/**
	 * Move sometimes. Other times, do nothing.
	 */
	public void takeTurn()
	{
		if (Math.random() > 0.25)
			setBehavior(new MoveBehavior());
		else
			setBehavior(new NullBehavior());
	}
}
