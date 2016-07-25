package dopidx.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.CommandInvoker;
import dopidx.message.Message;
import dopidx.message.MessageParser;

@RunWith(MockitoJUnitRunner.class)
public class WorkerTest {
	
	// Class under test
	private Worker worker;
	
	@Mock private Socket client;
	@Mock private MessageParser messageParser;
	@Mock private CommandInvoker commandInvoker;
	private ResponseCode responseCode = ResponseCode.OK;
	
	@Mock private InputStream inputStream;
	@Mock private OutputStream outputStream;
	
	@Before
	public void setup() throws Exception {
		worker = new Worker(client, messageParser, commandInvoker);
		
		when(client.getInputStream()).thenReturn(inputStream);
		when(client.getOutputStream()).thenReturn(outputStream);
		
		when(commandInvoker.invoke(any(Message.class))).thenReturn(responseCode);
	}

	@Test
	public void execute_nullMessage() throws IOException {
		Message message = null;
		
		ResponseCode response = worker.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(commandInvoker, never()).invoke(message);
	}
	
	@Test
	public void execute_ok() throws IOException {
		Message message = new Message("INDEX", "cloog");
		when(commandInvoker.invoke(message)).thenReturn(ResponseCode.OK);
		
		ResponseCode response = worker.execute(message);
		
		assertEquals(ResponseCode.OK, response);
	}
	
	@Test
	public void execute_fail() throws IOException {
		Message message = new Message("INDEX", "cloog");
		when(commandInvoker.invoke(message)).thenReturn(ResponseCode.FAIL);
		
		ResponseCode response = worker.execute(message);
		
		assertEquals(ResponseCode.FAIL, response);
	}
	
	@Test
	public void verifyStreamsClosed() throws IOException {
		worker.run();
		
		verify(outputStream).close();
		verify(inputStream).close();
	}

}
