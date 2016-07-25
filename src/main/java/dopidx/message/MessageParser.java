package dopidx.message;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the message string sent by the client into a {@link Message} object
 */
public class MessageParser {
	
	private Pattern pattern = Pattern.compile("(.*)\\|(.*)\\|(.*)");

	/**
	 * Apply the message pattern to the message string
	 * and create a {@link Message} object
	 */
	public Message parseMessage(String messageStr) {
		Matcher matcher = pattern.matcher(messageStr);
		if (!matcher.matches()) {
			return null;
		}
		
		if (matcher.groupCount() != 3) {
			return null;
		}
		
		String command = matcher.group(1);
		String pkg = matcher.group(2);
		String dependenciesStr = matcher.group(3);
		
		Message message;
		if (dependenciesStr.isEmpty()) {
			message = new Message(command, pkg);
		} else {
			String[] dependenciesAr = dependenciesStr.split(",");
			List<String> dependencies = Arrays.asList(dependenciesAr);
			
			message = new Message(command, pkg, dependencies);	
		}
		
		return message;
	}
}
