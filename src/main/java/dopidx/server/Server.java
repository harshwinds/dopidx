package dopidx.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dopidx.command.CommandInvoker;
import dopidx.message.MessageParser;

/**
 * TCP Server that listens for messages for the package indexer.
 */
public class Server implements Runnable {
	
	protected final int port;
	protected final MessageParser messageParser;
	protected final CommandInvoker commandInvoker;
	
	protected boolean running;
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
		try {
			start();
			
			while (isRunning()) {
				Socket client = null;
				try {
					client = socket.accept();
					this.threadPool.execute((new Worker(client, messageParser, commandInvoker)));
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
			stop();
		}
	}
	
	/**
	 * Indicates if the server is running
	 */
	public synchronized boolean isRunning() {
		return running;
	}
	
	/**
	 * Starts the server on the specified port
	 */
	protected synchronized void start() {
		try {
			this.socket = new ServerSocket(port);
			this.running = true;
		} catch (IOException e) {
			throw new RuntimeException("Failed to start server", e);
		}
	}
	
	/**
	 * Stops the server
	 */
	public synchronized void stop() {
		if  (running) {
			try {
				this.socket.close();
				this.running = false;
			} catch (IOException e) {
				throw new RuntimeException("Failed to stop server", e);
			}
		}
	}
}
