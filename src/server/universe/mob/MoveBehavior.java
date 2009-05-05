/**
 * 
 */
package server.universe.mob;

import java.util.List;
import java.util.Random;

import message.ClientMessage;
import server.Server;
import server.universe.Exit;
import server.universe.Room;

public class MoveBehavior implements BehaviorStrategy
{
	private static final long serialVersionUID = 1L;

	/**
	 * Move to the first room that is seen, if possible.
	 */
	public void doAction(MOB mob)
	{
		Room oldRoom = mob.getRoom();
		List<Exit> exits = oldRoom.getUnlockedExits();
		if (!exits.isEmpty())
		{
		
			// randomize which exit the mob takes
			int exitSize = exits.size();
			int randomRoomChoice = new Random(System.currentTimeMillis()).nextInt(exitSize);			
			Room newRoom = exits.get(randomRoomChoice).getRoom();
			
			mob.setRoom(newRoom);
			Server.sendMessageToAllClientsInRoom(oldRoom, new ClientMessage(mob.getName() + " has left the room."));
			Server.sendMessageToAllClientsInRoom(newRoom, new ClientMessage(mob.getName() + " has entered the room."));
		}
	}
}