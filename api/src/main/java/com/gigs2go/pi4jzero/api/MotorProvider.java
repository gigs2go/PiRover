package com.gigs2go.pi4jzero.api;

public interface MotorProvider {
    void setEnable( DigitalOutputDevice enable );
    Motor createPwmMotor( String name, PwmDevice pwm, DigitalOutputDevice inA, DigitalOutputDevice inB );
}
