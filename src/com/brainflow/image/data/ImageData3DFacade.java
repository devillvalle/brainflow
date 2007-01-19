package com.brainflow.image.data;

import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.utils.DataType;
import com.brainflow.utils.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2006
 * Time: 1:03:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData3DFacade implements IImageData3D {


    private IImageData wrappedData;
    private Anatomy facadAnatomy;

    public ImageData3DFacade(IImageData data, Anatomy facade) {
        wrappedData = data;
        facadAnatomy = facade;
    }

    public int getIdentifier() {
        return wrappedData.getIdentifier();
    }

    public void setIdentifier(int identifier) {
        wrappedData.setIdentifier(identifier);
    }

    public IImageSpace getImageSpace() {
        return wrappedData.getImageSpace();
    }

    public DataType getDataType() {
        return wrappedData.getDataType();
    }

    public Anatomy getAnatomy() {
        return wrappedData.getAnatomy();
    }

    public int getDimension(Axis axis) {
        return wrappedData.getDimension(axis);
    }

    public double getMaxValue() {
        return wrappedData.getMaxValue();
    }

    public double getMinValue() {
        return wrappedData.getMinValue();
    }

    public int getNumElements() {
        return wrappedData.getNumElements();
    }

    public ImageInfo getImageInfo() {
        return wrappedData.getImageInfo();
    }

    public Index3D voxelOf(int idx, Index3D voxel) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int indexOf(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(int index) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getInt(int index) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public double getValue(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float getFloat(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getInt(int x, int y, int z) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int idx, double val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setValue(int x, int y, int z, double val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setFloat(int x, int y, int z, float val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setInt(int x, int y, int z, int val) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] toBytes() {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ImageIterator iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getImageLabel() {
        return wrappedData.getImageLabel();
    }

    public void setImageLabel(String imageLabel) {
        wrappedData.setImageLabel(imageLabel);
    }

}
