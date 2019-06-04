package com.gigs2go.pi4jzero.web.controller;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.MotorPlatform;

public abstract class PlatformController {
	private static final Logger LOG = LoggerFactory.getLogger(PlatformController.class);
	private MotorPlatform platform;
	
	public abstract void initialise();

	public int handle(String command, String value) {
		int result = 200;

		switch ( command ) {
		case "stop":
			stop();
			break;
		case "center":
			center();
			break;
		case "speed":
			speed(new Integer(value));
			break;
		case "steer":
			steer(new Integer(value));
			break;
		}

		return result;
	}

	protected void setPlatform(MotorPlatform motorPlatform) {
		this.platform = motorPlatform;
	}

	protected Properties loadProperties() {
		Properties props = new Properties();
		try {
			props.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
		} catch (Exception e) {
			LOG.error("No properties", e);
		}
		return props;
	}

	public String stop() {
		platform.stop();
		return "Stopped";
	}

	public String center() {
		platform.center();
		return "Centered";
	}

	public String speed(int value) {
		if (value < 0) {
			platform.backward(-value);
		} else if (value > 0) {
			platform.forward(value);
		} else {
			platform.stop();
		}
		return "Speed";
	}

	public String steer(int value) {
		if (value < 0) {
			platform.left(-value);
		} else if (value > 0) {
			platform.right(value);
		} else {
			platform.center();
		}
		return "Steer";
	}
}
