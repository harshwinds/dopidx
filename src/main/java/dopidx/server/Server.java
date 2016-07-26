package dopidx.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import dopidx.command.CommandInvoker;
import dopidx.message.MessageParser;

/**
 * TCP Server that listens for messages for the package indexer.
 */
public class Server implements Runnable {
	
	protected final int port;
	protected final MessageParser messageParser;
	protected final CommandInvoker commandInvoker;
	
	protected AtomicBoolean initializing = new AtomicBoolean(false);
	protected AtomicBoolean running = new AtomicBoolean(false);
	protected ServerSocket socket;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(100);
	
	public Server(int port, MessageParser messageParser, CommandInvoker commandInvoker) {
		this.port = port;
		this.messageParser = messageParser;
		this.commandInvoker = commandInvoker;
	}

	/**
	 * Runs the server
	 */
	public void run() {
		Socket client = null;
		
		try {
			start();
			
			while (isRunning()) {
				try {
					client = socket.accept();
					this.threadPool.execute((new Worker(client, messageParser, commandInvoker))); // Worker handles closing individual Sockets
				} catch (IOException e) {
					if (isRunning()) {
						throw new RuntimeException("Failed to accept connection", e);	
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error", e);
		} finally {
			this.threadPool.shutdown();
			
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// OK. Cleaning up
				}
			}
			
			stop();
		}
	}
	
	/**
	 * Indicates if the server is running
	 */
	public boolean isRunning() {
		return running.get();
	}
	
	/**
	 * Starts the server on the specified port
	 */
	protected void start() {
		if (!running.get() && initializing.compareAndSet(false, true)) {
			try {
				this.socket = new ServerSocket(port);
				running.set(true);
			} catch (IOException e) {
				throw new RuntimeException("Failed to start server", e);
			} finally {
				initializing.set(false);
			}
		}
	}
	
	/**
	 * Stops the server
	 */
	public void stop() {
		if (running.compareAndSet(true, false)) {
			try {
				this.socket.close();
			} catch (IOException e) {
				running.set(true);
				throw new RuntimeException("Failed to stop server", e);
			}
		}
	}
}
