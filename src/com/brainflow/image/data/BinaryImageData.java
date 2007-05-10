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


    public BinaryImageData(IImageSpace _space) {
        space = _space;
        bits = new BitVector(space.getNumSamples());
    }

    protected BitVector getBitVector() {
        return bits;
    }

    public abstract BinaryImageData OR(BinaryImageData data);

    public abstract BinaryImageData AND(BinaryImageData data);



    public BinaryImageData(IImageSpace _space, boolean[] elements) {
        assert _space.getNumSamples() == elements.length : "array size does not match ImageSpace dimensions";

        bits = new BitVector(elements.length);
        for (int i = 0; i < elements.length; i++) {
            bits.putQuick(i, elements[i]);
        }

        space = _space;

    }


    public double getMaxValue() {
        if (bits.cardinality() > 0) return 1;
        else return 0;
    }

    public double getMinValue() {
        if (bits.cardinality() == bits.size()) return 1;
        else return 0;

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
