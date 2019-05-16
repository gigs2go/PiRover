package com.gigs2go.pi4jzero.devices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.Device;
import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.MotorProvider;
import com.gigs2go.pi4jzero.api.PwmDevice;

public class TB6612 implements Device, MotorProvider {
    private static final Logger LOG = LoggerFactory.getLogger( TB6612.class );
    private Motor[] motors = new Motor[2];
    private DigitalOutputDevice enable;
    private String name = "";
    private int count = 0;

    public TB6612( String name ) {
        this.name = name;
    }

    @Override
    public void setEnable( DigitalOutputDevice enable ) {
        this.enable = enable;
    }

    @Override
    public Motor createPwmMotor( String name, PwmDevice pwm, DigitalOutputDevice inA, DigitalOutputDevice inB ) {
        if ( enable == null ) {
            LOG.warn( "No enable set on motor processor" );
        }
        // Don't really care - there's only 2!
        if ( count < 2 ) {
            motors[count++] = new PwmMotorImpl( name, pwm, inA, inB );
        }
        LOG.info( "New motor '{}' : '{}'", name, count - 1 );
        Motor result = new PwmMotorImpl( name, pwm, inA, inB );
        return result;
    }

    @Override
    public void on() {
        LOG.debug( "{} ON", name );
        if ( enable != null ) {
            enable.on();
        }
    }

    @Override
    public void off() {
        LOG.debug( "{} OFF", name );
        if ( enable != null ) {
            enable.off();
        }
    }

    @Override
    public String toString() {
        return "TB6612 [enable=" + enable + ", name=" + name + ", count=" + count + "]";
    }
}
