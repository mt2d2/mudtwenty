package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Simple demo response that repeats whatever the user said back to the user.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class EchoResponse implements ServerResponse
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("Echo says: " + joinArguments(arguments, "/"));
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
	private static final String joinArguments(List<String> arguments, String symbol)
	{
		StringBuilder toReturn = new StringBuilder();

		for (String s : arguments)
			toReturn.append(s + symbol);

		return toReturn.toString();
	}
}
