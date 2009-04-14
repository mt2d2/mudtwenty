package server.response;

import java.util.List;

import message.ClientMessage;
import server.ServerThread;

public interface ServerResponse
{
	public ClientMessage respond(ServerThread serverThread, List<String> arguments);
}
