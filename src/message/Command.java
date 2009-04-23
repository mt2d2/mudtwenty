package message;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * An enum representing acceptable commands that clients can send.
 * User input must match one of these options;
 * all recognized input is directed to UNKNOWN.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public enum Command
{
	ECHO((byte) 0, "echos what you say, back to you"),
	EXIT((byte) 1, "saves your state and exits the mud"),
	LOOK((byte) 2, "sends a message to all users connected to the server, e.g., ooc <message>"),
	OOC((byte) 3, "send a message to all users connected to the mud"),
	HELP((byte) 4, "lists all available commands and general system help"),	
	WHO((byte) 5, "lists all users online, both users and guests"),
	LOGIN((byte) 6, "logs a user in, e.g., login <username> <password>"),
	REGISTER((byte) 7, "registers a user, e.g., register <username> <password"),
	TELL((byte) 8, "sends a private message to a user, e.g., say <username> <message>"),
	UNKNOWN((byte) 9, "all unrecognized commands are unknown, and you'll be told so");

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
	 * @return brief, user-visible description of the command enum.
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * @return The byte associated with this enum.
	 */
	public byte getCode()
	{
		return this.code;
	}

	/**
	 * @param code
	 *            The byte associated with an enum
	 * @return The associated enum member associated with code.
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

	// Put each enum number into the lookup map.
	static
	{
		for (Command s : EnumSet.allOf(Command.class))
			lookup.put(s.getCode(), s);
	}
}
