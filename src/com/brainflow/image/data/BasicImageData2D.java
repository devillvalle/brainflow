package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.utils.DataType;
import com.brainflow.utils.IDimension;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageData2D extends BasicImageData implements IImageData2D {


    public BasicImageData2D(BasicImageData2D src) {
        super(src.getImageSpace(),src.getDataType());
        fillBuffer(src.storage, space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, DataType _type) {
        super(space, _type);
        data = allocateBuffer(space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, DataType _type, String _imageLabel) {
        super(space, _type, _imageLabel);
        data = allocateBuffer(space.getNumSamples());


    }

     public BasicImageData2D(ImageSpace2D space, Object array) {
        super(space, establishDataType(array));
        storage = array;

        data = allocateBuffer(space.getNumSamples());

    }

    public BasicImageData2D(ImageSpace2D space, Object array, String imageLabel) {
        super(space, establishDataType(array), imageLabel);
        storage = array;

        data = allocateBuffer(space.getNumSamples());

    }


    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public IDimension<Integer> getDimension() {
        return space.getDimension();
    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D)space;

    }

    public double value(int index) {
        return data.getElemDouble(index);
    }

    public void setValue(int idx, double val) {
        data.setElemDouble(idx, val);
    }

    public final int indexOf(int x, int y) {
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public final double value(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public final double worldValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).gridPosition(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).gridPosition(realy);
        return interp.interpolate(x, y, this);
    }

    public final double value(int x, int y) {
        return data.getElemDouble(indexOf(x, y));
    }



    public final void setValue(int x, int y, double val) {
        data.setElemDouble(indexOf(x, y), val);
    }

  

    public ImageIterator iterator() {
        return new Iterator2D();
    }


    final class Iterator2D implements ImageIterator {

        private int index;

        private int len = space.getNumSamples();
        private int end = len;
        private int begin = 0;

        public Iterator2D() {
            index = 0;
        }

        public void advance() {
            index++;
        }

        public final double next() {
            double dat = data.getElemDouble(index);
            index++;
            return dat;
        }

        public void set(double val) {
            data.setElemDouble(index, val);
        }

        public double previous() {
            return data.getElemDouble(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double element(int number) {
            index = number;
            return data.getElemDouble(number);
        }

        public double jump(int number) {
            index += number;
            return data.getElemDouble(number);
        }

        public boolean canJump(int number) {
            if ((index + number) < end && (index - number >= begin))
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return data.getElemDouble(index);
        }

        public double nextPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.nextPlane(): only zero plane in 2D iterator!");
        }

        public boolean hasNextRow() {
            if ((index + space.getDimension(Axis.X_AXIS)) < len)
                return true;
            return false;
        }

        public boolean hasNextPlane() {
            return false;
        }

        public boolean hasPreviousRow() {
            if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
                return true;
            return false;
        }

        public boolean hasPreviousPlane() {
            return false;
        }

        public double previousRow() {
            index -= space.getDimension(Axis.X_AXIS);
            return data.getElemDouble(index);
        }

        public double previousPlane() {
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only zero plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}