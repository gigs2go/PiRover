package com.gigs2go.pi4jzero.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class PCA9685PwmDeviceImpl extends PCA9685DigitalOutputDeviceImpl implements com.gigs2go.pi4jzero.api.PwmDevice {
    private static final Logger LOG = LoggerFactory.getLogger( PCA9685PwmDeviceImpl.class );

    public PCA9685PwmDeviceImpl( PCA9685GpioProvider provider, GpioPinPwmOutput gpioPinPwmOutput ) {
        super( provider, gpioPinPwmOutput );
    }
    @Override
    public void setValue( int value ) {
        LOG.debug( "SetValue (noop): '{}'", value );
    }

    @Override
    public void setPwm( int onValue ) {
        LOG.debug( "SetPwm : '{}'", onValue );
        provider.setPwm( pin.getPin(), 0, onValue );
    }

}
