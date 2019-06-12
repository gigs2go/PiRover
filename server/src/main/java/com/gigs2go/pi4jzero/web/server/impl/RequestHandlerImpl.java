package com.gigs2go.pi4jzero.web.server.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.web.controller.PlatformController;
import com.gigs2go.pi4jzero.web.server.api.RequestHandler;
import com.gigs2go.pi4jzero.web.server.api.Response;

public class RequestHandlerImpl implements RequestHandler {
	private static final Logger LOG = LoggerFactory.getLogger(RequestHandlerImpl.class);
	private Socket socket;
	private PlatformController controller;

	public RequestHandlerImpl(PlatformController controller/*, Socket socket*/) {
		this.controller = controller;
		this.socket = socket;
	}

	@Override
	public Response handleRequest( Socket socket ) throws IOException {
		Response result = new Response(socket);
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
			}
		}
		return result;
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
			LOG.info("Command error", e);
			result = null;
		}
		return result;
	}

}
