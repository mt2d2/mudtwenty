package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.ServerThread;
import server.universe.Player;

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
		StringBuilder message = new StringBuilder();
		final List<Player> loggedInPlayers = serverThread.getServer().getUniverse().getLoggedInPlayers();
		
		for (Player p : loggedInPlayers)
			message.append(p.getName() + ", ");
		
		// remove trailing comma
		if (message.length() > 2)
			message.replace(message.length() - 2, message.length(), " ");
		else
			message.append("no users online");
		
		return new ClientMessage(message.toString(), Status.CHAT, null);
	}
}
