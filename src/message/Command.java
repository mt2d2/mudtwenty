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
	ECHO((byte) 0), EXIT((byte) 1), LOOK((byte) 2), OOC((byte) 3), UNKNOWN((byte) 4);

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

	/**
	 * This is the byte associated with the enum.
	 */
	private byte							code;

	/**
	 * Constructs the enum, mapping a byte to it.
	 * 
	 * @param code
	 *            an enums associated byte
	 */
	private Command(byte code)
	{
		this.code = code;
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
}
