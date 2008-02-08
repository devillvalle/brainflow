package com.brainflow.image.data;

import com.brainflow.utils.Index3D;
import com.brainflow.utils.DataType;
import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.space.IImageSpace;
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


    public Index3D voxelOf(int idx, Index3D voxel) {
        return source.voxelOf(idx, voxel);
    }

    public int indexOf(int x, int y, int z) {
        return source.indexOf(x, y, z);
    }

    public double getValue(double x, double y, double z, InterpolationFunction3D interp) {
        return predicate.mask(source.getValue(x, y, z, interp));
    }

    public double getRealValue(double realx, double realy, double realz, InterpolationFunction3D interp) {
        return predicate.mask(source.getRealValue(realx, realy, realz, interp));
    }

    public int isTrue(int index) {
        return predicate.mask(source.getInt(index));

    }

    public int isTrue(int x, int y, int z) {
        return predicate.mask(source.getInt(x, y, z));
    }

    public double getValue(int index) {
        return predicate.mask(source.getValue(index));
    }

    public int getInt(int index) {
        return predicate.mask(source.getInt(index));
    }

    public double getValue(int x, int y, int z) {
        return predicate.mask(source.getValue(x, y, z));
    }

    public float getFloat(int x, int y, int z) {
        return predicate.mask(source.getFloat(x, y, z));
    }

    public int getInt(int x, int y, int z) {
        return predicate.mask(source.getInt(x, y, z));
    }

    public void setValue(int idx, double val) {
        source.setValue(idx, val);
    }

    public void setValue(int x, int y, int z, double val) {
        source.setValue(x, y, z, val);
    }

    public void setFloat(int x, int y, int z, float val) {
        source.setFloat(x, y, z, val);
    }

    public void setInt(int x, int y, int z, int val) {
        source.setInt(x, y, z, val);
    }

    public ImageSpace3D getImageSpace() {
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
        return 1;
    }

    public double getMinValue() {
        return 0;
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

    public ImageIterator iterator() {
        return new MaskedIterator();
    }

    public int getIdentifier() {
        return source.getIdentifier();
    }

    public void setIdentifier(int identifier) {
        source.setIdentifier(identifier);
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
