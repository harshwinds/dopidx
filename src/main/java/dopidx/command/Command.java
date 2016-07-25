package dopidx.command;

import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * Command executable by a client via a {@link Message}
 */
public interface Command {
	/**
	 * Returns the name of the command
	 */
	String getName();
	
	/**
	 * Executes this command using the parameters specified by the 
	 * {@link Message} and returns the appropriate {@link ResponseCode}
	 */
	ResponseCode execute(Message message);
}
