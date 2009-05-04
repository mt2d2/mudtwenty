package server.universe.item;

import message.ClientMessage;
import server.universe.Creature;
import server.universe.Skill;

/**
 * Keys are special items that can unlock rooms.
 * 
 * Note that currently, keys unlock a room from any exit, not a particular exit.
 */
public class Book extends Item
{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a book with default attributes.
	 */
	public Book()
	{
		setName("book");
		setDescription("It is large and filled with arcane lore.");
		setPrice(20);
	}
	

	/**
	 * Unlock the room if the room that the key can be used on is adjacent. Otherwise, report that the key doesn't work.
	 */
	public ClientMessage use(Creature creature)
	{
		StringBuilder message = new StringBuilder();
		message.append("You practice your skills at reading and try to read the book.\n");
		if (creature.getSkillValue(Skill.READING) < 5)
			message.append("You are not very successful.");
		else if (creature.getSkillValue(Skill.READING)< 10)
			message.append("You can understand some symbols."); 
		else if (creature.getSkillValue(Skill.READING)< 15)
			message.append("You learn something from the text.");
		else if (creature.getSkillValue(Skill.READING)< 20)
			message.append("You understand most of the text.");
		else
			message.append("You understand the entire text. Congratulations.");
		
		creature.practice(Skill.READING);
		return new ClientMessage(message.toString());
	}
}
