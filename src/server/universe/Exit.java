package server.universe;

/**
 * An Exit is a way to get from one Room to another.
 */
public class Exit implements Entity
{
	private String		name;
	private String		description;
	private Direction	direction;
	private Room		room;

	/**
	 * Create a new Exit with the given attributes.
	 * 
	 * @param name user-selectable name of the exit
	 * @param description description of the exit, i.e., where it goes
	 * @param direction the direction the exit is situated in the room
	 */
	public Exit(String name, String description, Direction direction)
	{
		this.name = name;
		this.description = description;
		this.direction = direction;
	}

	/**
	 * The description of the exit.
	 */
	public String getDescription()
	{
		return this.name + ": " + this.description;
	}

	/**
	 * The description of the exit.
	 */
	public Direction getDirection()
	{
		return direction;
	}

	/**
	 * @return Room this exit connects to
	 */
	public Room getRoom()
	{
		return this.room;
	}

	@Override
	public String getName()
	{
		return this.name;
	}
}