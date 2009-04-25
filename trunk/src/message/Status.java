package message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The status of a given message is reflected with this enum.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public enum Status
{
	/**
	 * Indicates all is well for the message sender.
	 */
	OK((byte) 0),
	
	/**
	 * Indicates that this message is bound for the chat portion of the GUI
	 */
	CHAT((byte) 1);

	/**
	 * Holds the mapping between a enum in Status and its associated byte.
	 * Although this incurs a bit more work on setup, this is used to reduce the
	 * size of network packets.
	 */
	private static final Map<Byte, Status>	lookup	= new HashMap<Byte, Status>();

	static
	{
		for (Status s : EnumSet.allOf(Status.class))
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
	private Status(byte code)
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
	public static Status get(byte code)
	{
		return lookup.get(code);
	}
}
