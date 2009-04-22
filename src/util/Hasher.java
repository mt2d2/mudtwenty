package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

/**
 * Simple utility class that calculates the SHA-1 hash of various inputs. For
 * password, getting the digest of a String is very convenient.
 * 
 * @author Michael Tremel (mtremel@email.arizona.edu)
 */
public class Hasher
{
	private static final String		ALGORITHM	= "SHA";
	private static MessageDigest	md;

	static
	{
		try
		{
			md = MessageDigest.getInstance(ALGORITHM);
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * @param s
	 *            input to be hashed
	 * @return the SHA-1 digest of s
	 */
	public static String getDigest(String s)
	{
		md.update(s.getBytes(), 0, s.getBytes().length);
		final String encodedAndHashedString = new BASE64Encoder().encode(md.digest());

		md.reset();

		return encodedAndHashedString;
	}
}
