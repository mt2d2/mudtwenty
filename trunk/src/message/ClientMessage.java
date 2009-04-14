package message;

import java.awt.Color;

public class ClientMessage extends Message
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	private String payload;
	private Color color;
	
	public ClientMessage(String payload)
	{
		this(payload, null);
	}
	
	public ClientMessage(String payload, Color color)
	{
		super();
		
		this.payload = payload;
		this.color = color;
	}
	
	public String getPayload()
	{
		return this.payload;
	}
	
	public Color getColor()
	{
		return this.color;
	}
}
