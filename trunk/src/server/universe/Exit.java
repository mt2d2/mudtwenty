package server.universe;

/**
 * An Exit is a way to get from one Room to another.
 */
public class Exit
{
	private String description;
	private Direction direction;

	/**
	 * Create a new Exit with the given attributes.
	 */
	public Exit(String description, Direction direction) {
		this.description = description;
		this.direction = direction;
	}

	/**
	 * The description of the exit.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * The description of the exit.
	 */
	public Direction getDirection() {
		return direction;
	}
}