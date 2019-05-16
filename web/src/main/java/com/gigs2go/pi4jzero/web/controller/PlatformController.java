package com.gigs2go.pi4jzero.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gigs2go.pi4jzero.api.MotorPlatform;

public class PlatformController {
    @SuppressWarnings("unused") // TODO : Use ...
    private static final Logger LOG = LoggerFactory.getLogger( PlatformController.class );
    private MotorPlatform platform;


    public void setPlatform( MotorPlatform motorPlatform ) {
        this.platform = motorPlatform;
    }

    @RequestMapping("/platform")
    public String index() {
        return "Platform Controller running ...";
    }

    @RequestMapping("/platform/status")
    public String status() {
        return "Platform is " + platform.toString();
    }

    @RequestMapping("/platform/stop")
    public String stop() {
        platform.stop();
        return "Stopped";
    }

    @RequestMapping("/platform/center")
    public String center() {
        platform.center();
        return "Centered";
    }

    @RequestMapping("/platform/speed")
    public String speed( @RequestParam("value") int value ) {
        if ( value < 0 ) {
            platform.backward( -value );
        } else if ( value > 0 ) {
            platform.forward( value );
        } else {
            platform.stop();
        }
        return "Speed";
    }

    @RequestMapping("/platform/steer")
    public String steer( @RequestParam("value") int value ) {
        if ( value < 0 ) {
            platform.left( -value );
        } else if ( value > 0 ) {
            platform.right( value );
        } else {
            platform.center();
        }
        return "Steer";
    }
}
