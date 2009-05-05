package server.universe.mob;

import java.util.List;

import server.Server;


public class ICritterCat extends ICritter
{

	private static final long serialVersionUID = 2L;
	private String description = "That's not a real cat! It's a monster!.";

	/**
	 * Create a new ICritterCat with the given name.
	 */
	public ICritterCat(String name)
	{
		super(name);
		this.setDescription(description);
	}

	/**
	 * Create a new ICritterCat with the default name.
	 */
	public ICritterCat()
	{
		super("ICritterCat");
		this.setDescription(description);
	}

}

