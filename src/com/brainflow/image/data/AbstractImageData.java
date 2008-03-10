package com.brainflow.image.data;

import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 2:28:17 PM
 * To change this template use File | Settings | File Templates.
 */


public abstract class AbstractImageData implements IImageData {


    protected final IImageSpace space;

    protected final DataType datatype;

    private String imageLabel;

    private ImageInfo info = new ImageInfo();


    public AbstractImageData(IImageSpace space) {
        this.space = space;
        datatype = DataType.DOUBLE;
    }

    public AbstractImageData(IImageSpace space, DataType dtype) {
        this.space = space;
        this.datatype = dtype;
    }

    public DataType getDataType() {
        return datatype;
    }

    public Anatomy getAnatomy() {
        return space.getAnatomy();
    }

    public int getDimension(Axis axis) {
        return space.getDimension(axis);
    }

    public int numElements() {
        return space.getNumSamples();
    }

    public ImageInfo getImageInfo() {
        return info;
    }

    public void setImageLabel(String imageLabel) {
        this.imageLabel = imageLabel;
    }

    public String getImageLabel() {
        return imageLabel;
    }


}
