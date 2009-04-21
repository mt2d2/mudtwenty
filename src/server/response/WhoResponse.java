package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Responds to the who command as requested by the user. That is, this returns a
 * list of a users online in the mud, both users and guests.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class WhoResponse implements ServerResponse
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage(serverThread.getServer().getUsersOnline());
	}

}
