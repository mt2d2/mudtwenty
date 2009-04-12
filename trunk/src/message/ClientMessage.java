package message;

public class ClientMessage extends Message
{
	private Command command;
	
	public ClientMessage()
	{
		super();
	}
	
	public Command getCommand()
	{
		return this.command;
	}
}
