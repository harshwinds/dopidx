package dopidx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import dopidx.command.CommandInvoker;
import dopidx.message.Message;
import dopidx.message.MessageParser;

/**
 * Thread to handle each client connected to the {@link Server}
 */
public class Worker implements Runnable {
	
	private static final Logger LOG = Logger.getLogger(Worker.class.getName());
	
	private final Socket client;
	private final MessageParser messageParser;
	private final CommandInvoker commandInvoker;
	
	public Worker(Socket client, MessageParser messageParser, CommandInvoker commandInvoker) {
		this.client = client;
		this.messageParser = messageParser;
		this.commandInvoker = commandInvoker;
	}
	
	/**
	 * Process each message sent from the client, sending the parsed {@link Message}
	 * to the {@link CommandInvoker} and send the command response back to the client.
	 */
	public void run() {
		UUID clientId = UUID.randomUUID();
		LOG.log(Level.INFO, "Handling requests from client " + clientId.toString());
		
		try (InputStream input = client.getInputStream();
			 Scanner scanner = new Scanner(input, StandardCharsets.UTF_8.name());
			 PrintWriter output = new PrintWriter(client.getOutputStream(), true);) {
			
			scanner.useDelimiter("\\n");
			
			while (scanner.hasNext()) {
				UUID requestId = UUID.randomUUID();
				
				String messageStr = scanner.nextLine();
				LOG.log(Level.INFO, clientId.toString() + ": " + requestId.toString() + ": " + messageStr);
				Message message = messageParser.parseMessage(messageStr);
				LOG.log(Level.INFO, clientId.toString() + ": " + requestId.toString() + ": " + message);
				
				ResponseCode response = execute(message);
				LOG.log(Level.INFO, clientId.toString() + ": " + requestId.toString() + ": " + response);
				output.println(response.name());
			}
		} catch (IOException e) {
			LOG.log(Level.SEVERE, clientId.toString(), e);
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// OK. Cleaning up
				}
			}
		}
		
		LOG.log(Level.INFO, "Finished processing requests from client " + clientId.toString());
	}
	
	protected ResponseCode execute(Message message) {
		if (message == null) {
			return ResponseCode.ERROR;
		}
		
		return commandInvoker.invoke(message);
	}
}
