/*
 * MeanImageFilter.java
 *
 * Created on March 23, 2003, 12:45 PM
 */

package com.brainflow.image.operations;

import com.brainflow.image.data.BasicImageData;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;

import java.util.List;

/**
 * @author Bradley
 */
public class MeanImageFilter extends AbstractImageFilter {

    /**
     * Creates a new instance of MeanImageFilter
     */
    public MeanImageFilter() {
    }

    public IImageData getOutput() {
        List sources = getSources();
        if (sources.size() == 0)
            throw new RuntimeException("ImageFilter requires at least one input operations");

        BasicImageData first = (BasicImageData) sources.get(0);
        if (first == null) return null;

        BasicImageData opdata = BasicImageData.create(first.getImageSpace(), outputDataType);
        ImageIterator opiter = opdata.iterator();

        ImageIterator[] iters = getSourceIterators();

        while (opiter.hasNext()) {
            double sum = 0;
            for (int i = 0; i < iters.length; i++) {
                sum += iters[i].next();
            }
            double mean = sum / iters.length;
            opiter.set(mean);
            opiter.advance();
        }

        return opdata;
    }
}
        
        
        
        
        
        
        
        
 