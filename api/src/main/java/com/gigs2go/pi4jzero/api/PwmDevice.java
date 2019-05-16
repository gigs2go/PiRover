package com.gigs2go.pi4jzero.api;

public interface PwmDevice extends DigitalOutputDevice {
    void setValue( int value );
    void setPwm( int onValue );
}
