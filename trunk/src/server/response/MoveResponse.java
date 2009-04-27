package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Exit;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to the move command as input by the user. Specifically, this will
 * move a user from his current room, to the room which is connected by an exit
 * the user selects.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class MoveResponse implements ServerResponse
{

	/**
	 * Move the player (and notify users in both the old and new room) if possible.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() != 1)
		{
			return new ClientMessage("proper syntax of move: move <exit name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player player = serverThread.getPlayer();
			Room oldRoom = Server.getUniverse().getRoomOfCreature(player);
			List<Exit> exits = oldRoom.getExits();

			Room newRoom = null;
			for (Exit exit : exits)
				if (exit.getName().equals(arguments.get(0)))
					newRoom = exit.getRoom();

			if (newRoom == null)
				return new ClientMessage("that room name was not recognized", Server.ERROR_TEXT_COLOR);

			Server.getUniverse().changeRoomOfCreature(serverThread.getPlayer(), newRoom);

			ClientMessage leavingMessage = new ClientMessage(player.getName() + " has left the room");
			Server.sendMessageToAllClientsInRoom(oldRoom, leavingMessage);

			ClientMessage comingMessage = new ClientMessage(player.getName() + " has entered the room");
			Server.sendMessageToAllClientsInRoom(newRoom, comingMessage);

			return new ClientMessage("you have been moved to " + arguments.get(0));
		}
	}
}
