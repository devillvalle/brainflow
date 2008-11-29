package com.brainflow.image.data;

import com.brainflow.image.interpolation.InterpolationFunction3D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.math.ArrayUtils;
import com.brainflow.math.Index3D;
import com.brainflow.utils.DataType;
import test.TestUtils;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class BasicImageData3D extends BasicImageData implements IImageData3D {


    private int planeSize;

    private int dim0;

    private IImageSpace3D space3d;


    public BasicImageData3D(BasicImageData3D src) {
        //todo look at all the code duplication       
        super(src.getImageSpace(), src.getDataType());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = getImageSpace();
        fillBuffer(src.storage, space.getNumSamples());
    }


    public BasicImageData3D(IImageSpace3D space, DataType _type) {
        super(space, _type);

        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = getImageSpace();
        allocateBuffer(space.getNumSamples());

    }

    public BasicImageData3D(IImageSpace3D space, DataType _type, String imageLabel) {
        super(space, _type, imageLabel);
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = getImageSpace();
        allocateBuffer(space.getNumSamples());


    }

    public BasicImageData3D(IImageSpace3D space, Object array) {
        super(space,establishDataType(array));
        storage = array;


        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = getImageSpace();
        allocateBuffer(space.getNumSamples());

    }

    public BasicImageData3D(IImageSpace3D space, Object array, String imageLabel) {
        super(space,establishDataType(array), imageLabel);
        storage = array;
        data = allocateBuffer(space.getNumSamples());
        planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
        dim0 = space.getDimension(Axis.X_AXIS);
        space3d = getImageSpace();

    }

   



   

    public ImageInfo getImageInfo() {
        // todo is this  what we really want to do?
        return new ImageInfo(this);
    }

    public IImageSpace3D getImageSpace() {
        return (IImageSpace3D)space;

    }

    public final Index3D indexToGrid(int idx) {
        return space3d.indexToGrid(idx);
    }

    public final int indexOf(int x, int y, int z) {
        return (z * planeSize) + dim0 * y + x;
    }

   

    public final double value(float x, float y, float z, InterpolationFunction3D interp) {
        return interp.interpolate(x, y, z, this);
    }




    public final double worldValue(float realx, float realy, float realz, InterpolationFunction3D interp) {
        double x = space3d.worldToGridX(realx, realy, realz);
        double y = space3d.worldToGridY(realx, realy, realz);
        double z = space3d.worldToGridZ(realx, realy, realz);

       
        return interp.interpolate(x, y, z, this);
    }

    public final double value(int index) {
        return data.getElemDouble(index);
    }


    public final double value(int x, int y, int z) {
        return data.getElemDouble(indexOf(x, y, z));
    }


    public final void setValue(int idx, double val) {
        data.setElemDouble(idx, val);
    }

    public final void setValue(int x, int y, int z, double val) {
        data.setElemDouble(indexOf(x, y, z), val);
    }


    public byte[] toBytes() {
        byte[] idat = ArrayUtils.scaleToBytes(storage, minValue(), maxValue(), 255);
        return idat;
    }


    public ImageIterator iterator() {
        return new Iterator3D(this);
    }

    public static class Iterator3D implements ImageIterator {

        private int index;

        private IImageData3D data;

        private IImageSpace3D space;

        private int len;
        private int end;
        private int begin = 0;
        private int planeSize;

        public Iterator3D(IImageData3D _data) {
            data = _data;
            space = data.getImageSpace();
            index = 0;
            planeSize = space.getDimension(Axis.X_AXIS) * space.getDimension(Axis.Y_AXIS);
            len = space.getNumSamples();
            end = len;
        }

        public final double next() {
            double dat = data.value(index);
            index++;
            return dat;
        }

        public final void advance() {
            index++;
        }

        public void set(double val) {
            data.setValue(index, val);
        }

        public double previous() {
            return data.value(--index);
        }

        public final boolean hasNext() {
            if (index < end) return true;
            return false;
        }

        public double jump(int number) {
            index += number;
            return data.value(number);
        }

        public boolean canJump(int number) {
            //todo what on earth?
            if ( ((index + number) < end) && ((index - number) >= begin) )
                return true;
            return false;
        }

        public double nextRow() {
            index += space.getDimension(Axis.X_AXIS);
            return data.value(index);
        }

        public double nextPlane() {
            index += planeSize;
            return data.value(index);
        }

        public boolean hasNextRow() {
            if ((index + space.getDimension(Axis.X_AXIS)) < len)
                return true;
            return false;
        }

        public boolean hasNextPlane() {
            if ((index + planeSize) < len)
                return true;
            return false;
        }

        public boolean hasPreviousRow() {
            if ((index - space.getDimension(Axis.X_AXIS)) >= begin)
                return true;
            return false;
        }

        public boolean hasPreviousPlane() {
            if ((index - planeSize) >= begin)
                return true;
            return false;
        }

        public double previousRow() {
            index -= space.getDimension(Axis.X_AXIS);
            return data.value(index);
        }

        public double previousPlane() {
            index -= planeSize;
            return data.value(index);
        }

        public int index() {
            return index;
        }


    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicImageData3D that = (BasicImageData3D) o;

        if (dim0 != that.dim0) return false;
        if (planeSize != that.planeSize) return false;
        if (!space3d.equals(that.space3d)) return false;
        if (!(hashid() == that.hashid())) return false;



        return true;
    }

    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + planeSize;
        result = 31 * result + dim0;
        result = 31 * result + space3d.hashCode();
        result = 31 * result + (int)hashid();
        return result;
    }

    public String toString() {
        return this.getImageLabel();
    }






    public static void main(String[] args) {
        try {
            IImageData data = TestUtils.quickDataSource("mean-BRB-EPI-001.nii").load();
            Index3D vox = new Index3D(0,0,0);

         

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
