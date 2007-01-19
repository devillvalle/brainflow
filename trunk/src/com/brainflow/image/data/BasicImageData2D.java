package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.utils.ArrayUtils;
import com.brainflow.utils.DataType;

import java.awt.image.RenderedImage;


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

    private int[] dimArray = null;

    public BasicImageData2D(BasicImageData2D src) {
        space = new ImageSpace2D((ImageSpace2D) src.getImageSpace());
        datatype = src.getDataType();
        fillBuffer(src.storage, space.getNumSamples());
        dimArray = space.getDimensionVector();

    }

    public BasicImageData2D(IImageSpace _space, DataType _type) {
        assert _space.getNumDimensions() == 2;
        space = _space;
        datatype = _type;
        data = allocateBuffer(space.getNumSamples());
        dimArray = space.getDimensionVector();

    }

    public int getDimension(int dimnum) {
        return dimArray[dimnum];
    }

    public ImageInfo getImageInfo() {
        return new ImageInfo(this);
    }

    public BasicImageData2D(IImageSpace _space, Object array) {
        assert _space.getNumDimensions() == 2;
        space = _space;
        storage = array;
        establishDataType(storage);
        data = allocateBuffer(space.getNumSamples());
        dimArray = space.getDimensionVector();
    }


    public final int indexOf(int x, int y) {
        return space.getDimension(Axis.X_AXIS) * y + x;
    }

    public final double getValue(double x, double y, InterpolationFunction2D interp) {
        return interp.interpolate(x, y, this);
    }

    public final double getRealValue(double realx, double realy, InterpolationFunction2D interp) {
        double x = space.getImageAxis(Axis.X_AXIS).fractionalSample(realx);
        double y = space.getImageAxis(Axis.Y_AXIS).fractionalSample(realy);
        return interp.interpolate(x, y, this);
    }

    public final double getValue(int x, int y) {
        return data.getElemDouble(indexOf(x, y));
    }

    public final float getFloat(int x, int y) {
        return data.getElemFloat(indexOf(x, y));
    }

    public final int getInt(int x, int y) {
        return data.getElem(indexOf(x, y));
    }

    public final void setValue(int x, int y, double val) {
        data.setElemDouble(indexOf(x, y), val);
    }

    public final void setFloat(int x, int y, float val) {
        data.setElemFloat(indexOf(x, y), val);
    }

    public final void setInt(int x, int y, int val) {
        data.setElem(indexOf(x, y), val);
    }


    public ImageIterator iterator() {
        return new Iterator2D();
    }

    public RenderedImage snapShot() {
        byte[] idat = ArrayUtils.scaleToBytes(storage, getMinValue(), getMaxValue(), 255);
        return RenderUtils.createSingleBandedImage(idat, space.getDimension(Axis.X_AXIS), space.getDimension(Axis.Y_AXIS));
    }


    public byte[] toBytes() {
        byte[] idat = ArrayUtils.scaleToBytes(storage, getMinValue(), getMaxValue(), 255);
        return idat;
    }

    public byte[] toBytes(double min, double max) {
        byte[] idat = ArrayUtils.scaleToBytes(storage, min, max, 255);
        return idat;
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
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.nextPlane(): only one plane in 2D iterator!");
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
            throw new java.lang.UnsupportedOperationException("ImageIterator2D.previousPlane(): only one plane in 2D iterator!");
        }

        public int index() {
            return index;
        }


    }


}