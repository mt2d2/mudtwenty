package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Room;
import util.ArrayUtil;

/**
 * Responds to the emote command as input by the user. This allows users to
 * speak about themselves in the third person, which may or may not be creepy.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class EmoteResponse implements ServerResponse
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
		if (arguments.size() < 1)
		{
			return new ClientMessage("Improper syntax: the proper syntax is emote <phrase>");
		}
		else
		{
			Room roomOfPlayer = Server.getUniverse().getRoomOfCreature(serverThread.getPlayer());
			String phrase = ArrayUtil.joinArguments(arguments, " ").trim();

			Server.sendMessageToAllClientsInRoom(roomOfPlayer, new ClientMessage("*" + serverThread.getPlayer().getName() + " " + phrase + "*"));
			return new ClientMessage("You emoted, \"" + phrase + "\"");
		}
	}
}
