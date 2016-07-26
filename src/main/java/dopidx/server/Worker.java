package dopidx.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

import dopidx.command.CommandInvoker;
import dopidx.message.Message;
import dopidx.message.MessageParser;

/**
 * Thread to handle each client connected to the {@link Server}
 */
public class Worker implements Runnable {
	
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
		// System.out.println("[Handling requests from client " + clientId.toString() + "]");
		
		InputStream input = null;
		PrintWriter output = null;
		Scanner scanner = null;
		try {
			input = client.getInputStream();
			scanner = new Scanner(input, StandardCharsets.UTF_8.name());
			scanner.useDelimiter("\\n");
			
			output = new PrintWriter(client.getOutputStream(), true);
			while (scanner.hasNext()) {
				// UUID requestId = UUID.randomUUID();
				String messageStr = scanner.nextLine();
				// System.out.println(clientId.toString() + ": " + requestId.toString() + ": " + messageStr);
				Message message = messageParser.parseMessage(messageStr);
				// System.out.println(clientId.toString() + ": " + requestId.toString() + ": " + message);
				
				ResponseCode response = execute(message);
				// System.out.println(clientId.toString() + ": " + requestId.toString() + ": " + response);
				output.println(response.name());
			}
		} catch (IOException e) {
			System.out.println("ERROR: " + clientId.toString() + ": " + e.getMessage());
		} finally {
			if (output != null) {
				output.close();
			}
			
			if (scanner != null) {
				scanner.close(); // also closes input
			}
			
			try {
				client.close();
			} catch (IOException e) {
				// Ok.  Cleaning up
			}
		}
		
		// System.out.println("[Finished processing requests from client " + clientId.toString() + "]");
	}
	
	protected ResponseCode execute(Message message) {
		if (message == null) {
			return ResponseCode.ERROR;
		}
		
		return commandInvoker.invoke(message);
	}
}
