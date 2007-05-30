package com.brainflow.image.data;

import cern.colt.bitvector.BitVector;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.IImageSpace;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: May 2, 2005
 * Time: 1:06:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BinaryImageData extends AbstractImageData {


    protected BitVector bits;


    protected BinaryImageData() {
    }

    protected void allocateBits() {
        bits = new BitVector(space.getNumSamples());

    }

    protected void allocateBits(boolean[] elements) {
        assert space.getNumSamples() == elements.length : "array size does not match ImageSpace dimensions";

        bits = new BitVector(elements.length);
        for (int i = 0; i < elements.length; i++) {
            bits.putQuick(i, elements[i]);
        }


    }

    protected BitVector getBitVector() {
        return bits;
    }

    public abstract BinaryImageData OR(BinaryImageData data);

    public abstract BinaryImageData AND(BinaryImageData data);



    public double getMaxValue() {
        if (bits.cardinality() > 0) return 1;
        else return 0;
    }

    public double getMinValue() {
        if (bits.cardinality() == bits.size()) return 1;
        else return 0;

    }

    public int cardinality() {
        return bits.cardinality();
    }

    public int getNumElements() {
        return bits.size();
    }


    public byte[] toBytes() {
        byte[] b = new byte[bits.size()];
        for (int i = 0; i < b.length; i++) {
            b[i] = bits.get(i) ? (byte) 1 : (byte) 0;
        }

        return b;

    }


    public ImageIterator iterator() {
        throw new UnsupportedOperationException();
    }


}
