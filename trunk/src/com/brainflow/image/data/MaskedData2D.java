package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.Axis;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.DataType;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 9:50:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedData2D implements IImageData2D {

    private IImageData2D source;

    private MaskPredicate predicate;


    public MaskedData2D(IImageData2D src, MaskPredicate predicate) {
        super();
        source = src;
        this.predicate = predicate;
    }


    public int indexOf(int x, int y) {
        return source.indexOf(x, y);
    }

    public double getValue(double x, double y, InterpolationFunction2D interp) {
        return predicate.mask(source.getValue(x, y, interp));
    }

    public double getRealValue(double realx, double realy, InterpolationFunction2D interp) {
        return predicate.mask(source.getRealValue(realx, realy, interp));
    }

    public double getValue(int x, int y) {
        return predicate.mask(source.getValue(x, y));
    }

    public float getFloat(int x, int y) {
        return predicate.mask(source.getFloat(x, y));
    }

    public int getInt(int x, int y) {
        return predicate.mask(source.getInt(x, y));
    }

    public void setValue(int x, int y, double val) {
        source.setValue(x, y, val);
    }

    public void setFloat(int x, int y, float val) {
        source.setFloat(x, y, val);
    }

    public void setInt(int x, int y, int val) {
        source.setInt(x, y, val);
    }

    public ImageIterator iterator() {
        return new MaskedIterator();

    }

    public IImageSpace getImageSpace() {
        return source.getImageSpace();
    }

    public DataType getDataType() {
        return source.getDataType();
    }

    public Anatomy getAnatomy() {
        return source.getAnatomy();
    }

    public int getDimension(Axis axisNum) {
        return source.getDimension(axisNum);
    }

    public double getMaxValue() {
        return source.getMaxValue();
    }

    public double getMinValue() {
        return source.getMinValue();
    }

    public int getNumElements() {
        return source.getNumElements();
    }

    public ImageInfo getImageInfo() {
        return source.getImageInfo();
    }

    public void setImageLabel(String imageLabel) {
        source.setImageLabel(imageLabel);
    }

    public String getImageLabel() {
        return source.getImageLabel();
    }

    public int getIdentifier() {
        return source.getIdentifier();
    }

    public void setIdentifier(int identifier) {
        source.setIdentifier(identifier);
    }

    class MaskedIterator implements ImageIterator {

        ImageIterator iter;

        public MaskedIterator() {
            iter = source.iterator();
        }

        public double next() {
            return predicate.mask(iter.next());
        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            return predicate.mask(iter.previous());
        }

        public boolean hasNext() {
            return iter.hasNext();
        }

        public double jump(int number) {
            return iter.jump(number);
        }

        public boolean canJump(int number) {
            return iter.canJump(number);
        }

        public double nextRow() {
            return iter.nextRow();
        }

        public double nextPlane() {
            return iter.nextPlane();
        }

        public boolean hasNextRow() {
            return iter.hasNextRow();
        }

        public boolean hasNextPlane() {
            return iter.hasNextPlane();
        }

        public boolean hasPreviousRow() {
            return iter.hasPreviousRow();
        }

        public boolean hasPreviousPlane() {
            return iter.hasPreviousPlane();
        }

        public double previousRow() {
            return iter.previousRow();
        }

        public double previousPlane() {
            return iter.previousPlane();
        }

        public void set(double val) {
            iter.set(val);
        }

        public int index() {
            return iter.index();
        }
    }
}
