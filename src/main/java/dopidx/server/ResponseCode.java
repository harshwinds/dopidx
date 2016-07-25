package dopidx.server;

/**
 * Response codes returned in response to a message from a client
 */
public enum ResponseCode {
	/**
	 * {@link Command} was executed successfully or did not require executing
	 */
	OK,
	
	/**
	 * {@link Command} failed to execute
	 */
	FAIL,
	
	/**
	 * {@link Command} was not recognized
	 */
	ERROR;
}
