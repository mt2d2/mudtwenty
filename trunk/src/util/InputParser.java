package util;

import java.util.ArrayList;
import java.util.List;

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
		
		List<String> arguments = new ArrayList<String>();
		String[] args = ArrayUtil.removeElement(words, 0);
		
		if (args.length > 0)
		{
			for (String arg : args)
				arguments.add(arg);
		}
		
		return new ServerMessage(command, arguments);
	}
}
