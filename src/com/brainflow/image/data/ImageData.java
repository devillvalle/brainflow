package com.brainflow.image.data;

import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.DataType;
import com.brainflow.math.Index3D;
import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 9, 2008
 * Time: 9:10:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageData {

     public static IImageData3D createConstantData(final double value, final IImageSpace space) {
         if (space instanceof ImageSpace3D) {
             return ImageData.createConstantData(value, (ImageSpace3D)space);
         } else {
             throw new IllegalArgumentException("IImageSpace argumnet must be of class ImageSpace3D");
         }
     }

    public static IImageData3D createScaledData(final IImageData3D data, final double scaleFactor) {
        return new IImageData3D() {
            public IImageSpace3D getImageSpace() {
                return data.getImageSpace();
            }

            public final int indexOf(int x, int y, int z) {
                return data.indexOf(x,y,z);
            }

            public Index3D indexToGrid(int idx) {
                return data.indexToGrid(idx);
            }

            public void setValue(int x, int y, int z, double val) {
                data.setValue(x,y,z,val);
            }

            public Anatomy getAnatomy() {
                return data.getAnatomy();
            }

            public DataType getDataType() {
                return data.getDataType();
            }

            public int getDimension(Axis axisNum) {
                return data.getDimension(axisNum);
            }

            public ImageInfo getImageInfo() {
                return data.getImageInfo();
            }

            public String getImageLabel() {
                return data.getImageLabel();
            }

            public ImageIterator iterator() {
                //todo this is wrong
                return data.iterator();
            }

            public double maxValue() {
                return data.maxValue() * scaleFactor;
            }

            public double minValue() {
                return data.minValue() * scaleFactor;
            }

            public void setValue(int idx, double val) {
                data.setValue(idx,val);
            }


            public final double value(int index) {
                return data.value(index) * scaleFactor;
            }

            public int numElements() {
                return data.numElements();
            }

            public final double value(float x, float y, float z, InterpolationFunction3D interp) {
                return data.value(x,y,z,interp) * scaleFactor;
            }

            public final double value(int x, int y, int z) {
                return data.value(x,y,z) * scaleFactor;
            }

            public final double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                return data.worldValue(realx,realy,realz, interp);
            }
        };
    }

    @Testable
    public static IImageData3D createConstantData(final double value, final IImageSpace3D space) {
        return new AbstractImageData3D(space, DataType.DOUBLE) {
            public double value(int index) {
                return value;
            }

            public Index3D indexToGrid(int idx) {
                throw new UnsupportedOperationException();
            }

            public void setValue(int idx, double val) {
                throw new UnsupportedOperationException();
            }

            public double minValue() {
                return value;
            }

            public double maxValue() {
                return value;
            }

            public ImageIterator iterator() {
                return new BasicImageData3D.Iterator3D(this);
            }

            public double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
                return value;
            }

            public double value(float x, float y, float z, InterpolationFunction3D interp) {
                return value;
            }

            public double value(int x, int y, int z) {
                return value;
            }

            public void setValue(int x, int y, int z, double val) {
                throw new UnsupportedOperationException();
            }

            public String getImageLabel() {
                return "constant: " + value;
            }


        };



    }
}
