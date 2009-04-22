package util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import util.Hasher;

public class HasherTest
{	
	@Test
	public void getDigestTest()
	{
		assertEquals("5yfRRkrhJDbomacm2lsvEdg4GyY=", Hasher.getDigest("mypass"));
	}
}
