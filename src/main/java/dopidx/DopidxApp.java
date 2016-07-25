package dopidx;

import dopidx.command.CommandInvoker;
import dopidx.command.IndexCommand;
import dopidx.command.QueryCommand;
import dopidx.command.RemoveCommand;
import dopidx.index.InMemoryIndex;
import dopidx.index.Index;
import dopidx.message.MessageParser;
import dopidx.server.Server;

/**
 * Main application class for the dopidx application.
 */
public class DopidxApp {
	
    public static void main(String[] args) {
        System.out.println("Initializing dopidx...");
        
        System.out.println("...Configuring available commands...");
        Index index = new InMemoryIndex(); 
        CommandInvoker commandInvoker = new CommandInvoker(new IndexCommand(index), new QueryCommand(index), new RemoveCommand(index));
        System.out.println("......commands successfully configured.");
        
        System.out.println("...Starting server...");
        MessageParser messageParser = new MessageParser();
        final Server server = new Server(8080, messageParser, commandInvoker);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    Thread.sleep(200);
                    System.out.println("Stopping server ...");
                    server.stop();
                    System.out.println("...server stopped successfully.");
                } catch (Exception e) {
                    System.out.println("...problem stopping server: " + e.getMessage());
                }
            }
        });

        
        try {
        	new Thread(server).start();
        	System.out.println("......server successfully started.");
        } catch (Exception e) {
        	System.out.println("ERROR: " + e.getMessage());
        }
        System.out.println("...dopidx successfully initialized.");
    }
}
