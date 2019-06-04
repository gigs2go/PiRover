package com.gigs2go.pi4jzero.web.controller;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.MotorPlatform;
import com.gigs2go.pi4jzero.api.MotorProvider;
import com.gigs2go.pi4jzero.api.PwmDevice;
import com.gigs2go.pi4jzero.devices.PCA9685;
import com.gigs2go.pi4jzero.devices.TB6612;
import com.gigs2go.pi4jzero.platforms.MotorPlatformImpl;

public class PiPlatformController extends PlatformController {
	private static final Logger LOG = LoggerFactory.getLogger(PiPlatformController.class);
	Properties props;
	private PCA9685 controller;
	private TB6612 mpLeft;
	private TB6612 mpRight;
	Motor flMotor;
	Motor frMotor;
	Motor rlMotor;
	Motor rrMotor;

	@Override
	public void initialise() {
		LOG.info( "Loading");
		props = loadProperties();
		try {
			int i2cAddress = new Integer( props.getProperty("controller.i2c.address") );
			int frequency = new Integer( props.getProperty("controller.frequency") );
			controller = new PCA9685(i2cAddress, frequency);
			// Get motors
			mpLeft = new TB6612("MotorController1"); // Left hand controller
			mpLeft.setEnable( getDigitalOutputDevice( "motor.left.enable" ) );
			flMotor = getMotor(mpLeft, "motor.front.left");
			rlMotor = getMotor(mpLeft, "motor.rear.left");
			mpRight = new TB6612("MotorController2"); // Right hand controller
			mpRight.setEnable( getDigitalOutputDevice( "motor.right.enable" ) );
			frMotor = getMotor(mpRight, "motor.front.right");
			rrMotor = getMotor(mpRight, "motor.rear.right");
			mpLeft.on();
			mpRight.on();
			MotorPlatform platform = new MotorPlatformImpl(flMotor, frMotor, rlMotor, rrMotor, 4096);
			super.setPlatform(platform);
		} catch (IOException e) {
			LOG.error("Error", e);
		}
	}

	private Motor getMotor(MotorProvider mp, String name) {
		Motor result = null;
		PwmDevice pwm = getPwmDevice(name+".pwm");
		DigitalOutputDevice inA = getDigitalOutputDevice(name+".inA");
		DigitalOutputDevice inB = getDigitalOutputDevice(name+".inB");
		result = mp.createPwmMotor(name, pwm, inA, inB);
		return result;
	}
	
	private DigitalOutputDevice getDigitalOutputDevice(String key ) {
		Integer pinNo = new Integer( props.getProperty(key));
		return controller.getDigitalOutputDevice(pinNo);
	}

	private PwmDevice getPwmDevice(String key ) {
		Integer pinNo = new Integer( props.getProperty(key));
		return controller.getPwmDevice(pinNo);
	}

}
