package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

public class EchoResponse implements ServerResponse
{
	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("Echo says: " + joinArguments(arguments, "/"));
	}

	private static final String joinArguments(List<String> arguments, String deliminator)
	{
		StringBuilder toReturn = new StringBuilder();

		for (String s : arguments)
			toReturn.append(s + deliminator);

		return toReturn.toString();
	}
}
