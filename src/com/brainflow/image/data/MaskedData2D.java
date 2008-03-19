package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction2D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.DataType;
import com.brainflow.utils.IDimension;

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

    public IDimension<Integer> getDimension() {
        return source.getDimension();
    }

    public double getValue(double x, double y, InterpolationFunction2D interp) {
        return predicate.mask(source.getValue(x, y, interp))? 1 : 0;
    }

    public double getRealValue(double realx, double realy, InterpolationFunction2D interp) {
        return predicate.mask(source.getRealValue(realx, realy, interp)) ? 1 : 0;
    }

    public double getValue(int x, int y) {
        return predicate.mask(source.getValue(x, y)) ? 1 : 0;
    }

    public double getValue(int index) {
        return predicate.mask(source.getValue(index)) ? 1 : 0;
    }

    public void setValue(int idx, double val) {
        source.setValue(idx, val);
    }

    public void setValue(int x, int y, double val) {
        source.setValue(x, y, val);
    }



    public ImageIterator iterator() {
        return new MaskedIterator();

    }

    public ImageSpace2D getImageSpace() {
        return (ImageSpace2D)source.getImageSpace();
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

    public double maxValue() {
        return source.maxValue();
    }

    public double minValue() {
        return source.minValue();
    }

    public int numElements() {
        return source.numElements();
    }

    public ImageInfo getImageInfo() {
        return source.getImageInfo();
    }

    

    public String getImageLabel() {
        return source.getImageLabel();
    }



    class MaskedIterator implements ImageIterator {

        ImageIterator iter;

        public MaskedIterator() {
            iter = source.iterator();
        }

        public double next() {
            return predicate.mask(iter.next()) ? 1 : 0;
        }

        public void advance() {
            iter.advance();
        }

        public double previous() {
            return predicate.mask(iter.previous()) ? 1 : 0;
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
