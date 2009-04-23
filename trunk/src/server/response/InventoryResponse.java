/**
 * 
 */
package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;
import server.universe.Item;

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
			items.append(item.toString() + " ");
		
		return new ClientMessage("You have the following items: " + items);
		
	}

}
