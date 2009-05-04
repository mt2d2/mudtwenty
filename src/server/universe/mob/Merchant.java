package server.universe.mob;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import server.Server;
import server.universe.Creature;
import server.universe.item.Item;
/**
 * The merchant class models a MOB that can sell his items to players.
 * This creature will sell whatever he has in his inventory until runs out.
 */
public class Merchant extends MOB {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Default constructor sets up dialog.
	 * 
	 * @param name
	 * 			name of the merchant
	 */
	public Merchant(String name) {
		super(name);
		this.setMaxHealth(1);
		this.setDescription("Talk to this me to find out how to buy items.");
		this.setDialog(new HelloDialog());
	}
	
	/**
	 * What happens when a player talks to a merchant. Users can
	 * buy items by typing "tell <merchantName> buy <item>"
	 */
	private class HelloDialog implements DialogStrategy
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Either buys an item or displays hello message
		 */
		public void tell(Creature sender, String message)
		{
			Scanner input = new Scanner(message);
			
			if (input.next().toLowerCase().equals("buy"))
			{
				StringBuffer sb = new StringBuffer();
				while (input.hasNext())
					sb.append(input.next() + " ");
				String itemToBuyName = new String(sb).trim();
				
				List<Item> itemsForSale = Merchant.this.getItems();
				
				Iterator i = itemsForSale.iterator();
				while (i.hasNext())
				{
					Object o = i.next();
					if (itemToBuyName.equals(((Item)o).getName()))
					{
						Item toExchange = Merchant.this.getItem(((Item)o).getName());
						if (sender.decreaseGold(toExchange.getPrice()))
						{
							sender.addItem(toExchange);
							Server.getUniverse().sendMessageToCreature(Merchant.this, sender, "You successfully bought " + toExchange.getName() + " for " + toExchange.getPrice() + " gold");
							i.remove();
						} else
						{
							Server.getUniverse().sendMessageToCreature(Merchant.this, sender, "I am sorry, but you do not have enough gold for that item");
						}
					}
				}
			} else
			{
				Server.getUniverse().sendMessageToCreature(Merchant.this, sender, "The items I have for sale are as follows:\n" + Merchant.this.itemsForSale());
			}
		}
	}

	/**
	 * For now, does nothing. Possibly restock?
	 * TODO restock items
	 */
	public void takeTurn()
	{
		setBehavior(new NullBehavior());
	}
	
	/**
	 * @return
	 * 		String representation of the items for sale
	 */
	public String itemsForSale()
	{
		List<Item> itemsForSale = this.getItems();
		StringBuffer sb = new StringBuffer();
		
		for (Item item : itemsForSale)
			sb.append(item.getName() + ": " + item.getPrice() + " gold" + "\n");
		
		return new String(sb);
	}
}
