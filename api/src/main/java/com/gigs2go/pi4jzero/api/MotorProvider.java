package com.gigs2go.pi4jzero.api;

/**
 * Create a Motor driven by 1 PWM input and 2 DigitalOutput devices.
 * <p>The PWM input is used to control the speed, and the 2 DigitalOutputDevices
 * provide directional control by being set on/off accordingly.</p>
 * 
 * @author tim
 *
 */
public interface MotorProvider {
	/**
	 * Enable the actual controller.
	 * @param enable
	 */
    void setEnable( DigitalOutputDevice enable );
    /**
     * Create a Motor.
     * The Provider may provide 1 to many different Motors, depending on it's capabilities.
     * @param name The name of the provided Motor
     * @param pwm The PWM Device controlling the speed
     * @param inA One of the DigitalOutputDDevices controlling direction
     * @param inB One of the DigitalOutputDDevices controlling direction
     * @return The Motor that has been created
     */
    Motor createPwmMotor( String name, PwmDevice pwm, DigitalOutputDevice inA, DigitalOutputDevice inB );
}
