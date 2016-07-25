package dopidx.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MessageParserTest {
	
	// Class under test
	private MessageParser messageParser;
	
	@Before
	public void setup() throws Exception {
		messageParser = new MessageParser();
	}

	@Test
	public void message_invalid() {
		Message message = messageParser.parseMessage("invalid");
		assertNull(message);
	}
	
	@Test
	public void message_index() {
		Message message = messageParser.parseMessage("INDEX|ceylon|");
		assertNotNull(message);
		assertEquals("INDEX", message.getCommand());
		assertEquals("ceylon", message.getPackage());
		assertNull(message.getDependencies());
	}
	
	@Test
	public void message_indexWithDependencies() {
		Message message = messageParser.parseMessage("INDEX|cloog|gmp,isl,pkg-config");
		assertNotNull(message);
		assertEquals("INDEX", message.getCommand());
		assertEquals("cloog", message.getPackage());
		assertNotNull(message.getDependencies());
		assertEquals(3, message.getDependencies().size());
		assertTrue(message.getDependencies().contains("gmp"));
		assertTrue(message.getDependencies().contains("isl"));
		assertTrue(message.getDependencies().contains("pkg-config"));
	}

	@Test
	public void message_remove() {
		Message message = messageParser.parseMessage("REMOVE|cloog|");
		assertNotNull(message);
		assertEquals("REMOVE", message.getCommand());
		assertEquals("cloog", message.getPackage());
		assertNull(message.getDependencies());
	}
	
	@Test
	public void message_query() {
		Message message = messageParser.parseMessage("QUERY|cloog|");
		assertNotNull(message);
		assertEquals("QUERY", message.getCommand());
		assertEquals("cloog", message.getPackage());
		assertNull(message.getDependencies());
	}
}
