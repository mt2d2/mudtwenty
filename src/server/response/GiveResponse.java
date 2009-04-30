package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.item.Item;

/**
 * Invokes give at requested by the user. This command allows a user to give an
 * item to a user. The syntax is give <player name> <item name>.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class GiveResponse implements ServerResponse
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
		if (arguments.size() < 2)
		{
			return new ClientMessage("Invalid syntax: the proper syntax is give <player name> <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			// identify the receipient
			Player receipient = Server.getUniverse().getPlayer(arguments.get(0));
			if (receipient == null)
				return new ClientMessage("that player, " + arguments.get(0) + ", was not found on the system");
			
			Item itemToGive = serverThread.getPlayer().getItem(arguments.get(1));
			if (itemToGive == null)
				return new ClientMessage("that item, " + arguments.get(1) + ", was not found in your inventory");
		
			// give and alert
			serverThread.getPlayer().giveItem(receipient, itemToGive);
			Server.sendMessageToPlayer(receipient.getName(), new ClientMessage("you have recieved " + itemToGive.getName() + " from " + serverThread.getPlayer().getName()));
			return new ClientMessage("you gave " + receipient.getName() + " " + itemToGive.getName());
		}
	}
}
