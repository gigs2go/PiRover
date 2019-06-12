package com.gigs2go.pi4jzero.web.server.api;

import java.io.IOException;

public interface ResponseHandler {
	Response handleResponse( Response response ) throws IOException;
}
