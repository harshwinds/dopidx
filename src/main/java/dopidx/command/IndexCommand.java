package dopidx.command;

import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * A {@link Command} that indexes a package.  The package's dependencies
 * must be indexed before the package can be indexed.
 */
public class IndexCommand extends BaseCommand implements Command {
	
	protected static final String NAME = "INDEX";
	
	public IndexCommand(Index index) {
		super(index);
	}
	
	/**
	 * Returns the name of this command
	 */
	public String getName() {
		return NAME;
	}

	/**
	 * Sends the package and its dependencies to the index for indexing.
	 * Returns {@link ResponseCode.OK} if the indexing was successful or
	 * {@link ResponseCode.Fail} if the indexing failed.
	 */
	protected ResponseCode executeCommand(Message message) {
		if (getIndex().add(message.getPackage(), message.getDependencies())) {
			return ResponseCode.OK;
		} else {
			return ResponseCode.FAIL;
		}
	}
}
