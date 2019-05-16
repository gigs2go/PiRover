package com.gigs2go.pi4jzero.platforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.DeviceProvider;
import com.gigs2go.pi4jzero.api.DigitalOutputDevice;
import com.gigs2go.pi4jzero.api.Motor;
import com.gigs2go.pi4jzero.api.MotorPlatform;
import com.gigs2go.pi4jzero.api.MotorProvider;
import com.gigs2go.pi4jzero.api.PwmDevice;

public class MotorPlatformImpl implements MotorPlatform {
    private static final Logger LOG = LoggerFactory.getLogger( MotorPlatformImpl.class );
    private Motor flMotor;
    private Motor frMotor;
    private Motor rlMotor;
    private Motor rrMotor;
    //private boolean backwards = false;
    private int speed = 0; // Motor.MAX_SPEED => Motor.MAX_SPEED

    public MotorPlatformImpl( DeviceProvider controller, MotorProvider mp1, MotorProvider mp2 ) {
        // Get Motors
        DigitalOutputDevice enableMp1 = controller.getDigitalOutputDevice( 3 );
        mp1.setEnable( enableMp1 );

        PwmDevice pwm = controller.getPwmDevice( 0 );
        DigitalOutputDevice inA = controller.getDigitalOutputDevice( 1 );
        DigitalOutputDevice inB = controller.getDigitalOutputDevice( 2 );
        frMotor = mp1.createPwmMotor( "FrontRight", pwm, inA, inB );

        pwm = controller.getPwmDevice( 4 );
        inA = controller.getDigitalOutputDevice( 6 );
        inB = controller.getDigitalOutputDevice( 5 );
        rrMotor = mp1.createPwmMotor( "Rear Right", pwm, inA, inB );

        DigitalOutputDevice enableMp2 = controller.getDigitalOutputDevice( 11 );
        mp2.setEnable( enableMp2 );

        pwm = controller.getPwmDevice( 8 );
        inA = controller.getDigitalOutputDevice( 9 );
        inB = controller.getDigitalOutputDevice( 10 );
        flMotor = mp2.createPwmMotor( "Front Left", pwm, inA, inB );

        pwm = controller.getPwmDevice( 12 );
        inA = controller.getDigitalOutputDevice( 14 );
        inB = controller.getDigitalOutputDevice( 13 );
        rlMotor = mp2.createPwmMotor( "Rear Left", pwm, inA, inB );
    }

    @Override
    public void forward( int value ) {
        LOG.debug( "Platform - forward : '{}'", value );
        speed = value;
        flMotor.forward( value );
        frMotor.forward( value );
        rlMotor.forward( value );
        rrMotor.forward( value );
    }

    @Override
    public void backward( int value ) {
        LOG.debug( "Platform - backward : '{}'", value );
        speed = -value;
        flMotor.backward( value );
        frMotor.backward( value );
        rlMotor.backward( value );
        rrMotor.backward( value );
    }

    @Override
    public void stop() {
        LOG.debug( "Platform - stop" );
        speed = 0;
        flMotor.stop();
        frMotor.stop();
        rlMotor.stop();
        rrMotor.stop();
    }

    @Override
    public void center() {
        LOG.debug( "Platform - center" );
        if ( speed < 0 ) {
            backward( -speed );
        } else {
            forward( speed );
        }
    }

    private int[] splitSteer( int steer ) {
        int[] result = new int[2];
        if ( (speed + steer) > Motor.MAX_SPEED ) {
            result[0] = Motor.MAX_SPEED;
            result[1] = Motor.MAX_SPEED - (2 * steer);
        } else if ( (speed - steer) < -Motor.MAX_SPEED ) {
            result[0] = -Motor.MAX_SPEED + (2 * steer);
            result[1] = -Motor.MAX_SPEED;
        } else {
            result[0] = speed + steer;
            result[1] = speed - steer;
        }
        LOG.debug( "SplitSteer : Speed - '{}', Steer - '{}', High - '{}', Low - '{}'", speed, steer, result[0], result[1] );
        return result;
    }
    
    private void adjustMotor( Motor motor, int value ) {
        if ( value < 0 ) {
            motor.backward( -value );
        } else {
            motor.forward( value );
        }
    }

    @Override
    public void left( int value ) {
        LOG.debug( "Platform - left : '{}'", value );
        int[] newSpeeds = splitSteer( value );
        adjustMotor( frMotor, newSpeeds[1] );
        adjustMotor( rrMotor, newSpeeds[1] );
        adjustMotor( flMotor, newSpeeds[0] );
        adjustMotor( rlMotor, newSpeeds[0] );
    }

    @Override
    public void right( int value ) {
        LOG.debug( "Platform - right : '{}'", value );
        int[] newSpeeds = splitSteer( value );
        adjustMotor( flMotor, newSpeeds[1] );
        adjustMotor( rlMotor, newSpeeds[1] );
        adjustMotor( frMotor, newSpeeds[0] );
        adjustMotor( rrMotor, newSpeeds[0] );
    }

    @Override
    public void full() {
        LOG.debug( "Platform - full" );
        speed = Motor.MAX_SPEED;
        flMotor.full();
        frMotor.full();
        rlMotor.full();
        rrMotor.full();
    }
}
