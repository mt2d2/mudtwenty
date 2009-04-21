import static org.junit.Assert.*;
import items.Potion;
import items.SmallPotion;

import org.junit.Test;

import base.Exit;
import base.Player;
import base.Room;


public class TestCreatureCreation {
	@Test
	public void testHealthPlayer() {
		Player simon = new Player("simon");
		
		// print initial stats
		System.out.println(simon.toString());
		
		// decrease health by 2 and print again
		simon.decreaseHealth(2);
		System.out.println(simon.toString());
		
		// place item in player's collection
		Potion flask = new SmallPotion();
		simon.addItem(flask);
		assertEquals(1, simon.listItems().size());
		
		// use item
		simon.listItems().get(0).Interact(simon);
		System.out.println(simon.toString());
		
		// make sure item was destroyed
		assertTrue(simon.listItems().isEmpty());
	}
	
	@Test
	public void testRoomMovement() {
		Player simon = new Player("simon");
		Room palace = new Room("palace");
		palace.addEntity(simon);
		simon.setRoom(palace);
		
		assertTrue(palace.listPlayers().contains(simon));
		assertEquals(simon.getRoom(), palace);
		
		// new room with exit required level 1
		Room biggerPalace = new Room("bigger palace");
		Exit canPass = new Exit("first exit", biggerPalace, 1);
		
		// new room with ext requiring level 2
		Room tooBigPalace = new Room("too big palace");
		Exit cannotPass = new Exit("second exit", tooBigPalace, 2);
		
		// try to go through second exit, fail
		assertFalse(cannotPass.Interact(simon));
		
		// make it through the first, though
		assertTrue(canPass.Interact(simon));
		
		// make sure player is not still in first room
		assertFalse(palace.listPlayers().contains(simon));
		assertEquals(simon.getRoom(), biggerPalace);
	}
}
