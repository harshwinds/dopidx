package dopidx.index;

import java.util.List;

/**
 * A package indexer for keeping track of packages and their dependencies.
 */
public interface Index {
	
	/**
	 * Adds a package with optional dependencies to the index
	 */
	boolean add(String pkg, List<String> dependencies);

	/**
	 * Checks to see if a package exists in the index
	 */
	boolean exists(String pkg);
	
	/**
	 * Removes the package from the index
	 */
	boolean remove(String pkg);
}
