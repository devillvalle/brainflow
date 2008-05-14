/*
 * XYZIterator.java
 *
 * Created on May 12, 2003, 3:32 PM
 */

package com.brainflow.image.iterators;
import com.brainflow.utils.*;
import com.brainflow.math.Vector3f;


/**
 *
 * @author  Bradley
 */
public interface XYZIterator {
    
    public Vector3f next();

    public Vector3f next(Vector3f holder);

    public int nextIndex();

    public boolean hasNext();

    public int getXIndex();

    public int getYIndex();

    public int getZIndex();

    public int getIndex();
    
}
