package com.gigs2go.pi4jzero.api;

/**
 * Almost everything is a device.
 * <p>Many devices have enable/disable capability, which is captured here.</p>
 * <p>DigitalOutputDevices are either on or off.</p>
 * @author tim
 *
 */
public interface Device {
	/**
	 * Enable device. If this device does not support this operation, it should do nothing.
	 */
    void on();
	/**
	 * Disable device. If this device does not support this operation, it should do nothing.
	 */
    void off();
}
