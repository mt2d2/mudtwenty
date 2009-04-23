package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Responds to the say command from the user. This sends a message to everyone
 * that is in the same room as that user.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class SayResponse implements ServerResponse
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
		// TODO Auto-generated method stub
		return null;
	}
}
