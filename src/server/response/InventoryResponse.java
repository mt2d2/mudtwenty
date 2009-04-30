/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.universe.item.Item;

/**
 * Responds to the inventory command issued by a player. This lists all the
 * items a player has.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class InventoryResponse implements ServerResponse
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
		StringBuilder items = new StringBuilder();
		
		for (Item item : serverThread.getPlayer().getItems())
			items.append(item.getName() + ", ");
		
		if (items.length() != 0)
		{
			items.deleteCharAt(items.length()-1);
			items.deleteCharAt(items.length()-1);
		}
		
		return new ClientMessage("You have " + (items.length() != 0 ? "the following items: " + items : "no items"));
		
	}

}
