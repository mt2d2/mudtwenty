package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import util.ArrayUtil;

/**
 * Simple demo response that repeats whatever the user said back to the user.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class EchoResponse implements ServerResponse
{

	/**
	 * Echo user input.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("Echo: " + ArrayUtil.joinArguments(arguments, " "));
	}
}
