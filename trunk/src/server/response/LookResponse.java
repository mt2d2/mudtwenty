package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.universe.Player;
import server.universe.Room;

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
		StringBuilder message = new StringBuilder();
		final Room userRoom = serverThread.getServer().getUniverse().getRoomOfCreature(serverThread.getPlayer());
		
		// add room 
		message.append(userRoom.getDescription());
		
		// add players
		message.append("\tPlayers in this room: ");
		final List<Player> loggedInPlayers = serverThread.getServer().getUniverse().getPlayersInRoom(serverThread.getServer().getUniverse().getRoomOfCreature(serverThread.getPlayer()));
		
		for (Player p : loggedInPlayers)
			message.append(p.getName() + ", ");
		
		// remove trailing comma
		if (message.length() > 2)
			message.replace(message.length() - 2, message.length(), " ");
		else
			message.append("no users online");
		
		return new ClientMessage(message.toString());
	}
}
