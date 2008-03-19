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
public interface IImageData extends DataAccessor {


    public DataType getDataType();

    public Anatomy getAnatomy();

    public int getDimension(Axis axisNum);

    public void setValue(int idx, double val);

    public double maxValue();

    public double minValue();
 
    public ImageInfo getImageInfo();
    
    public IImageSpace getImageSpace();

    public String getImageLabel();

    public ImageIterator iterator();



    

}
