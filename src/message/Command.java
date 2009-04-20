package message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple list of acceptable commands in the MUD. User input must match one of
 * these options; all recognized input is directed to UNKNOWN.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public enum Command
{
	/**
	 * 
	 */
	ECHO((byte) 0, "echos what you say, back to you"),
	/**
	 * 
	 */
	EXIT((byte) 1, "saves your state and exits the mud"),
	/**
	 * 
	 */
	LOOK((byte) 2, "allows you to view the world around you"),
	/**
	 * 
	 */
	OOC((byte) 3, "send a message to all users connected to the mud"),
	/**
	 * 
	 */
	HELP((byte) 4, "lists all available commands and general system help"),
	/**
	 * 
	 */
	UNKNOWN((byte) 5, "all unrecognized commands are unknown, and you'll be told so");

	// --------------------------------------------------

	private byte	code;
	private String	description;

	/**
	 * Constructs the enum, mapping a byte to it.
	 * 
	 * @param code
	 *            an enums associated byte
	 */
	private Command(byte code, String description)
	{
		this.code = code;
		this.description = description;
	}

	/**
	 * @return brief, user-visible description of the command enum
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return the byte associated with an enum
	 */
	public byte getCode()
	{
		return this.code;
	}

	/**
	 * @param code
	 *            byte associated with an enum
	 * @return associated enum member associated with code
	 */
	public static Command get(byte code)
	{
		return lookup.get(code);
	}

	/**
	 * Holds the mapping between a enum in Command and its associated byte.
	 * Although this incurs a bit more work on setup, this is used to reduce the
	 * size of network packets.
	 */
	private static final Map<Byte, Command>	lookup	= new HashMap<Byte, Command>();

	// give each enum a mapping
	static
	{
		for (Command s : EnumSet.allOf(Command.class))
			lookup.put(s.getCode(), s);
	}
}
