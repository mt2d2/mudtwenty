package server.universe;

/**
 * The Skill class represents a skill. A skill can be checked, and some
 * Interaction can fail or succeed based on this. Usually, but not necessarily,
 * when a skill is checked it is practiced.
 */
public class Skill
{

	private String	name;
	private int		value;

	/**
	 * Create a new skill with the given attributes.
	 */
	public Skill(String name, int initValue)
	{
		this.name = name;
		this.value = initValue;
	}

	/**
	 * Return the name of the skill.
	 *
	 * @return The name of the skill.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Return the value of the skill.
	 *
	 * @return int The current value of the skill.
	 */
	public int getValue()
	{
		return value;
	}

	/**
	 * Practice the skill. This has a chance of increasing the value of the
	 * skill. The result of practicing a skill should depend on the current
	 * skill level and it should have a chance of increasing the skill.
	 */
	public void practice()
	{
	}

}