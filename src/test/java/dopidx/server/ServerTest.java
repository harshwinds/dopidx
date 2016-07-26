package dopidx.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.CommandInvoker;
import dopidx.message.MessageParser;
import dopidx.server.Server;

@RunWith(MockitoJUnitRunner.class)
public class ServerTest {
	
	// Class under test
	private Server server;
	
	private int port = 8080;
	@Mock private MessageParser messageParser;
	@Mock private CommandInvoker commandInvoker;
	
	@Before
	public void setup() throws Exception {
		
	}

	@Test
	public void start_successful() {
		server = new Server(port, messageParser, commandInvoker);
		server.start();
		
		assertTrue(server.isRunning());
	}
	
	@Test
	@Ignore
	public void start_failed() {
		server = new Server(80, messageParser, commandInvoker);
		try {
			server.start();
		} catch (Exception e) {
			// server fails to startup due to using a privileged port
		}
		
		assertFalse(server.isRunning());
	}
	
	@Test
	public void stop() {
		server = new Server(port, messageParser, commandInvoker);
		server.start();
		assertTrue(server.isRunning());
		
		server.stop();
		assertFalse(server.isRunning());
	}

}
