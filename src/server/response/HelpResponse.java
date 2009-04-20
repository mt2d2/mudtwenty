/**
 * 
 */
package server.response;

import java.util.EnumSet;
import java.util.List;

import message.ClientMessage;
import message.Command;
import server.ServerThread;

/**
 * Responds to the Help command.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class HelpResponse implements ServerResponse
{

	/* (non-Javadoc)
	 * @see server.response.ServerResponse#respond(server.ServerThread, java.util.List)
	 */
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		StringBuilder message = new StringBuilder();
		
		message.append("The following commands are available:\n");
		
		// add all the commands
		for (Command c : EnumSet.allOf(Command.class))
			message.append("\t" + c.toString().toLowerCase() + ": " + c.getDescription() + "\n");
		
		return new ClientMessage(message.toString());
	}

}
