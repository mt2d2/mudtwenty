package message;

public class ClientMessage extends Message
{
	private String payload;
	
	public ClientMessage()
	{
		super();
	}
	
	public String getPayload()
	{
		return this.payload;
	}
}
