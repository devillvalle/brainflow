/*
 * BrainflowException.java
 *
 * Created on February 4, 2003, 10:36 AM
 */

package com.brainflow.core;

/**
 *
 * @author  Bradley
 */
public class BrainflowException extends java.lang.Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8377682156060867344L;

	/**
     * Creates a new instance of <code>BrainflowException</code> without detail message.
     */
    public BrainflowException() {
    }
    
    
    /**
     * Constructs an instance of <code>BrainflowException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BrainflowException(String msg) {
        super(msg);
    }
    
    public BrainflowException(String msg, Throwable cause) {
        super(msg, cause);
    }
    
    public BrainflowException(Throwable cause) {
        super(cause);
    }
    
    
}
