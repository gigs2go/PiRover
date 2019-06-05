package com.gigs2go.pi4jzero.web.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.web.controller.PCPlatformController;
import com.gigs2go.pi4jzero.web.controller.PiPlatformController;
import com.gigs2go.pi4jzero.web.controller.PlatformController;

public class SimpleServer {
	private static final Logger LOG = LoggerFactory.getLogger(SimpleServer.class);

	private PlatformController controller = null;

	private SimpleServer() {
		super();
	}

	public static void main(String[] args) {

		SimpleServer server = new SimpleServer();
		try {
			int controller = 1;
			if (args.length > 0) {
				controller = new Integer(args[0]);
				LOG.info("Using Controller : '{}'", controller);
			}
			server.initialise(controller);
			server.listen(8090);
		} catch (Exception e) {
			LOG.error("Failed", e);
		}
	}

	private void initialise(int val) {
		if (val == 0) {
			this.controller = new PiPlatformController();
		} else {
			this.controller = new PCPlatformController();
		}
		controller.initialise();
	}

	private void listen(int port) throws IOException {
		Socket socket = null;
		ServerSocket serverSocket = new java.net.ServerSocket(port);
		boolean doContinue = true;
		while (doContinue) {
			try {
				LOG.info("Listening on port {}", port);
				socket = serverSocket.accept(); // Blocks
				boolean ok = handleRequest(socket);
				handleResponse(socket, ok);
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				doContinue = false;
			}
		}

		if (serverSocket != null) {
			serverSocket.close();
		}
	}

	private boolean handleRequest(Socket socket) throws IOException {
		boolean result = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String line = null;
		String firstLine = null;
		line = reader.readLine();
		firstLine = line;
		while (line != null && line.length() > 0) {
			LOG.debug("Got '{}'", line);
			line = reader.readLine();
		}
		LOG.debug("Handling? : '{}'", firstLine);
		if (firstLine != null) {
			String[] commandElements = getCommand(firstLine.split(" ")[1]);
			if (commandElements != null) {
				LOG.info("Handling ... '{}', '{}'", commandElements[0], commandElements[1]);
				controller.handle(commandElements[0], commandElements[1]);
				result = true;
			}
		}
		return result;
	}

	private void handleResponse(Socket socket, boolean ok) throws UnsupportedEncodingException, IOException {
		LOG.debug("Handling Response : OK is {}", ok);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		if (ok) {
			writer.write("HTTP/1.1 200 Done it!\n");
		} else {
			writer.write("HTTP/1.1 404 Didn't do it!\n");
		}
		// writer.write("Content-Type: text/html; charset=utf8\n");
		writer.write("\n");
		writer.flush();
		writer.close();
	}

	// Simple one
	// Expected URI is of the form '/rover/platform/<cmd>?value=<val>
	// where <cmd> is one of speed|steer|stop|center
	// and, for speed/steer, there must be a 'value' parameter with an int-valid
	// string
	// If it fails these checks, result will be null
	private String[] getCommand(String uriString) {
		String[] result = new String[2];
		URI uri = null;
		try {
			uri = new URI(uriString);
			// Command
			String[] path = uri.getPath().split("/");
			// Must be ours ...
			if (!(path[1].equals("platform") && path[2].equals("rover") && path.length == 4)) {
				throw new Exception("Invalid Path : " + uri.getPath());
			}
			result[0] = path[3];
			result[1] = null; // Safe
			// Params?
			if (result[0].equals("speed") || result[0].equals("steer")) {
				String queryString = uri.getQuery();
				if (queryString != null) {
					String[] query = uri.getQuery().split("=");
					if (!(query[0].equals("value") && query.length == 2)) {
						throw new Exception("Invalid Query : " + uri.getQuery());
					}
					result[1] = query[1];
				}
				if (result[1] == null) {
					throw new Exception("Invalid Params : " + uri.getPath());
				} else {
					Integer.valueOf(result[1]);
				}
			} else if (!result[0].equals("stop") && !result[0].equals("center")) {
				throw new Exception("Invalid Command : " + result[0]);
			}
		} catch (Exception e) {
			LOG.info("Command error", e );
			result = null;
		}
		return result;
	}

}
