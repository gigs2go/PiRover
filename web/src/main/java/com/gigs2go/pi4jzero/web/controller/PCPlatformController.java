package com.gigs2go.pi4jzero.web.controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RestController;

import com.gigs2go.pi4jzero.api.MotorPlatform;

@RestController
@Profile("PC")
public class PCPlatformController extends PlatformController {
    private static final Logger LOG = LoggerFactory.getLogger( PCPlatformController.class );

    @PostConstruct
    private void initialise() {
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

        } );
    }
    
    @PreDestroy
    public void onExit() {
      LOG.info("### PCRover Stopping ###");
    }
}
