package com.gigs2go.pi4jzero.api;

public interface DeviceProvider {
    DigitalOutputDevice getDigitalOutputDevice( int channel );
    PwmDevice getPwmDevice( int channel );

}
