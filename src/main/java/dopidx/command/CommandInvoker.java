package dopidx.command;

import java.util.HashMap;
import java.util.Map;

import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * Invokes the {@link Command} requested by the {@link Message}
 */
public class CommandInvoker {
	
	private Map<String, Command> commandMap = new HashMap<String, Command>();

	public CommandInvoker(Command... command) {
		for (int i = 0; i < command.length; i++) {
			commandMap.put(command[i].getName(), command[i]);
		}
	}
	
	/**
	 * Invokes the {@link Command} specified by the {@link Message}
	 * and returns the appropriate {@link ResponseCode}
	 */
	public ResponseCode invoke(Message message) {
		Command command = retrieveCommand(message.getCommand());
		if (command == null) {
			return ResponseCode.ERROR;
		}
		
		ResponseCode responseCode = command.execute(message);
		return responseCode;
	}
	
	/**
	 * Retrieves the {@link Command} implementations that 
	 * corresponds with the specified commandName
	 */
	protected Command retrieveCommand(String commandName) {
		Command command = commandMap.get(commandName);
		return command;
	}
}
