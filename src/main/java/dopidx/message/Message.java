package dopidx.message;

import java.util.List;

/**
 * Represents a message sent by the client.
 * 
 * Command and package is required.
 */
public class Message {
	private String command;
	private String pkg;
	private List<String> dependencies;
	
	public Message(String command, String pkg) {
		this.command = command;
		this.pkg = pkg;
	}
	
	public Message(String command, String pkg, List<String> dependencies) {
		this(command, pkg);
		this.dependencies = dependencies;
	}
	
	/**
	 * Command requested by the client
	 */
	public String getCommand() {
		return command;
	}
	
	/**
	 * Package being acted upon
	 */
	public String getPackage() {
		return pkg;
	}
	
	/**
	 * List of dependencies of the package
	 */
	public List<String> getDependencies() {
		return dependencies;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (!(o instanceof Message)) {
			return false;
		}
		
		Message that = (Message) o;
		return this.command == null ? that.command == null : this.command.equals(that.command) &&
				this.pkg == null ? that.pkg == null : this.pkg.equals(that.pkg) &&
				this.dependencies == null ? that.dependencies == null : this.dependencies.equals(that.dependencies);
	}
	
	@Override
	public int hashCode() {
		return (this.command == null ? 42 : this.command.hashCode() * 42) +
				(this.pkg == null ? 42 : this.pkg.hashCode() * 42) +
				(this.dependencies == null ? 42 : this.dependencies.hashCode() * 42);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(command)
				.append("|")
				.append(pkg)
				.append("|")
				.append(dependencies);
		
		return sb.toString();
	}
}
