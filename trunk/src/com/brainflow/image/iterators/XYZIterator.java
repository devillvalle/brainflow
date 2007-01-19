/*
 * XYZIterator.java
 *
 * Created on May 12, 2003, 3:32 PM
 */

package com.brainflow.image.iterators;
import com.brainflow.utils.*;


/**
 *
 * @author  Bradley
 */
public interface XYZIterator {
    
    public Point3D next();
    public Point3D next(Point3D holder);
    public int nextIndex();
    public boolean hasNext();
    public int getXIndex();
    public int getYIndex();
    public int getZIndex();
    public int getIndex();
    
}
