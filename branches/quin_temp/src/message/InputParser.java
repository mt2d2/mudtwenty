package message;

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

		return new ServerMessage(command, Arrays.asList(removeElement(words, 0)));
	}

	/**
	 * Removes an element from a an array yielding a new array with that data.
	 * 
	 * @param elementData
	 *            original array
	 * @param index
	 *            item to remove
	 * @return new array sized to fit
	 */
	private static String[] removeElement(String[] elementData, int index)
	{
		int size = elementData.length;
		String[] newData = new String[size - 1];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, newData, index, numMoved);

		elementData = null; // let the gc work
		return newData;
	}
}
