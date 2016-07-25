package dopidx.command;

import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * Base class for all {@link Command} implementations.
 * 
 * Provides general validation to incoming messages,
 * wraps the execution and delegates to the subclass 
 * to do the actual execution work.
 *
 */
public abstract class BaseCommand implements Command {
	
	private final Index index;
	
	public BaseCommand(Index index) {
		this.index = index;
	}
	
	/**
	 * Entry point for all command execution.  Provides
	 * validation of the incoming message, then delegates
	 * to {@link BaseCommand.executeCommand} for the 
	 * subclass to do the work.
	 * 
	 * Returns {@link ResponseCode.ERROR} if message validation
	 * fails, otherwise returns the {link ResponseCode} returned
	 * by the subclass.
	 */
	public final ResponseCode execute(Message message) {
		if (!validate(message)) { 
			return ResponseCode.ERROR;
		}
		
		return executeCommand(message);
	}
	
	/**
	 * Validates the incoming message.  Command and package
	 * are required.
	 */
	protected boolean validate(Message message) {
		String command = message.getCommand();
		if (!command.equals(getName())) {
			return false;
		}
		
		String pkg = message.getPackage();
		if (pkg == null || "".equals(pkg)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Where the actual command execution takes place.
	 */
	abstract protected ResponseCode executeCommand(Message message);
	
	/**
	 * Returns the index for commands to interact with.
	 */
	protected Index getIndex() {
		return index;
	}

}
