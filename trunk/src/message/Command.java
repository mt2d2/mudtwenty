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
	QUIT((byte) 1, "saves your state and exits the mud"),
	SCORE((byte) 2, "gives detailed information about your player"),
	OOC((byte) 3, "send a message to all users connected to the mud"),
	COMMANDS((byte) 4, "lists all available commands and general system help"),	
	WHO((byte) 5, "lists all users online, both users and guests"),
	LOGIN((byte) 6, "logs a user in, e.g., login <username> <password>"),
	REGISTER((byte) 7, "registers a user, e.g., register <username> <password"),
	TELL((byte) 8, "sends a private message to a user, e.g., say <username> <message>"),
	SAY((byte) 9, "sends a message to everyone in the same room, e.g., say <message>"),
	LOOK((byte) 10, "looks around at the current room you occupy, giving your information about yoru surroundings"),
	INVENTORY((byte) 11, "lists all the items you are currently carrying"),
	DROP((byte) 12, "drops a specified item from your inventory, e.g., drop <item name>"),
	USE((byte) 13, "use a specified item from your inventory, e.g., use <item name>"),
	MOVE((byte) 14, "moves you to through your specified exit, e.g., move <exit name>"),
	SHUTDOWN((byte) 15, "saves the universe and player state, alerts all participants, and shuts down the server; you must be granted access to this function"),
	GIVE((byte) 16, "gives an item to a user, e.g., give <player name> <item name>"),
	GET((byte) 17, "gets an item that exists in the room, e.g., get <name of item in room>"),
	UNKNOWN((byte) 18, "all unrecognized commands are unknown, and you'll be told so"),

	// aliases
	CD((byte) 19, "alias for move"),
	NORTH((byte) 20, "alias for move north"),
	SOUTH((byte) 21, "alias for move south"),
	EAST((byte) 22, "alias for move east"),
	WEST((byte) 23, "alias for move west"),
	HELP((byte) 24, "alias for commands"),
	L((byte) 25, "alias for look"),
	EXIT((byte) 26, "alias for quit");
	
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
