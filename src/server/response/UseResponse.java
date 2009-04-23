/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;

/**
 * Responds to the use command as input by the user. Uses a specified item in
 * the users inventory.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class UseResponse implements ServerResponse
{
	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("NOT IMPLEMENTED", Server.ERROR_TEXT_COLOR);
	}
}
