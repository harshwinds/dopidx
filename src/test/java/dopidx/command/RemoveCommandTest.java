package dopidx.command;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.RemoveCommand;
import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

@RunWith(MockitoJUnitRunner.class)
public class RemoveCommandTest {
	
	// Class under test
	private RemoveCommand command;
	
	@Mock private Index index;
	
	private String pkg = "cloog";
	
	@Before
	public void setup() throws Exception {
		command = new RemoveCommand(index);
	}

	@Test
	public void remove_invalidMessage_invalidCommand() {
		Message message = new Message("invalid", pkg);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).remove(pkg);
	}
	
	@Test
	public void remove_invalidMessage_invalidPackage() {
		Message message = new Message(RemoveCommand.NAME, "");
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).remove(pkg);
	}
	
	@Test
	public void remove_successful() {
		Message message = new Message(RemoveCommand.NAME, pkg);
		when(index.remove(pkg)).thenReturn(true);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.OK, response);
	}
	
	@Test
	public void remove_fail() {
		Message message = new Message(RemoveCommand.NAME, pkg);
		when(index.remove(pkg)).thenReturn(false);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.FAIL, response);
	}
}
