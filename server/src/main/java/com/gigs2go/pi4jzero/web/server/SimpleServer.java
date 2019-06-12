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
import com.gigs2go.pi4jzero.web.server.api.RequestHandler;
import com.gigs2go.pi4jzero.web.server.api.Response;
import com.gigs2go.pi4jzero.web.server.api.ResponseHandler;
import com.gigs2go.pi4jzero.web.server.impl.RequestHandlerImpl;
import com.gigs2go.pi4jzero.web.server.impl.ResponseHandlerImpl;

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
		if (val == 1) {
			this.controller = new PCPlatformController();
		} else {
			this.controller = new PiPlatformController();
		}
		controller.initialise();
	}

	private void listen(int port) throws IOException {
		RequestHandler requestHandler = new RequestHandlerImpl( controller);
		ResponseHandler responseHandler = new ResponseHandlerImpl();
		ServerSocket serverSocket = new java.net.ServerSocket(port);
		boolean doContinue = true;
		while (doContinue) {
			try {
				LOG.info("Listening on port {}", port);
				Response response = responseHandler.handleResponse(requestHandler.handleRequest(serverSocket.accept()));
				LOG.info(response.toString());
			} catch (Exception e) {
				e.printStackTrace();
				doContinue = false;
			}
		}

		if (serverSocket != null) {
			serverSocket.close();
		}
	}

}
