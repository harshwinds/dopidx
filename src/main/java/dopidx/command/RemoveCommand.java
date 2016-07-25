package dopidx.command;

import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * A {@link Command} that removes a package from the index.
 * 
 * The package will only be removed if it does not have any dependencies.
 */
public class RemoveCommand extends BaseCommand implements Command {
	
	protected static final String NAME = "REMOVE";
	
	public RemoveCommand(Index index) {
		super(index);
	}
	
	/**
	 * Returns the name of this command
	 */
	public String getName() {
		return NAME;
	}

	/**
	 * Sends the package to the index to be removed.
	 * Returns {@link ResponseCode.OK} if the package was successfully
	 * removed or {@link ResponseCode.Fail} if the removal failed.
	 */
	protected ResponseCode executeCommand(Message message) {
		if (getIndex().remove(message.getPackage())) {
			return ResponseCode.OK;
		} else {
			return ResponseCode.FAIL;
		}
	}
}
