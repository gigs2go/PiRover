package com.gigs2go.pi4jzero.web.server.api;

import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.web.server.impl.RequestHandlerImpl;

public class Response {
	private static final Logger LOG = LoggerFactory.getLogger(RequestHandlerImpl.class);
	private Socket socket;
	private boolean status = true;

	public Response( Socket socket ) {
		this.socket = socket;
		LOG.debug("Created Response");
	}

	public Socket getSocket() {
		return socket;
	}
	
	public boolean getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "Response [socket connected=" + socket.isConnected() + ", status=" + status + "]";
	}
	
	

}
