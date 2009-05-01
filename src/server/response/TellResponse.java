package server.response;

import java.util.List;

import message.ClientMessage;
import message.Status;
import server.Server;
import server.ServerThread;
import server.universe.MOB;
import server.universe.Room;
import util.ArrayUtil;

/**
 * Responds to the say command as input by the user. This command sends a
 * (private) message to the specified user.
 *
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class TellResponse implements ServerResponse
{

	/**
	 * Send message to receiver and notify sender.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 2)
		{
			return new ClientMessage("the proper syntax of say is: say <user> <message>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			// Get and remove the receiver from the argument list.
			final String receiver = arguments.get(0);
			arguments.remove(0);
			
			final String textSaid = ArrayUtil.joinArguments(arguments, " ");
			if (Server.getUniverse().isLoggedIn(receiver))
			{
				ClientMessage message = new ClientMessage(serverThread.getPlayer().getName()
						+ " says: " + textSaid, Status.CHAT, Server.MESSAGE_TEXT_COLOR);
				Server.sendMessageToPlayer(receiver, message);
			}
			else
			{
				Room room = Server.getUniverse().getRoomOfCreature(serverThread.getPlayer());
				for (MOB mob : Server.getUniverse().getMOBsInRoom(room))
				{
					if (mob.getName().equals(receiver))
					{
						mob.tell(serverThread.getPlayer(), textSaid);
					}
				}
			}

			return new ClientMessage("you said \"" + textSaid + "\" to " + receiver);
		}
	}
}
