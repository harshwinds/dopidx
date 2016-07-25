package dopidx.command;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.QueryCommand;
import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

@RunWith(MockitoJUnitRunner.class)
public class QueryCommandTest {
	
	// Class under test
	private QueryCommand command;
	
	@Mock private Index index;
	
	private String pkg = "cloog";
	
	@Before
	public void setup() throws Exception {
		command = new QueryCommand(index);
	}

	@Test
	public void query_invalidMessage_invalidCommand() {
		Message message = new Message("invalid", pkg);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).exists(pkg);
	}
	
	@Test
	public void query_invalidMessage_invalidPackage() {
		Message message = new Message(QueryCommand.NAME, "");
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).exists(pkg);
	}
	
	@Test
	public void query_exists() {
		Message message = new Message(QueryCommand.NAME, pkg);
		when(index.exists(pkg)).thenReturn(true);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.OK, response);
	}
	
	@Test
	public void query_doesNotExist() {
		Message message = new Message(QueryCommand.NAME, pkg);
		when(index.exists(pkg)).thenReturn(false);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.FAIL, response);
	}
}
