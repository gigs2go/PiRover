package com.gigs2go.pi4jzero.web.controller;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gigs2go.pi4jzero.api.MotorPlatform;

public class PCPlatformController extends PlatformController {
    private static final Logger LOG = LoggerFactory.getLogger( PCPlatformController.class );
	@Override
	public void initialise() {
		LOG.info( "Loading");
		Properties props = loadProperties();
		LOG.debug( props.toString() );
        super.setPlatform( new MotorPlatform() {

            @Override
            public void forward( int value ) {
                LOG.info( "Forward {}", value );
            }

            @Override
            public void backward( int value ) {
                LOG.info( "Backward {}", value );
            }

            @Override
            public void stop() {
                LOG.info( "Stop" );
            }

            @Override
            public void full() {
                LOG.info( "Full" );
            }

            @Override
            public void center() {
                LOG.info( "Center" );
            }

            @Override
            public void left( int delta ) {
                LOG.info( "Left {}", delta );
            }

            @Override
            public void right( int delta ) {
                LOG.info( "Right {}", delta );
            }

			@Override
			public int getFullSpeed() {
				// TODO Auto-generated method stub
				return 0;
			}

        } );
    }
}
