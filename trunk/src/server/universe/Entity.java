package server.universe;

/**
 * Entity represents something in the Universe.
 * All entities have a name and a description.
 * Thus, all entities can be looked at and such.
 */
public interface Entity {

	/**
	 * Return the name of the entity. Names are not necessarily unique.
	 * Name should be short (one word no spaces?). They should be singular nouns.
	 *
	 * @return the name of the entity.
	 */
	public String getName();

	/**
	 * Return a description of the entity. This should be a string (about 1-4 sentences?)
	 * describing what the thing is and what it looks like.
	 *
	 * @return the name of the entity.
	 */
	public String getDescription();

}