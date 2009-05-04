package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.Room;

/**
 * Responds to various social commands, i.e., wink, giggle, etc. Constructor
 * takes in a string that represents the verb of the social interaction.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class SocialResponse implements ServerResponse
{
	private String	verb;

	/**
	 * Constructs the social interaction
	 * 
	 * @param verb
	 *            type of social interaction
	 */
	public SocialResponse(String verb)
	{
		this.verb = verb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see server.response.ServerResponse#respond(server.ServerThread,
	 * java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		Room roomOfPlayer = Server.getUniverse().getRoomOfCreature(serverThread.getPlayer());
		
		if (arguments.size() < 1)
		{
			Server.sendMessageToAllClientsInRoom(roomOfPlayer, new ClientMessage(serverThread.getPlayer().getName() + " " + this.verb));
			return new ClientMessage("You " + this.verb.substring(0, this.verb.length() - 1) + "ed at the room!");
		}
		else
		{
			Player target = Server.getUniverse().getPlayer(arguments.get(0));
			
			if (target == null)
				return new ClientMessage("That player, " + arguments.get(0)  + ", could not be found.", Server.ERROR_TEXT_COLOR);
			
			Server.sendMessageToAllClientsInRoom(roomOfPlayer, new ClientMessage(serverThread.getPlayer().getName() + " " + this.verb + " at " + target.getName()));
			
			return new ClientMessage("You " + this.verb.substring(0, this.verb.length() - 1) + "ed at " + target.getName() + " to the whole room!");
		}
	}
}
