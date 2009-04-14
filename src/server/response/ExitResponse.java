package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

public class ExitResponse implements ServerResponse
{
	public ExitResponse()
	{
	}

	@Override
	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		serverThread.terminateConnection();
		return null;
	}

}
