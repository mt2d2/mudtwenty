package server.response;

import java.util.List;

import server.ServerThread;

import message.ClientMessage;

/**
 * The default response to any action that the Server does not recognize. Spits
 * an unhappy message back to the user, asking them to get a clue.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class UnknownResponse implements ServerResponse
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("The command you entered was not understood. "
			+ "Type help for a list of available commands.");
	}
}
