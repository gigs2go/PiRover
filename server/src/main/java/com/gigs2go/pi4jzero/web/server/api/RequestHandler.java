package com.gigs2go.pi4jzero.web.server.api;

import java.io.IOException;
import java.net.Socket;

public interface RequestHandler {
	Response handleRequest( Socket socket ) throws IOException;
}
