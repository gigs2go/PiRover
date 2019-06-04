package com.gigs2go.pi4jzero.api;

/**
 * Provides specific types of device.
 * 
 * @author tim
 *
 */
public interface DeviceProvider {
	/**
	 * Configure the controller on the given channel (pin)
	 * 
	 * @param channel The channel to configure
	 * @return The device
	 */
    DigitalOutputDevice getDigitalOutputDevice( int channel );
	/**
	 * Configure the controller on the given channel (pin)
	 * 
	 * @param channel The channel to configure
	 * @return The device
	 */
    PwmDevice getPwmDevice( int channel );
}
