package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.AnatomicalPoint;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Jan 30, 2004
 * Time: 11:30:32 AM
 * To change this template use Options | File Templates.
 */
public interface IImageSpace {

    public int[] getDimensionVector();

    public int getDimension(Axis axis);

    public double getSpacing(Axis axis);

    public double getExtent(Axis axis);

    public int getDimension(AnatomicalAxis axis);

    public double getSpacing(AnatomicalAxis axis);

    public double getExtent(AnatomicalAxis axis);

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection);

    public AnatomicalAxis getAnatomicalAxis(Axis axis);

    public Axis findAxis(AnatomicalAxis axis);

    public boolean sameAxes(IImageSpace other);

    public Anatomy getAnatomy();

    public int getNumDimensions();

    public int getNumSamples();

    public IImageOrigin getImageOrigin();

    public IImageSpace union(IImageSpace other);

    public AnatomicalPoint getCentroid();


}
