package server.response;

import java.util.List;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.universe.Player;
import server.universe.item.Item;
import util.ArrayUtil;

/**
 * Invokes give command as requested by the user. This command allows a user to give an
 * item to a user. The syntax is give <player name> <item name>.
 */
public class GiveResponse implements ServerResponse
{

	/**
	 * If possible, transfer an item from one player to another.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		if (arguments.size() < 2)
		{
			return new ClientMessage("The proper syntax is: give <player name> <item name>", Server.ERROR_TEXT_COLOR);
		}
		else
		{
			Player giver = serverThread.getPlayer();
			
			// PRECONDITION: THE PLAYER NAME IS 1 WORD.
			// ALSO, THE RECIPIENT MUST BE A PLAYER, NOT A MOB.
			String recipientName = arguments.get(0);
			Player recipient = Server.getUniverse().getPlayer(recipientName);
			if (recipient == null)
				return new ClientMessage("That player, " + recipientName + ", was not found on the system.");
			
			// check to see if giver is in the same room as recipient.
			if (!recipient.getRoom().equals(giver.getRoom()))
				return new ClientMessage(recipient.getName() + " is not in the same room as you.");
			
			// identify the item
			String itemName = ArrayUtil.joinArguments(arguments.subList(1, arguments.size()), " ").trim();
			Item itemToGive = giver.getItem(itemName);
			if (itemToGive == null)
				return new ClientMessage("That item, " + itemName + ", was not found in your inventory.");
		
			// give and notify
			giver.giveItem(recipient, itemToGive);
			ClientMessage message = new ClientMessage("You have recieved " + itemName + " from " + giver.getName() + ".");
			Server.sendMessageToPlayer(recipient, message);
			return new ClientMessage("You gave " + recipient.getName() + " " + itemToGive.getName());
		}
	}
}
