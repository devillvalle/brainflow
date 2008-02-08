package com.brainflow.image.data;

import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.DataType;

import java.awt.image.*;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public abstract class BasicImageData extends AbstractImageData {


    protected DataBuffer data;

    protected Object storage;

    protected double minValue;

    protected double maxValue;

    protected boolean recomputeMax = true;

    protected boolean recomputeMin = true;

    protected int identifier = 0;


    public BasicImageData(IImageSpace space) {
        super(space);
    }


    public Object getStorage() {
        return storage;
    }


    protected DataType establishDataType(Object array) {
        if (array instanceof byte[]) datatype = DataType.BYTE;
        else if (array instanceof short[]) datatype = DataType.SHORT;
        else if (array instanceof float[]) datatype = DataType.FLOAT;
        else if (array instanceof int[]) datatype = DataType.INTEGER;
        else if (array instanceof double[]) datatype = DataType.DOUBLE;
        else {
            throw new IllegalArgumentException("BasicImageData: illegal array type: " + array);
        }

        return datatype;
    }

    protected void fillBuffer(Object storage, int size) {
        if (datatype == DataType.BYTE) {
            data = new DataBufferByte((byte[]) storage, size);
        } else if (datatype == DataType.SHORT) {
            data = new DataBufferShort((short[]) storage, size);
        } else if (datatype == DataType.INTEGER) {
            data = new DataBufferInt((int[]) storage, size);
        } else if (datatype == DataType.FLOAT) {
            data = new DataBufferFloat((float[]) storage, size);
        } else if (datatype == DataType.DOUBLE) {
            data = new DataBufferDouble((double[]) storage, size);
        } else {
            throw new IllegalArgumentException("BasicImageData: cannot allocate data of type " + datatype.toString());
        }
    }

    public DataBuffer getDataBuffer() {
        return data;
    }


    protected DataBuffer allocateBuffer(int size) {

        DataBuffer data = null;

        if (datatype == DataType.BYTE) {
            if (storage == null)
                storage = new byte[size];
            data = new DataBufferByte((byte[]) storage, size);
        } else if (datatype == DataType.SHORT) {
            if (storage == null)
                storage = new short[size];
            data = new DataBufferShort((short[]) storage, size);
        } else if (datatype == DataType.INTEGER) {
            if (storage == null)
                storage = new int[size];
            data = new DataBufferInt((int[]) storage, size);
        } else if (datatype == DataType.FLOAT) {
            if (storage == null)
                storage = new float[size];
            data = new DataBufferFloat((float[]) storage, size);
        } else if (datatype == DataType.DOUBLE) {
            if (storage == null)
                storage = new double[size];
            data = new DataBufferDouble((double[]) storage, size);
        } else {
            throw new IllegalArgumentException("BasicImageData: cannot allocate data of type " + datatype.toString());
        }

        return data;
    }

    public double getMaxValue() {
        if (!recomputeMax)
            return maxValue;

        ImageIterator iter = this.iterator();
        double max = -Double.MAX_VALUE;
        while (iter.hasNext()) {
            max = Math.max(iter.next(), max);
        }

        recomputeMax = false;
        maxValue = max;
        return maxValue;
    }

    public double getMinValue() {
        if (!recomputeMin)
            return minValue;
        ImageIterator iter = this.iterator();
        double min = Double.MAX_VALUE;
        while (iter.hasNext()) {
            min = Math.min(iter.next(), min);
        }

        recomputeMin = false;
        minValue = min;
        return minValue;
    }


    public final int getNumElements() {
        return data.getSize();
    }

    public abstract ImageIterator iterator();


    public int hashCode() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf(super.hashCode()));
        sb.append(this.getImageLabel());
        return sb.hashCode();
    }


    public static BasicImageData create(IImageSpace space, DataType type) {
        if (space.getNumDimensions() == 2) {
            return new BasicImageData2D((ImageSpace2D)space, type);
        }
        if (space.getNumDimensions() == 3) {
            return new BasicImageData3D((ImageSpace3D)space, type);
        } else
            throw new IllegalArgumentException("Cannot create BasicImageData with dimensionality " + space.getNumDimensions());
    }


}
