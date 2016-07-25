package dopidx.index;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import dopidx.index.InMemoryIndex;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryIndexTest {

	// Class under test
	private InMemoryIndex index;
	
	private String pkg = "cloog";
	private String dep1 = "gmp";
	private String dep2 = "isl";
	private String dep3 = "pkg-config";
	private List<String> noDependencies;
	private List<String> dependencies;
	
	@Before
	public void setup() throws Exception {
		index = new InMemoryIndex();
		
		noDependencies = Collections.emptyList();
		
		dependencies = new ArrayList<String>();
		dependencies.add(dep1);
		dependencies.add(dep2);
		dependencies.add(dep3);
	}
	
	@Test
	public void add_noDependencies() {
		assertTrue(index.add(pkg, noDependencies));
	}
	
	@Test
	public void add_exists() {
		index.add(pkg, noDependencies);
		assertTrue(index.add(pkg, noDependencies));
	}
	
	@Test
	public void add_dependenciesExist() {
		index.add(dep1, noDependencies);
		index.add(dep2, noDependencies);
		index.add(dep3, noDependencies);
		assertTrue(index.add(pkg, dependencies));
	}
	
	@Test
	public void add_dependenciesDoNotExist() {
		assertFalse(index.add(pkg, dependencies));
	}
	
	@Test
	public void exists_false() {
		assertFalse(index.exists(pkg));
	}
	
	@Test
	public void exists_true() {
		index.add(pkg, noDependencies);
		assertTrue(index.exists(pkg));
	}
	
	@Test
	public void remove_doesNotExist() {
		assertTrue(index.remove(pkg));
	}
	
	@Test
	public void remove_exists_notADependency() {
		index.add(pkg, noDependencies);
		assertTrue(index.remove(pkg));
	}
	
	@Test
	public void remove_exists_isADependency() {
		index.add(dep1, noDependencies);
		index.add(dep2, noDependencies);
		index.add(dep3, noDependencies);
		index.add(pkg, dependencies);
		assertFalse(index.remove(dep1));
	}
}
