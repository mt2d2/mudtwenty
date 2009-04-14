package server.response;

import java.util.List;

import message.ClientMessage;

public interface ServerResponse
{
	public ClientMessage respond(List<String> arguments);
}
