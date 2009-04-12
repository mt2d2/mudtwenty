package message;

public class ServerMessage extends Message
{
	private String payload;
	
	public ServerMessage()
	{
		super();
	}
	
	public String getPayload()
	{
		return this.payload;
	}
}
