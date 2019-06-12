package com.gigs2go.pi4jzero.web.server.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.web.server.api.Response;
import com.gigs2go.pi4jzero.web.server.api.ResponseHandler;

public class ResponseHandlerImpl implements ResponseHandler {
	private static final Logger LOG = LoggerFactory.getLogger(ResponseHandlerImpl.class);

	public ResponseHandlerImpl() {
	}

	@Override
	public Response handleResponse(Response response) throws IOException {
		Response result = response;
		LOG.debug("Handling Response : Response is {}", response);
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(response.getSocket().getOutputStream()));
		if (response.getStatus()) {
			writer.write("HTTP/1.1 200 Done it!\n");
		} else {
			writer.write("HTTP/1.1 404 Didn't do it!\n");
		}
		// writer.write("Content-Type: text/html; charset=utf8\n");
		writer.write("\n");
		writer.flush();
		writer.close();
		return result;
	}

}
