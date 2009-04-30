package server.universe;

import java.io.Serializable;

/**
 * An Exit is a way to get from one Room to another.
 */
public class Exit implements Serializable
{
	private static final long	serialVersionUID	= 1L;

	private String		name;
	private String		blurb;
	private Room		room;

	/**
	 * Create a new Exit with the given attributes.
	 *
	 * @param room
	 *            the room that this exit leads to
	 * @param blurb
	 *            description of the exit; where it goes or what it looks like
	 */
	public Exit(Room room, String blurb)
	{
		this.blurb = blurb;
		this.room = room;
	}

	/**
	 * Create a new Exit with a default blurb.
	 */
	public Exit(Room room)
	{
		this.blurb = "a nondescript passageway to another place";
		this.room = room;
	}

	/**
	 * Get description of the exit. This will include everything that is returned when the exit is looked at.
	 */
	public String getDescription()
	{
		return this.blurb;
	}

	/**
	 * Set the blurb of the exit. This will be the appearance of the exit.
	 */
	public void setBlurb(String s)
	{
		this.blurb = s;
	}

	/**
	 * Get the room that this exit leads to.
	 *
	 * @return Room this exit connects to
	 */
	public Room getRoom()
	{
		return this.room;
	}

}