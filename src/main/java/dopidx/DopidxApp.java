package dopidx;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	private static final Logger LOG = Logger.getLogger(DopidxApp.class.getName());
	
    public static void main(String[] args) {    	
        LOG.log(Level.INFO, "Initializing dopidx...");
        
        LOG.log(Level.INFO, "...Configuring available commands...");
        Index index = new InMemoryIndex(); 
        CommandInvoker commandInvoker = new CommandInvoker(new IndexCommand(index), new QueryCommand(index), new RemoveCommand(index));
        LOG.log(Level.INFO, "......commands successfully configured.");
        
        LOG.log(Level.INFO, "...Starting server...");
        MessageParser messageParser = new MessageParser();
        final Server server = new Server(8080, messageParser, commandInvoker);
        
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                	System.out.println("Stopping server...");
                    Thread.sleep(200);
                    server.stop();
                    System.out.println("...server stopped successfully.");
                } catch (Exception e) {
                	System.out.println("...problem stopping server: " + e.getMessage());
                }
            }
        });

        
        try {
        	new Thread(server).start();
        	LOG.log(Level.INFO, "......server successfully started.");
        } catch (Exception e) {
        	LOG.log(Level.SEVERE, "Server error", e);
        }
        LOG.log(Level.INFO, "...dopidx successfully initialized.");
    }
}
