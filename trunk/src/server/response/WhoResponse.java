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
		switch (players.size())
		{
			case 0:
				message = "There are no players online.";
				break;
			case 1:
				message = "There is 1 player online: " + listPlayerNames(players);
				break;
			default:
				message = "There are " + players.size() + " players online: " + listPlayerNames(players);
				break;
		}

		return new ClientMessage(message, Status.CHAT, null);
	}

	/**
	 * Make a properly formatted list of player names.
	 * 
	 * Precondition: List is not empty.
	 */
	private String listPlayerNames(List<Player> players)
	{
		if (players.size() == 1)
			return players.get(0).getName() + ".";
		else
			return players.get(0).getName() + ", " + listPlayerNames(players.subList(1, players.size()));
	}

}
