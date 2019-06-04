package com.gigs2go.pi4jzero.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.PwmDevice;

public class PwmMotorImpl implements Motor {
    private static final Logger LOG = LoggerFactory.getLogger( PwmMotorImpl.class );
    private final PwmDevice pwm;
    private final DigitalOutputDevice inA;
    private final DigitalOutputDevice inB;
    private final String name;
    private final int range;
    private boolean forwards = false;
    private boolean backwards = false;
    private int value = 0;

    PwmMotorImpl( String name, PwmDevice pwm, DigitalOutputDevice inA, DigitalOutputDevice inB ) {
        this.name = name;
        this.pwm = pwm;
        this.inA = inA;
        this.inB = inB;
        this.range = pwm.getFullSpeed();
    }

    @Override
    public void forward( int value ) {
        LOG.debug( "Motor {} Forward : '{}'", name, value );
        setForwards( value );
    }

    @Override
    public void backward( int value ) {
        LOG.debug( "Motor {} Backward : '{}'", name, value );
        setBackwards( value );
    }

    @Override
    public void stop() {
        LOG.debug( "Motor {} Stop", name );
        // Set pwm to off
        pwm.off();
        value = 0;
        forwards = false;
        backwards = false;
    }

    @Override
    public void full() {
        LOG.debug( "Motor {} Full", name );
        // Set pwm to on
        pwm.on();
        value = this.range;
    }

	@Override
	public int getFullSpeed() {
		return range;
	}

    private void setForwards( int value ) {
        if ( !forwards ) {
            inA.off();
            inB.on();
            backwards = false;
            forwards = true;
        }
        setSpeed( value );
    }

    private void setBackwards( int value ) {
        if ( !backwards ) {
            inA.on();
            inB.off();
            backwards = true;
            forwards = false;
        }
        setSpeed( value );
    }

    private void setSpeed( int value ) {
        LOG.debug(  "Motor {} SetSpeed : '{}'", name, value );
        if ( value < 0 ) {
            LOG.warn( "Invalid value : '{}' - leaving unchanged from '{}'", value, this.value );
        } else if ( value == 0 ) {
            LOG.info( "Value was '{}' - stopping", value );
            stop();
        } else if ( value >= this.range ) {
            LOG.info( "Value was > {} ('{}') - going to full", this.range, value );
            full();
        } else {
            LOG.debug( "Setting? '{}' == '{}'", this.value, value );
            if ( this.value != value ) {
                // Set speed
                pwm.setPwm( value );
                this.value = value;
            }
        }
    }

    @Override
    public String toString() {
        return "PwmMotorImpl [name=" + name + ", pwm=" + pwm + ", inA=" + inA + ", inB=" + inB + ", forwards=" + forwards + ", backwards=" + backwards + ", value=" + value + "]";
    }
}
