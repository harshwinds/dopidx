package dopidx.command;

import dopidx.index.Index;
import dopidx.message.Message;
import dopidx.server.ResponseCode;

/**
 * A {@link Command} that indicates if a package has been indexed
 */
public class QueryCommand extends BaseCommand implements Command {
	
	protected static final String NAME = "QUERY";
	
	public QueryCommand(Index index) {
		super(index);
	}
	
	/**
	 * Returns the name of this command
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * Queries the index to determine if the package has been indexed.
	 * Returns {@link ResponseCode.OK} if the package is in the index,
	 * otherwise {@link ResponseCode.Fail}.
	 */
	protected ResponseCode executeCommand(Message message) {
		if (getIndex().exists(message.getPackage())) {
			return ResponseCode.OK;
		} else {
			return ResponseCode.FAIL;
		}
	}
}
