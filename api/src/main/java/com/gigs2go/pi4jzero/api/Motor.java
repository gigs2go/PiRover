package com.gigs2go.pi4jzero.api;
/**
 * Basic controls for a Motor.
 * 
 * @author tim
 *
 */
public interface Motor {
    /** 
     * Set the motor to go forwards.
     * 
     * @param value The value to set
     */
    void forward( int value );
    /** 
     * Set the motor to go backwards.
     * 
     * @param value The value to set
     */
    void backward( int value );
    /**
     * Stop the motor
     */
    void stop();
    /**
     * Set motor to full speed
     */
    void full();
    /**
     * Get the maximum value to use for 'speed'
     */
    int getFullSpeed();
}
