package server.response;

import java.util.ArrayList;
import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Direction;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to the move command as input by the user. Specifically, this will
 * move a user from his current room, to the room which is connected by an exit
 * the user selects.
 */
public class MoveResponse implements ServerResponse
{

	private Direction direction; 
	
	/**
	 * Alternative constructor for MoveResponse that takes a direction.
	 */
	public MoveResponse(Direction direction)
	{
		this.direction = direction;
	}
	
	/**
	 * Default constructor.
	 */
	public MoveResponse()
	{
	}

	/**
	 * Move the player (and notify users in both the old and new room) if possible.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (this.direction != null)
		{
			return this.respond(serverThread, this.direction);
		}
		else if (arguments.size() != 1)
		{
			return new ClientMessage("proper syntax of move: move <exit name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			// get the direction from the first argument
			this.direction = stringToDirection(arguments.get(0));
			if (this.direction == null)
				return new ClientMessage("that direction was not recognized", Server.ERROR_TEXT_COLOR);

			return this.respond(serverThread, this.direction);
		}
	}

	/**
	 * An alternative respond method that takes a direction instead of a list of arguments.
	 * This may be used directly by other responses such as NorthResponse, EastResponse etc.
	 */
	public ClientMessage respond(ServerThread serverThread, Direction direction)
	{
		Player player = serverThread.getPlayer();
		Room oldRoom = Server.getUniverse().getRoomOfCreature(player);

		// make sure that the exit exists
		if (!oldRoom.hasExit(direction))
			return new ClientMessage("there is no exit in that direction", Server.ERROR_TEXT_COLOR);

		// get the new room
		Room newRoom = oldRoom.getExit(direction).getRoom();
		
		// make sure that the exit is unlocked
		if (newRoom.requiresKey())
			if (!newRoom.isLocked())
				return new ClientMessage("the exit leading to " + newRoom.getName() + " is currently locked");

		// make the swap
		Server.getUniverse().changeRoomOfCreature(player, newRoom);

		// notify all users that the king has left the building
		ClientMessage leavingMessage = new ClientMessage(player.getName() + " has left the room");
		Server.sendMessageToAllClientsInRoom(oldRoom, leavingMessage);

		// and that he has entered
		ClientMessage comingMessage = new ClientMessage(player.getName() + " has entered the room");
		Server.sendMessageToAllClientsInRoom(newRoom, comingMessage);

		// invoke the look response to show the user the new room
		return new LookResponse().respond(serverThread, new ArrayList<String>());
	}

	/**
	 * @return the direction, or null
	 */
	private Direction stringToDirection(String s)
	{
		for (Direction direction : Direction.values())
			if (direction.toString().equals(s.toUpperCase()))
				return direction;
		return null;
	}

}
