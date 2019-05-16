package com.gigs2go.pi4jzero.web;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gigs2go.pi4jzero")
public class WebApplication {
    private static final Logger LOG = LoggerFactory.getLogger( WebApplication.class );

    public static void main( String[] args ) {
        SpringApplication.run( WebApplication.class, args );
    }
}
