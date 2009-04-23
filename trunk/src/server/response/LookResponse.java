/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;

/**
 * Responds to the look command as issued by the user. This provides detailed
 * information about the enviroment a players character is currently in,
 * specifically about the room.
 * 
 * @author Michael Tremel
 */
public class LookResponse implements ServerResponse
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
