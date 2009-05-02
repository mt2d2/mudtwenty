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
				+ "\tquit, exit: saves your state and exits the mud\n"
				+ "\tscore, sc: gives detailed information about your player\n"
				+ "\tooc <message>: send a message to all users connected to the mud\n"
				+ "\tcommands, help :lists all available commands and general system help\n"
				+ "\twho, w: lists all users online, both users and guests\n"
				+ "\tlogin <username> <password>: logs a user in\n"
				+ "\tregister <username> <password>: registers a user\n"
				+ "\ttell <name> <message>: send a private message to a player or mob\n"
				+ "\tsay <message>: send a message to everyone in the same room\n"
				+ "\tlook, ls, l: look around the room, giving a description\n"
				+ "\tlook, ls, l <entity>: examine an entity, giving its description\n"
				+ "\tinventory, inv: list all the items you are currently carrying\n"
				+ "\tdrop <item>: drops a specified item from your inventory, e.g., drop <item name>\n"
				+ "\tuse <item>: use a specified item from your inventory, e.g., use <item name>\n"
				+ "\tmove <direction>: moves you to through your specified exit\n"
				+ "\tshutdown: admin command: shuts down the server, saving and notifying players\n"
				+ "\tgive <name> <item>: gives an item to a user\n"
				+ "\tget <item>: gets an item that exists in the room\n";
		
		return new ClientMessage(message);
	}

}
