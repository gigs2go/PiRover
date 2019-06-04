package com.gigs2go.pi4jzero.api;

/**
 * Basic directional control for a Platform.
 *  
 * @author tim
 *
 */
public interface Platform {
	/**
	 * Center the steering
	 */
    void center();
    /**
     * Steer left
     * @param delta The amount to steer
     */
    void left( int delta );
    /**
     * Steer right
     * @param delta The amount to steer
     */
    void right( int delta );
}
