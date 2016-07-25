package dopidx.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.Command;
import dopidx.command.CommandInvoker;
import dopidx.command.IndexCommand;
import dopidx.command.QueryCommand;
import dopidx.command.RemoveCommand;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

@RunWith(MockitoJUnitRunner.class)
public class CommandInvokerTest {
	
	// Class under test
	private CommandInvoker commandInvoker;
	
	@Mock private IndexCommand indexCommand;
	@Mock private QueryCommand queryCommand;
	@Mock private RemoveCommand removeCommand;

	@Before
	public void setup() throws Exception {
		when(indexCommand.getName()).thenReturn(IndexCommand.NAME);
		when(queryCommand.getName()).thenReturn(QueryCommand.NAME);
		when(removeCommand.getName()).thenReturn(RemoveCommand.NAME);
	}
	
	@Test
	public void retrieveCommand_Index() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		
		Command command = commandInvoker.retrieveCommand(IndexCommand.NAME);
		assertTrue(command instanceof IndexCommand);
	}
	
	@Test
	public void retrieveCommand_Query() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		
		Command command = commandInvoker.retrieveCommand(QueryCommand.NAME);
		assertTrue(command instanceof QueryCommand);
	}
	
	@Test
	public void retrieveCommand_Remove() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		
		Command command = commandInvoker.retrieveCommand(RemoveCommand.NAME);
		assertTrue(command instanceof RemoveCommand);
	}
	
	@Test
	public void retrieveCommand_Unknown() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		
		Command command = commandInvoker.retrieveCommand("foo");
		assertNull(command);
	}
	
	@Test
	public void retrieveCommand_noCommands() {
		commandInvoker = new CommandInvoker();
		
		Command command = commandInvoker.retrieveCommand(IndexCommand.NAME);
		assertNull(command);
	}
	
	@Test
	public void invoke_unknownCommand() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		Message message = new Message("UNKNOWN", "cloog");
		
		ResponseCode response = commandInvoker.invoke(message);
		assertEquals(ResponseCode.ERROR, response);
	}
	
	@Test
	public void invoke_success() {
		commandInvoker = new CommandInvoker(indexCommand, queryCommand, removeCommand);
		Message message = new Message("INDEX", "cloog");
		when(indexCommand.validate(message)).thenReturn(true);
		when(indexCommand.execute(message)).thenReturn(ResponseCode.OK);
		
		ResponseCode response = commandInvoker.invoke(message);
		assertEquals(ResponseCode.OK, response);
	}
}
