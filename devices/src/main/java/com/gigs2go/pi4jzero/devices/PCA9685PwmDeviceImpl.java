package com.gigs2go.pi4jzero.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.PwmDevice;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.io.gpio.GpioPinPwmOutput;

public class PCA9685PwmDeviceImpl extends PCA9685DigitalOutputDeviceImpl implements PwmDevice {
    private static final Logger LOG = LoggerFactory.getLogger( PCA9685PwmDeviceImpl.class );
    
    private int range;

    public PCA9685PwmDeviceImpl( PCA9685GpioProvider provider, GpioPinPwmOutput gpioPinPwmOutput, int range ) {
        super( provider, gpioPinPwmOutput );
        this.range = range;
    }
    
    @Override
    public void setPwm( int onValue ) {
        LOG.debug( "SetPwm : '{}'", onValue );
        provider.setPwm( pin.getPin(), 0, onValue );
    }

	@Override
	public int getFullSpeed() {
		return range;
	}

}
