package message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This extends MessageProtocol, providing data useful for a Client to receive.
 * In this message, the client receives a payload response from the server in a
 * specific color.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class ClientMessage extends MessageProtocol
{
	private static final long	serialVersionUID	= 1L;

	private String				payload;
	private Color				color;

	/**
	 * This constructor is required by Externalizable. There are no setters to
	 * access the data, making a blank message. DO NOT USE!
	 */
	public ClientMessage() // Externalizable requires blank constructor
	{
	}

	/**
	 * Constructs with just a payload message, assumes a null color.
	 * 
	 * @param payload
	 *            message that will be sent to the user
	 */
	public ClientMessage(String payload)
	{
		this(payload, null);
	}

	/**
	 * Constructs with a payload message and a color.
	 * 
	 * @param payload
	 *            message that will be sent to the user
	 * @param color
	 *            color of payload
	 */
	public ClientMessage(String payload, Color color)
	{
		super();

		this.payload = payload;
		this.color = color;
	}

	/**
	 * @return message payload visible to the user
	 */
	public String getPayload()
	{
		return this.payload;
	}

	/**
	 * @return color of payload visible to the user
	 */
	public Color getColor()
	{
		return this.color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageProtocol#readExternal(java.io.ObjectInput)
	 */
	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		// avoid a call to super for speed
		this.time = in.readLong();
		this.status = Status.valueOf(in.readUTF());

		this.payload = in.readUTF();

		if (!in.readBoolean())
			this.color = new Color(in.readInt());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see message.MessageProtocol#writeExternal(java.io.ObjectOutput)
	 */
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
		if (!isColorNull)
			out.writeInt(this.color.getRGB());
	}
}
