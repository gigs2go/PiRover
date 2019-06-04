package com.gigs2go.pi4jzero.web.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.web.controller.PCPlatformController;
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
			server.initialise();
			server.listen();
		} catch (Exception e) {
			LOG.error("Failed", e);
		}
	}

	private void initialise() {
		this.controller = new PCPlatformController();
		controller.initialise();
	}

	private void listen() throws IOException {
		Socket socket = null;
		ServerSocket serverSocket = new java.net.ServerSocket(8725);
		boolean doContinue = true;
		while (doContinue) {
			try {
				socket = serverSocket.accept(); // Blocks
				handleRequest(socket);
				handleResponse(socket);
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

	private void handleRequest(Socket socket) throws IOException {
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
			List<String> commandElements = getCommand(firstLine);
			if (commandElements.size() == 2) {
				controller.handle(commandElements.get(0), commandElements.get(1));
			}
		}
	}

	private List<String> getCommand(String line) {
		List<String> result = new ArrayList<>();
		String[] bitz = line.split(" ");
		if (bitz.length > 1 && bitz[1].startsWith("/rover")) {
			try {
				String uri = bitz[1];
				LOG.debug("Splitting '{}'", uri);
				// '/rover/<cmd>?value=<val>'
				String mine = uri.split("/")[2];
				// '<cmd>?value=<val>
				LOG.debug("Splitting '{}'", mine);
				String[] split1 = mine.split("\\?");
				// <cmd>
				String command = split1[0];
				// value=<val>
				String value = null;
				if (split1.length > 1) {
					LOG.debug("Splitting '{}'", split1[1]);
					value = split1[1].split("=")[1];
				}
				result.add(command);
				result.add(value);
			} catch (Exception e) {
				LOG.error("Failed", e);
				LOG.error("Invalid URI : '{}'", line);
			}
		} else {
			LOG.debug("Nope - not mine");
		}
		return result;
	}

	private void handleResponse(Socket socket) throws UnsupportedEncodingException, IOException {
		LOG.debug("Handling Response");
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		writer.write("HTTP/1.1 200 Done it!\n");
		// writer.write("Content-Type: text/html; charset=utf8\n");
		writer.write("\n");
		writer.flush();
		writer.close();
	}

}
