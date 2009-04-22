package util;

import java.util.List;

public class ArrayUtil
{
	/**
	 * Removes an element from a an array yielding a new array with that data.
	 * 
	 * @param elementData
	 *            original array
	 * @param index
	 *            item to remove
	 * @return new array sized to fit
	 */
	public static String[] removeElement(String[] elementData, int index)
	{
		int size = elementData.length;
		String[] newData = new String[size - 1];

		int numMoved = size - index - 1;
		if (numMoved > 0)
			System.arraycopy(elementData, index + 1, newData, index, numMoved);

		elementData = null; // let the gc work
		return newData;
	}
	
	/**
	 * Joins a List<String> with a symbol into a single String. Why isn't
	 * this part of List? Blah.
	 * 
	 * @param arguments
	 *            segments to be joined
	 * @param symbol
	 *            what the segments will be joined with
	 * @return joined String of all the arguments
	 */
	public static final String joinArguments(List<String> arguments, String symbol)
	{
		StringBuilder toReturn = new StringBuilder();

		for (String s : arguments)
			toReturn.append(s + symbol);

		return toReturn.toString();
	}
}
