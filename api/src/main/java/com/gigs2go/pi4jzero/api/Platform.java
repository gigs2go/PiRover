package com.gigs2go.pi4jzero.api;

public interface Platform {
    void center();
    void left( int delta );
    void right( int delta );
}
