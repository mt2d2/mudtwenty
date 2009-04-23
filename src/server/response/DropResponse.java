/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;

/**
 * Responds to the drop command as input by the user. Drops a specified item
 * from the inventory.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class DropResponse implements ServerResponse
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
