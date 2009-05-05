package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;

/**
 * The default response to any action that the Server does not recognize. Spits
 * an unhappy message back to the user, asking them to get a clue.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class UnknownResponse implements ServerResponse
{

	/**
	 * Send a friendly, helpful, wonderful message to the user.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("The command you entered was not found. Type help for a list of commands.",
				Server.ERROR_TEXT_COLOR);
	}
}
