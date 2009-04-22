package util;

import java.util.Arrays;

import message.Command;
import message.ServerMessage;

/**
 * A collection of static methods that can be used to parse a user's input
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class InputParser
{
	/**
	 * Parses the input given by a user. Currently, the first word is tested as
	 * a viable Command, the reset of the words are passed as arguments in the
	 * message.
	 * 
	 * @param input
	 *            user-provided input (the string sent from the client)
	 * @return message which can be sent to the server.
	 */
	public static final ServerMessage parse(String input)
	{
		String[] words = input.split(" ");
		Command command = null;

		try
		{
			command = Command.valueOf(words[0].toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			command = Command.UNKNOWN;
		}

		// handle passwords differently, they need to be hashed
		if (command == Command.LOGIN || command == Command.REGISTER)
		{
			// remove the first string, that's the command
			String[] args = ArrayUtil.removeElement(words, 0);

			// passwords are the second (after username) argument, hash them
			args[1] = Hasher.getDigest(args[1]);

			return new ServerMessage(command, Arrays.asList(args));
		}
		else
		{
			return new ServerMessage(command, Arrays.asList(ArrayUtil.removeElement(words, 0)));
		}
	}
}
