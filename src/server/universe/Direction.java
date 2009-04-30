package server.universe;

/**
 * A Direction is some possible direction that an exit can be pointing.
 */
public enum Direction
{
	NORTH, EAST, SOUTH, WEST;

	/**
	 * Find the direction opposite to this one.
	 */
	public Direction opposite()
	{
		switch (this)
		{
			case NORTH: return SOUTH;
			case SOUTH: return NORTH;
			case WEST: return EAST;
			case EAST: return WEST;
		}
		// this should never be reached:
		// throw new WtfException()...
		return null;
	}
}