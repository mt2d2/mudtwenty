package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Exit;
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() != 1)
		{
			return new ClientMessage("proper syntax of move: move <exit name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			List<Exit> exits = serverThread.getServer().getUniverse().getRoomOfCreature(serverThread.getPlayer()).getExits();
			Room newRoom = null;
			
			for (Exit e : exits)
				if (e.getName().equals(arguments.get(0)))
					newRoom = e.getRoom();
			
			if (newRoom != null)
			{
				serverThread.getServer().getUniverse().changeRoomOfCreature(serverThread.getPlayer(), newRoom);
				return new ClientMessage("you have been moved to " + arguments.get(0));
			}
			else
			{
				return new ClientMessage("that room name was not recognized", Server.ERROR_TEXT_COLOR);
			}
		}
	}
}
