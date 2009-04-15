package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Terminates a ServerThread's connection to a client, usually invoked by the
 * user when he wants to quit.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ExitResponse implements ServerResponse
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
		serverThread.terminateConnection();
		return null;
	}
}
