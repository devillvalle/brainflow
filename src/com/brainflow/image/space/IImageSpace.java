package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.math.IIndex;
import com.brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Jan 30, 2004
 * Time: 11:30:32 AM
 * To change this template use Options | File Templates.
 */
public interface IImageSpace extends ICoordinateSpace {

    public IDimension<Integer> getDimension();

    public IDimension<Float> getOrigin();

    public int getDimension(Axis axis);

    public double getSpacing(Axis axis);

    public int getDimension(AnatomicalAxis axis);

    public double getSpacing(AnatomicalAxis axis);

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection);

    public int getNumSamples();

    public boolean sameGeometry(IImageSpace other);

    public IIndex indexToGrid(int idx);

    public float[] indexToWorld(int ... index);

    public float[] gridToWorld(float ... gridpos);

    public float[] worldToGrid(float ... coord);

    // todo should be a static library method
    
    //public IImageSpace union(IImageSpace other);

    // todo should be a static library method
    //public AnatomicalPoint getCentroid();


}
