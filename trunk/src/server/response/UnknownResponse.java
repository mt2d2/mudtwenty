package server.response;

import java.util.List;

import server.ServerThread;

import message.ClientMessage;

public class UnknownResponse implements ServerResponse
{

	public ClientMessage respond(ServerThread serverThread, List<String> arguments)
	{
		return new ClientMessage("The command you entered was not understood. Type help for a list of available commands.");
	}
}
