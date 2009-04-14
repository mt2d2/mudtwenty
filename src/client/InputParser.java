package client;

import java.util.Arrays;

import message.Command;
import message.ServerMessage;

public class InputParser
{
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
