package client;

import java.util.Arrays;

import message.Command;
import message.ServerMessage;

/**
 * Simple utility that parses a user's input into a ClientMessage that can be
 * sent to the Server.
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
	 *            user-provided input
	 * @return message which can be sent to the server.
	 */
	public static final ServerMessage parse(String input)
	{
		String[] chunks = input.split(" ");
		Command command = null;

		try
		{
			command = Command.valueOf(chunks[0].toUpperCase());
		}
		catch (IllegalArgumentException e)
		{
			command = Command.UNKNOWN;
		}

		return new ServerMessage(command, Arrays.asList(removeElement(chunks, 0)));
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
