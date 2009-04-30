package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.Server;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to the look command as issued by the user.
 *
 * @author Michael Tremel
 */
public class LookResponse implements ServerResponse
{

	/**
	 * Currently, this looks at the room. Later, it should be able to be used to look
	 * at any entity.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		StringBuilder message = new StringBuilder();
		final Room roomOfPlayer = Server.getUniverse().getRoomOfCreature(serverThread.getPlayer());
		
		// Append room info.
		message.append(roomOfPlayer.getDescription());

		// Append players in room.
		message.append("\tPlayers in this room: ");
		final List<Player> playersInRoom = Server.getUniverse().getPlayersInRoom(roomOfPlayer);
		for (Player player : playersInRoom)
			message.append(player.getName() + ", ");

		// remove trailing comma or report that there are no players
		if (playersInRoom.size() >= 1)
			message.replace(message.length() - 2, message.length(), " ");
		else
			message.append("no users in the room");

		return new ClientMessage(message.toString());
	}
}
