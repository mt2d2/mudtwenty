package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
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

	/**
	 * Generate a message with a list of logged in players.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		final List<Player> players = Server.getUniverse().getLoggedInPlayers();
		
		String message;
		if (players.isEmpty())
			message = "There are no players online.";
		else if (players.size() == 1)
			message = "There is 1 player online: " + listPlayerNames(players);
		else
			message = "There are " + players.size() + " players online: " + listPlayerNames(players);

		return new ClientMessage(message, Status.CHAT, null);
	}

	/**
	 * Make a properly formatted list of player names.
	 * 
	 * PRECONDITION: List is not empty.
	 */
	private String listPlayerNames(List<Player> players)
	{
		if (players.size() == 1)
			return players.get(0).getName() + ".";
		else
			return players.get(0).getName() + ", " + listPlayerNames(players.subList(1, players.size()));
	}

}
