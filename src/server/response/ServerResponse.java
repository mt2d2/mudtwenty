package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * This interface dictates a correct response from the Server to a Client.
 * ServerResponse are externally associated with a Command.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public interface ServerResponse
{
	/**
	 * Dictates a a response to a Client from a given serverThread. The response
	 * can use a list of arguments for the command in determining its response.
	 * 
	 * @param serverThread
	 *            ServerThread requesting this response
	 * @param arguments
	 *            arguments passed from the client as part of the command, which
	 *            can be used to determine an appropriate response
	 * @return response to be sent back to the client
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments);
}
