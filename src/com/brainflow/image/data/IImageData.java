package com.brainflow.image.data;

import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 1:00:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageData {

    public IImageSpace getImageSpace();

    public DataType getDataType();

    public Anatomy getAnatomy();

    public int getDimension(Axis axisNum);

    public double getMaxValue();

    public double getMinValue();

    public int getNumElements();

    public ImageInfo getImageInfo();


    public void setImageLabel(String imageLabel);

    public String getImageLabel();

    public ImageIterator iterator();

    public int getIdentifier();

    public void setIdentifier(int identifier);


}
