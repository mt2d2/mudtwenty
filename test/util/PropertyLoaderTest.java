package util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Test;

public class PropertyLoaderTest
{
	@Test
	public void loadProperties()
	{
		Properties properties = PropertyLoader.loadProperties("server/configuration.properties");
		
		assertNotNull(properties);
		assertTrue(properties instanceof Properties);
	}
}
