package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.utils.IDimension;
import com.brainflow.math.IIndex;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 25, 2006
 * Time: 4:43:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageSpace1D extends AbstractImageSpace implements IImageSpace {


    public IDimension<Float> getOrigin() {
        throw new UnsupportedOperationException();
    }

    public AnatomicalPoint getCentroid() {
        throw new UnsupportedOperationException();
    }

    public IDimension getDimension() {
        throw new UnsupportedOperationException();
    }

    public float[] indexToWorld(int[] index) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float[] gridToWorld(float[] gridpos) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float[] worldToGrid(float[] coord) {
        return new float[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IIndex indexToGrid(int idx) {
       throw new UnsupportedOperationException(); 
    }
}
