package com.brainflow.image;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.ArrayUtils;
import com.brainflow.utils.Range;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class Histogram {

    private int numBins;
    private IImageData data;
    private double binSize;

    private DoubleArrayList binList;
    private DoubleArrayList binIntervals;

    private double minValue;
    private double maxValue;

    private Range ignore;

    private boolean computed;

    public Histogram(IImageData _data, int _numBins) {
        data = _data;
        numBins = _numBins;
    }

    public int getNumBins() {
        return numBins;
    }

    public double[] getBins() {
        if (computed)
            return binList.elements();

        maxValue = data.getMaxValue();
        minValue = data.getMinValue();

        int[] bins = new int[numBins];

        binSize = (maxValue - minValue) / numBins;
        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            double val = iter.next();
            if (ignore != null)
                if (ignore.contains(val))
                    continue;

            int nbin = (int) ((val - minValue) / binSize);
            if (nbin >= bins.length)
                nbin = bins.length - 1;

            bins[nbin]++;
        }
        computed = true;
        binList = new DoubleArrayList(ArrayUtils.castToDoubles(bins));
        getBinIntervals();
        binList.trimToSize();
        return binList.elements();
    }

    public void ignoreRange(Range range) {
        ignore = range;
    }


    public double[] getBinIntervals() {
        if (binIntervals == null) {
            double[] intervals = new double[numBins + 1];
            intervals[0] = minValue;
            intervals[intervals.length - 1] = maxValue;
            for (int i = 1; i < intervals.length - 1; i++) {
                intervals[i] = intervals[i - 1] + binSize;
            }
            binIntervals = new DoubleArrayList(intervals);
            return intervals;
        }

        binIntervals.trimToSize();
        return binIntervals.elements();
    }

    public int getHighestBin() {
        return binList.indexOf(Descriptive.max(binList));
    }

    public double binMean() {
        if (!computed)
            getBins();
        return Descriptive.mean(binList);
    }

    public double binStandardDeviation() {
        if (!computed)
            getBins();
        return Math.sqrt(Descriptive.sampleVariance(binList, binMean()));
    }

    public double binMedian() {
        if (!computed)
            getBins();
        DoubleArrayList sortedBins = binList.copy();
        sortedBins.sort();
        return Descriptive.median(sortedBins);
    }

    public double intervalMedian() {
        if (!computed)
            getBins();
        return Descriptive.median(binIntervals);
    }

    public double getMinValue() {
        return data.getMinValue();
    }

    public double getMaxValue() {
        return data.getMaxValue();
    }


}