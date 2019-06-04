package com.gigs2go.pi4jzero.api;
/**
 * Set the PWM value - determines the on/off cycle.
 * 
 * @author tim
 *
 */
public interface PwmDevice extends Device {
	/**
	 * Set the PWM value - determines the on/off cycle.
	 * @param onValue Value to set. Must be positive and in the range specified by the device it is controlling. 
	 */
    void setPwm( int onValue );
    
    /**
     * Get the upper value of the accepted values.
     */
    int getFullSpeed();
}
