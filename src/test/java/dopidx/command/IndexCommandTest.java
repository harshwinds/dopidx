package dopidx.command;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.command.IndexCommand;
import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

@RunWith(MockitoJUnitRunner.class)
public class IndexCommandTest {
	
	// Class under test
	private IndexCommand command;
	
	@Mock private Index index;
	
	private String pkg = "cloog";
	private String dep1 = "gmp";
	private String dep2 = "isl";
	private String dep3 = "pkg-config";
	private List<String> dependencies;
	
	@Before
	public void setup() throws Exception {
		command = new IndexCommand(index);
		
		dependencies = new ArrayList<String>();
		dependencies.add(dep1);
		dependencies.add(dep2);
		dependencies.add(dep3);
	}

	@Test
	public void index_invalidMessage_invalidCommand() {
		Message message = new Message("invalid", pkg);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).exists(pkg);
	}
	
	@Test
	public void index_invalidMessage_invalidPackage() {
		Message message = new Message(IndexCommand.NAME, "");
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.ERROR, response);
		verify(index, never()).exists(pkg);
	}
	
	@Test
	public void index_noDependencies_success() {
		Message message = new Message(IndexCommand.NAME, pkg);
		when(index.add(pkg, null)).thenReturn(true);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.OK, response);
	}
	
	@Test
	public void index_noDependencies_fail() {
		Message message = new Message(IndexCommand.NAME, pkg);
		when(index.add(pkg, null)).thenReturn(false);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.FAIL, response);
	}
	
	@Test
	public void index_withDependencies_success() {
		Message message = new Message(IndexCommand.NAME, pkg, dependencies);
		when(index.add(pkg, dependencies)).thenReturn(true);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.OK, response);
	}
	
	@Test
	public void index_withDependencies_fail() {
		Message message = new Message(IndexCommand.NAME, pkg, dependencies);
		when(index.add(pkg, dependencies)).thenReturn(false);
		
		ResponseCode response = command.execute(message);
		
		assertEquals(ResponseCode.FAIL, response);
	}
}
