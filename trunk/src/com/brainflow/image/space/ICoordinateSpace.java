package com.brainflow.image.space;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.axis.CoordinateAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 19, 2007
 * Time: 1:08:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ICoordinateSpace {


    public double getExtent(Axis axis);

    public double getExtent(AnatomicalAxis axis);

    public Axis findAxis(AnatomicalAxis axis);

    public AnatomicalAxis getAnatomicalAxis(Axis axis);

    public CoordinateAxis getImageAxis(Axis axis);

    public CoordinateAxis getImageAxis(AnatomicalAxis axis, boolean ignoreDirection);

    public int getNumDimensions();

    public ICoordinateSpace union(ICoordinateSpace other);

    public Anatomy getAnatomy();

    public IImageOrigin getImageOrigin();



}
