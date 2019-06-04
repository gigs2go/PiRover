package com.gigs2go.pi4jzero.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class PCA9685DigitalOutputDeviceImpl implements DigitalOutputDevice {
    private static final Logger LOG = LoggerFactory.getLogger( DigitalOutputDevice.class );
    protected GpioPinPwmOutput pin;
    protected PCA9685GpioProvider provider;
    
    PCA9685DigitalOutputDeviceImpl( PCA9685GpioProvider provider, GpioPinPwmOutput pin ) {
        this.pin = pin;
        this.provider = provider;
    }

    @Override
    public void on() {
        LOG.debug( "ON" );
        provider.setAlwaysOn(pin.getPin());
    }

    @Override
    public void off() {
        LOG.debug( "OFF" );
        provider.setAlwaysOff(pin.getPin());
    }

}
