package message;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * This extends MessageProtocol, providing data useful for a Client to receive.
 * That is, a server might send a ClientMessage to a server -- this is not a message
 * that a client might create.
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
	 *            message string that will be sent to the user
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
	 * @return The message payload visible to the user
	 */
	public String getPayload()
	{
		return this.payload;
	}

	/**
	 * @return Color of payload visible to the user
	 */
	public Color getColor()
	{
		return this.color;
	}

	/* (non-Javadoc)
	 * @see message.MessageProtocol#readExternal(java.io.ObjectInput)
	 */
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		// properties of message
		super.readExternal(in);

		this.payload = in.readUTF();

		if (!in.readBoolean())
			this.color = new Color(in.readInt());
	}

	/* (non-Javadoc)
	 * @see message.MessageProtocol#writeExternal(java.io.ObjectOutput)
	 */
	public void writeExternal(ObjectOutput out) throws IOException
	{
		// properties of message
		super.writeExternal(out);

		// write payload, non-nullable
		out.writeUTF(this.payload);

		// write Color, nullable
		final boolean isColorNull = this.color == null;
		out.writeBoolean(isColorNull);
		if (!isColorNull)
			out.writeInt(this.color.getRGB());
	}
}
