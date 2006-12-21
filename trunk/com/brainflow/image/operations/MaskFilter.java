package com.brainflow.image.operations;

import cern.colt.function.DoubleProcedure;
import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.DataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 1:26:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskFilter extends AbstractImageFilter {

    private List<DoubleProcedure> procList = new ArrayList<DoubleProcedure>();


    public MaskFilter() {
        this.setOutputDataType(DataType.BOOLEAN);
    }

    public void addPredicate(DoubleProcedure predicate) {
        procList.add(predicate);
    }

    public void clearPredicates() {
        procList.clear();
    }


    public IImageData getOutput() {
        List sources = getSources();
        if (sources.size() != 1)
            throw new RuntimeException("ThresholdImageFilter requires one and only one source image");

        BasicImageData first = (BasicImageData) sources.get(0);
        BasicImageData opdata = BasicImageData.create(first.getImageSpace(), outputDataType);

        ImageIterator opiter = opdata.iterator();

        ImageIterator[] iters = getSourceIterators();

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /*public static void main(String[] args) {
        BitSet bset1 = new BitSet(64*64*64);
        BitSet bset2 = new BitSet(64*64*64);
        int len = 64*64*64;

        for (int i=0; i<len; i++) {
            bset1.set(i, );

        }

    }*/
}
