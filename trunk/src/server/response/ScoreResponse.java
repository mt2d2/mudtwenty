package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Responds to the score command as issued by the user. This provides detailed
 * information about the player the user is currently player.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ScoreResponse implements ServerResponse
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
		return new ClientMessage(serverThread.getPlayer().getDescription());
	}

}
