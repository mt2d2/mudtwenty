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
		StringBuilder message = new StringBuilder();
		final List<Player> loggedInPlayers = Server.getUniverse().getLoggedInPlayers();

		// Make a comma-separated list of players.
		for (Player p : loggedInPlayers)
			message.append(p.getName() + ", ");

		// Remove trailing comma, or modify message to
		if (message.length() > 2)
		{
			message.insert(0, "users online: ");
			message.replace(message.length() - 2, message.length(), " ");
		}
		else
		{
			message.append("no users online");
		}

		return new ClientMessage(message.toString(), Status.CHAT, null);
	}
}
