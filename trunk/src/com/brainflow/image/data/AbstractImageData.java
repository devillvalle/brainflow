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

    // todo private
    protected final IImageSpace space;

    // todo private
    protected DataType datatype;

    private String imageLabel;

    private ImageInfo info = new ImageInfo();

    // todo get rid of this abomination
    protected int identifier = 0;

    public AbstractImageData(IImageSpace space) {
        this.space = space;
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

    public int getNumElements() {
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

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }
}
