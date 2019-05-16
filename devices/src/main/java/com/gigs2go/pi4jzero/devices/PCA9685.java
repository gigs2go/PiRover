package com.gigs2go.pi4jzero.devices;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DeviceProvider;
import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.MotorProvider;
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
    private I2CBus bus = null;
    private GpioPinPwmOutput[] outputChannels;
    private PCA9685GpioProvider provider;
    private int address;
    private int frequency;

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
    
//    public MotorProvider getDualPwmMotorProvider( int enable ) {
//        return new DualPwmMotorImpl( this, enable );
//    }

    public DigitalOutputDevice getDigitalOutputDevice( int channel ) {
        return new PCA9685DigitalOutputDeviceImpl( provider, outputChannels[channel] );
    }

    public PwmDevice getPwmDevice( int channel ) {
        return new PCA9685PwmDeviceImpl( provider, outputChannels[channel] );
    }
    
    private static GpioPinPwmOutput[] provisionPwmOutputs( final PCA9685GpioProvider gpioProvider ) {
        GpioController gpio = GpioFactory.getInstance();
        GpioPinPwmOutput myOutputs[] = { gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_00, "Motor1 PWM" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_01, "Motor1 InA" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_02, "Motor1 InB" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_03, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_04, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_05, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_06, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_07, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_08, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_09, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_10, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_11, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_12, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_13, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_14, "Unused" ), gpio.provisionPwmOutputPin( gpioProvider, PCA9685Pin.PWM_15, "Unused" ) };
        return myOutputs;
    }

    public Motor getPwmMotor( String name, int pwm, int inA, int inB ) {
        return new PwmMotorImpl( name, this.getPwmDevice( pwm ), this.getDigitalOutputDevice( inA ), this.getDigitalOutputDevice( inB ) );
    }

    @Override
    public String toString() {
        return "PCA9685 [address=" + address + ", frequency=" + frequency + "]";
    }

}
