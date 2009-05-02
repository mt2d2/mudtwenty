package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.Room;
import server.universe.item.Item;

/**
 * Invokes give at requested by the user. This command allows a user to give an
 * item to a user. The syntax is give <player name> <item name>.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class GiveResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 2)
		{
			return new ClientMessage("Invalid syntax: the proper syntax is give <player name> <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player giver = serverThread.getPlayer();
			
			// identify the recipient
			Player recipient = Server.getUniverse().getPlayer(arguments.get(0));
			if (recipient == null)
				return new ClientMessage("That player, " + arguments.get(0) + ", was not found on the system.");
			
			// check to see if user is in the same room
			Room room = Server.getUniverse().getRoomOfCreature(giver);
			List<Player> players = Server.getUniverse().getPlayersInRoom(room);
			if (! players.contains(recipient))
				return new ClientMessage(recipient.getName() + " is not in the same room as you.");
			
			// identify the item
			String itemName = arguments.get(1);
			Item itemToGive = serverThread.getPlayer().getItem(itemName);
			if (itemToGive == null)
				return new ClientMessage("That item, " + itemName + ", was not found in your inventory.");
		
			// give and notify
			serverThread.getPlayer().giveItem(recipient, itemToGive);
			ClientMessage message = new ClientMessage("You have recieved " + itemName + " from " + giver.getName() + ".");
			Server.sendMessageToPlayer(recipient, message);
			return new ClientMessage("You gave " + recipient.getName() + " " + itemToGive.getName());
		}
	}
}
