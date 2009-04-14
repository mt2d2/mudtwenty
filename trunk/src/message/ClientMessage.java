package message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class ClientMessage extends Message
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	private String				payload;
	private Color				color;

	public ClientMessage() // Externalizable requires blank constructor
	{
	}
	
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

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		// avoid a call to super for speed
		this.time = in.readLong();
		this.status = Status.valueOf(in.readUTF());

		this.payload = in.readUTF();
		
		if (! in.readBoolean())
			this.color = new Color(in.readInt());
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		// properties of message
		// avoid a call to super for speed
		out.writeLong(this.time);
		out.writeUTF(this.status.name());
		
		// write payload, non-nullable
		out.writeUTF(this.payload);
		
		// write Color, nullable  
		final boolean isColorNull = this.color == null;
		out.writeBoolean(isColorNull);
		if (! isColorNull)
			out.writeInt(this.color.getRGB());	
	}
}
