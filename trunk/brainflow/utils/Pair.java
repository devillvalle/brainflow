/*
 * Pair.java
 *
 * Created on January 29, 2003, 1:46 PM
 */

package com.brainflow.utils;

/**
 *
 * @author  Bradley
 */

public class Pair {
    
    
    private Object left, right;
    
    
    public Pair( Object left, Object right ) {
        this.left = left;
        this.right = right;
    }
    
    
    public Object left() {
        return left;
    }
    
    
    public Object right() {
        return right;
    }
    
    /// Equality test.
    public boolean equals( Object o ) {
        if ( o != null && o instanceof Pair ) {
            Pair p = (Pair) o;
            return this.left.equals(p.left()) && this.right.equals(p.right());
        }
        return false;
    }
    
    /// Compute a hash code for the pair.
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }
    
    
    
}





