package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

/**
 * Responds to the command-listing command.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class CommandsResponse implements ServerResponse {

	/**
	 * Give the user a listing of commands.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		String message = "The following commands are available:\n"
				+ "Chat commands:\n"
				+ "\twho: lists all users online, both users and guests\n"
				+ "\ttell <name> <message>: send a private message to a player or mob\n"
				+ "\tsay <message>: send a message to everyone in the same room\n"
				+ "\tooc <message>: send a message to all users connected to the mud\n"
				+ "\temote <message>: sends a message to everyone in your room in the third person"
				+ "General commands:\n"
				+ "\tcommands, help :lists all available commands and general system help\n"
				+ "\tquit, exit: saves your state and exits the mud\n"
				+ "\tscore, sc: gives detailed information about your player\n"
				+ "\tlook, ls, l: look around the room, giving a description\n"
				+ "\tlook, ls, l <entity>: examine an entity, giving its description\n"
				+ "\tinventory, inv: list all the items you are currently carrying\n"
				+ "\tdrop <item>: drop a specified item from your inventory\n"
				+ "\tuse <item>: use a specified item from your inventory\n"
				+ "\tgive <name> <item>: gives an item to a user\n"
				+ "\tget <item>: gets an item that exists in the room\n"
				+ "Movement commands:\n"
				+ "\tmove <direction>: move you to through your specified exit\n"
				+ "\tnorth, n: move north\n"
				+ "\tsouth, s: move south\n"
				+ "\teast, e: move east\n"
				+ "\twest, w: move west\n"
				+ "Admin commands:\n"
				+ "\tshutdown: shuts down the server, saving and notifying players\n";
		
		return new ClientMessage(message);
	}

}
