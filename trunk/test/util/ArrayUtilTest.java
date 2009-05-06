package util;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArrayUtilTest
{
	@Test
	public void removeElement()
	{
		String[] nums = {"", "", "", ""};
		assertEquals(4, nums.length);
		
		assertEquals(3, ArrayUtil.removeElement(nums, 0).length);
	}

	@Test
	public void joinArguments()
	{
		List<String> words = Arrays.asList("hello", "world");
		assertEquals(2, words.size());
		
		assertEquals("hello,world,", ArrayUtil.joinArguments(words, ","));
	}
}
