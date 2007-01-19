/*
 * AbstractTransformImageFilter.java
 *
 * Created on May 20, 2003, 4:29 PM
 */

package com.brainflow.image.operations;
import com.brainflow.image.space.*;
import com.brainflow.utils.*;
import com.brainflow.image.interpolation.*;
import cern.colt.matrix.*;
import cern.colt.matrix.impl.*;
import java.util.*;
/**
 *
 * @author  Bradley
 */
public abstract class AbstractTransformImageFilter extends AbstractImageFilter {
    
    protected ImageSpace3D outputSpace;
    protected ImageSpace3D referenceSpace;
    protected InterpolationFunction3D interpolator = new TrilinearInterpolator();
    
    protected Point3D origin = new Point3D();
    
    /** Creates a new instance of AbstractTransformImageFilter */
    public AbstractTransformImageFilter() {
    }
    
    public abstract void setTransform(AbstractTransform transform);
        
    public void setOutputSpace(ImageSpace3D space3d) {
        outputSpace = space3d;
    }    
    
    public void setInterpolator(InterpolationFunction3D _interp) {
        interpolator = _interp;
    }
    
    public void setOuputOrigin(Point3D pt) {
        origin = pt;
    }
    
    public void setReferenceSpace(ImageSpace3D refSpace) {
        referenceSpace = refSpace;
    }
    
}
