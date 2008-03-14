package com.brainflow.image.data;

import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.anatomy.Anatomy;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 9:56:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskedData3D implements IMaskedData3D {

    private IImageData3D source;

    private MaskPredicate predicate;

    
    public MaskedData3D(IImageData3D src, MaskPredicate predicate) {
        source = src;
        this.predicate = predicate;
    }

    public MaskPredicate getPredicate() {
        return predicate;
    }

    public IImageData3D getSource() {
        return source;
    }


    public Index3D indexToGrid(int idx, Index3D voxel) {
        return source.indexToGrid(idx, voxel);
    }

    public int indexOf(int x, int y, int z) {
        return source.indexOf(x, y, z);
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return predicate.mask(source.getValue(x, y, z, interp)) ? 1 : 0;
    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return predicate.mask(source.getRealValue(realx, realy, realz, interp)) ? 1 : 0;
    }

    public boolean isTrue(int index) {
        return predicate.mask(source.getValue(index));
    }

    public boolean isTrue(int x, int y, int z) {
        return predicate.mask(source.getValue(x, y, z));
    }

    public double getValue(int index) {
        return predicate.mask(source.getValue(index)) ? 1 : 0;
    }


    public double getValue(int x, int y, int z) {
        return predicate.mask(source.getValue(x, y, z)) ? 1 : 0;
    }



    public void setValue(int idx, double val) {
        source.setValue(idx, val);
    }

    public void setValue(int x, int y, int z, double val) {
        source.setValue(x, y, z, val);
    }



    public ImageSpace3D getImageSpace() {
        return source.getImageSpace();
    }

    public DataType getDataType() {
        return DataType.INTEGER;
    }

    public Anatomy getAnatomy() {
        return source.getAnatomy();
    }

    public int getDimension(Axis axisNum) {
        return source.getDimension(axisNum);
    }

    public double maxValue() {
        //todo needs to be computed
        return 1;
    }

    public double minValue() {
        //todo needs to be computed
        return 0;
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

    public ImageIterator iterator() {
        return new MaskedIterator();
    }

   

    public int cardinality() {
        MaskedIterator iter = new MaskedIterator();
        int count = 0;
        while (iter.hasNext()) {
            if (iter.next() > 0) {
                count++;
            }
        }

        return count;

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
