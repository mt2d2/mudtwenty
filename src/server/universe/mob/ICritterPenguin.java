package server.universe.mob;

import server.Server;

public class ICritterPenguin extends ICritter
{

	private static final long serialVersionUID = 2L;
	private String description = "It has large, bulging eyes and a sharp beak. Its interests include mating and receiving FancyTreats.";

	/**
	 * Create a new ICritterPenguin with the given name.
	 */
	public ICritterPenguin(String name)
	{
		super(name);
		this.setDescription(this.description);
	}

	/**
	 * Create a new ICritterPenguin with the default name.
	 */
	public ICritterPenguin()
	{
		super("ICritterPenguin");
		this.setDescription(this.description);
	}

}

