package com.gigs2go.pi4jzero.devices;

/**
 * Map from the API to the Pi4J implementation classes.
 */
import java.io.IOException;
import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DeviceProvider;
import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.PwmDevice;
import com.pi4j.gpio.extension.pca.PCA9685GpioProvider;
import com.pi4j.gpio.extension.pca.PCA9685Pin;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinPwmOutput;
import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class PCA9685 implements DeviceProvider {
    private static final Logger LOG = LoggerFactory.getLogger( PCA9685.class );
    private static final int MAX_VALUE = 4096;
    private I2CBus bus = null;
    private GpioPinPwmOutput[] outputChannels;
    private PCA9685GpioProvider provider;
    private int address;
    private int frequency;

    /**
     * 
     * @param address
     * @param frequency
     * @throws IOException
     */
    public PCA9685( int address, int frequency ) throws IOException {
        try {
            bus = I2CFactory.getInstance( I2CBus.BUS_1 );
        } catch ( UnsupportedBusNumberException e ) {
            LOG.error( "Bad BUS", e );
            throw new IOException( e );
        }
        BigDecimal fFrequency = new BigDecimal( frequency );

        this.address = address;
        this.frequency = frequency;
        this.provider = new PCA9685GpioProvider( bus, address, fFrequency );
        // Define outputs
        this.outputChannels = provisionPwmOutputs( provider );
        // Reset outputs
        provider.reset();
    }
    
    @Override
    public DigitalOutputDevice getDigitalOutputDevice( int channel ) {
        return new PCA9685DigitalOutputDeviceImpl( provider, outputChannels[channel] );
    }

    @Override
    public PwmDevice getPwmDevice( int channel ) {
        return new PCA9685PwmDeviceImpl( provider, outputChannels[channel], MAX_VALUE );
    }
    
    private static GpioPinPwmOutput[] provisionPwmOutputs( final PCA9685GpioProvider gpioProvider ) {
        GpioController gpio = GpioFactory.getInstance();
		GpioPinPwmOutput myOutputs[] = { 
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_00, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_01, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_02, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_03, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_04, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_05, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_06, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_07, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_08, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_09, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_10, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_11, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_12, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_13, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_14, "Available"),
				gpio.provisionPwmOutputPin(gpioProvider, PCA9685Pin.PWM_15, "Available" ) };
        return myOutputs;
    }

    @Override
    public String toString() {
        return "PCA9685 [address=" + address + ", frequency=" + frequency + "]";
    }

}
