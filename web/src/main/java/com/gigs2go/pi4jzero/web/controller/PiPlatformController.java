package com.gigs2go.pi4jzero.web.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.gigs2go.pi4jzero.devices.PCA9685;
import com.gigs2go.pi4jzero.devices.TB6612;
import com.gigs2go.pi4jzero.platforms.MotorPlatformImpl;

//@CrossOrigin(origins = { "/**" }, allowCredentials = "false")
//@CrossOrigin(origins = "http://192.168.2.1:9000")
@RestController
@Profile("pi")
public class PiPlatformController extends PlatformController {
    private static final Logger LOG = LoggerFactory.getLogger( PiPlatformController.class );
    private PCA9685 controller;

    @PostConstruct
    private void initialise() {
        try {
            this.controller = new PCA9685( 0x40, 500 );
            TB6612 mp1 = new TB6612( "MotorController1" );
            TB6612 mp2 = new TB6612( "MotorController2" );
            super.setPlatform( new MotorPlatformImpl( this.controller, mp1, mp2 ) );
            // Enable motor controllers
            mp1.on();
            mp2.on();
        } catch ( IOException e ) {
            LOG.error( "Error", e );
        }
    }

    @PreDestroy
    public void onExit() {
      LOG.info("### PiRover Stopping ###");
    }
}
