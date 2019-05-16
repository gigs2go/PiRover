package com.gigs2go.pi4jzero.api;

public interface Motor {
    static int MAX_SPEED = 4096;
    void forward( int value );
    void backward( int value );
    void stop();
    void full();
}
