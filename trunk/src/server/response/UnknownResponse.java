package server.response;

import java.util.List;

import message.ClientMessage;

public class UnknownResponse implements ServerResponse
{

	@Override
	public ClientMessage respond(List<String> arguments)
	{
		return new ClientMessage("The command you entered was not understood. Type help for a list of available commands.");
	}
}
