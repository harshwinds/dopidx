package dopidx.index;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of {@link Index} that stores the index in application memory.
 * 
 * Not a valid implementation for a multi-node environment or an index that must be 
 * maintained between restarts.  To be used for development and testing purposes only.
 */
public class InMemoryIndex implements Index {

	private final Map<String, List<String>> index;
	
	public InMemoryIndex() {
		index = Collections.synchronizedMap(new HashMap<String, List<String>>());
	}

	/**
	 * Adds a package to the index.
	 * 
	 * Indicates success if package already exists in the index.
	 * Package will only be added if all of its dependencies are already present.
	 */
	public boolean add(String pkg, List<String> dependencies) {
		synchronized (index) {
			if (index.containsKey(pkg)) {
				return true;
			}
			
			if (dependencies != null) {
				for (String dependency : dependencies) {
					if (!index.containsKey(dependency)) {
						return false;
					}
				}
			}
			
			index.put(pkg, dependencies);
			return true;
		}
	}

	/**
	 * Indicates if the package exists in the index
	 */
	public boolean exists(String pkg) {
		return index.containsKey(pkg);
	}

	/**
	 * Removes the package from the index.
	 * 
	 * Package can only be removed if there are no dependencies on the package.
	 */
	public boolean remove(String pkg) {
		synchronized (index) {
			if (!index.containsKey(pkg)) {
				return true;
			}
			
			for (List<String> dependencies : index.values()) {
				if (dependencies != null) {
					if (dependencies.contains(pkg)) {
						return false;
					}
				}
			}

			index.remove(pkg);
			return true;
		}
	}

}
