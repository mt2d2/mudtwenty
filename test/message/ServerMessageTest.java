package message;

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ServerMessageTest
{
	@Test
	public void SpeedTest()
	{
		long start = 0l;

		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		start = System.currentTimeMillis();
		List<ServerMessage> messageObjects = new ArrayList<ServerMessage>();
		
		for (int x = 0; x < 1000; x++)
			messageObjects.add(new ServerMessage(Command.ECHO, Arrays.asList(new String[] {"hi", "hi"})));

		System.out.println("Took _" + (System.currentTimeMillis() - start) + "_ milliseconds to create objects");

		start = System.currentTimeMillis();
		int x = 1;
		for (ServerMessage message : messageObjects)
		{
			File file = new File(tempDir, "tempFile" + x++);
			ObjectOutputStream os;
			try
			{
				os = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				os.writeObject(message);
				os.flush();
				os.close();
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		long payload = getPayload();
		System.out.println("Took _" + (System.currentTimeMillis() - start) + "_ milliseconds to serialize objects");

		start = System.currentTimeMillis();
		x = 1;
		for (ServerMessage message : messageObjects)
		{
			try
			{
				File file2 = new File(tempDir, "tempFile" + x++);
				ObjectInputStream is = new ObjectInputStream(new FileInputStream(file2));
				ServerMessage prHost = (ServerMessage) is.readObject();
			 	assertEquals(message.getCommand(), prHost.getCommand());
				is.close();
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Took _" + (System.currentTimeMillis() - start) + "_ milliseconds to deserialize objects");

		System.out.println("Payload: " + payload);

	}

	public static long getPayload()
	{
		File tempDir = new File(System.getProperty("java.io.tmpdir"));
		long payload = 0;
		for (int x = 1; x <= 1000; x++)
		{
			File file = new File(tempDir, "tempFile" + x++);
			payload += file.length();
		}

		return payload;
	}

}
