package server.response;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import message.ClientMessage;
import server.Server;
import server.ServerThread;
import server.SystemColor;
import util.PropertyLoader;

/**
 * Responds to the shutdown command as directed by the user. This command will
 * save the universe and player state and shutdown the game server if the user
 * has been granted access. Access is based on the Server's
 * configuration.properties.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ShutdownResponse implements ServerResponse
{
	/**
	 * Server configuration and properties, used for setting up the server and
	 * its universe.
	 */
	private static final Properties	conf	= PropertyLoader.loadProperties("server/configuration.properties");

	/**
	 * Shutdown the server if the player is an admin.
	 */
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		List<String> admins = Arrays.asList(conf.getProperty("admin").split(","));

		if (admins.contains(serverThread.getPlayer().getName()))
		{
			Server.sendMessageToAllClients(new ClientMessage("The server is shutting down now.", SystemColor.ERROR));
			serverThread.getServer().shutdown();

			// the server is shutdown, there is no response to send
			return new ClientMessage("The server was unable to be shutdown, check the server logs.");
		}
		else
		{
			// user is not allowed to perform shutdown
			return new ClientMessage("You are not permmitted to execute this command.", SystemColor.ERROR);
		}
	}
}
