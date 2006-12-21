package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeListener;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Mar 6, 2005
 * Time: 3:23:30 PM
 * To change this template use File | Settings | File Templates.
 */


public class RaggedColorMap extends AbstractColorMap {


    private IntervalLookupTable<ColorInterval> table;


    public RaggedColorMap() {
        table = new IntervalLookupTable<ColorInterval>();

    }

    public RaggedColorMap(IColorMap cmap) {
        table = new IntervalLookupTable<ColorInterval>();

        Iterator<ColorInterval> iter = cmap.iterator();
        while (iter.hasNext()) {
            ColorInterval ival = iter.next();
            assert ival.getMaximum() <= cmap.getMaximumValue() : "interval max = " + ival.getMaximum() + " cmap max = " + cmap.getMaximumValue();

            table.addIntervalToEnd(ival);
        }


        maximumValue = cmap.getMaximumValue();
        minimumValue = cmap.getMinimumValue();
    }

    public void setUpperAlphaThreshold(double _upperAlphaThreshold) {
        double oldValue = getUpperAlphaThreshold();
        double[] range = super.filterHighValue(getLowerAlphaThreshold(), _upperAlphaThreshold);
        upperAlphaThreshold = range[1];


        changeSupport.firePropertyChange(AbstractColorMap.UPPER_ALPHA_PROPERTY,
                oldValue, upperAlphaThreshold);

        if (range[0] != getLowerAlphaThreshold()) {
            double oldThresh = getLowerAlphaThreshold();
            lowerAlphaThreshold = range[0];
            changeSupport.firePropertyChange(AbstractColorMap.LOWER_ALPHA_PROPERTY,
                    oldThresh, lowerAlphaThreshold);
        }

    }


    public void setLowerAlphaThreshold(double _lowerAlphaThreshold) {
        double oldValue = getLowerAlphaThreshold();

        double[] range = super.filterLowValue(_lowerAlphaThreshold, getUpperAlphaThreshold());

        lowerAlphaThreshold = range[0];
        changeSupport.firePropertyChange(AbstractColorMap.LOWER_ALPHA_PROPERTY,
                oldValue, lowerAlphaThreshold);

        if (range[1] != getUpperAlphaThreshold()) {
            double oldThresh = getUpperAlphaThreshold();
            upperAlphaThreshold = range[1];
            changeSupport.firePropertyChange(AbstractColorMap.UPPER_ALPHA_PROPERTY,
                    oldThresh, upperAlphaThreshold);
        }

    }

    public void setAlphaMultiplier(Double amult) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setHighClip(double _highClip) {
        try {
            ColorInterval lastInterval = table.getLastInterval().clone();

            table.removeLastInterval();

            table.addIntervalToEnd(new ColorInterval(_highClip, lastInterval.getMaximum(), lastInterval.getColor()));
            table.suture(table.getNumIntervals() - 1);

            fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());
        } catch (Throwable t) {
            t.printStackTrace();

        }

    }


    public void setLowClip(double _lowClip) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void setColor(int index, Color clr) {
        ColorInterval current = table.getInterval(index);

        assert current != null;

        ColorInterval newInterval = new ColorInterval(current.getMinimum(), current.getMaximum(), clr);
        table.setInterval(index, newInterval);

        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
    }

    public void equalize(int startIndex, int endIndex) {

        if (startIndex < 0 || endIndex > getMapSize()) {
            throw new IllegalArgumentException("index range is outside map range!");
        } else if ((endIndex - startIndex) < 1) {

            throw new IllegalArgumentException("index range must be greater than 1!");
        }

        ColorInterval lastInterval = table.getLastInterval().clone();

        int range = endIndex - startIndex + 1;

        ColorInterval begin = table.getInterval(startIndex);
        ColorInterval end = table.getInterval(endIndex);

        double bucketSize = (end.getMaximum() - begin.getMinimum()) / (range);
        double start = begin.getMinimum();


        for (int i = 0; i < range; i++) {
            ColorInterval oldcolor = this.getInterval(startIndex + i);
            ColorInterval clr = new ColorInterval(start, start + bucketSize,
                    oldcolor.getColor());

            start = start + bucketSize;
            table.setInterval(startIndex + i, clr);
        }

        fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());
        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }

    public int squeezeInterval(int index, ColorInterval ival) {
        ColorInterval current = table.getInterval(index);
        if (current.equals(ival))
            return index;
        else if (table.getNumIntervals() == 1) {
            ColorInterval lastInterval = table.getLastInterval().clone();
            table.setInterval(index, ival);
            fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());
            return index;
        } else {
            ColorInterval lastInterval = table.getLastInterval().clone();
            int idx = table.squeezeInterval(ival);
            fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());
            return idx;
        }
    }

    private void fireHighClipChangeIfNecessary(ColorInterval oldHigh, ColorInterval newHigh) {
        if (Double.compare(newHigh.getMinimum(), oldHigh.getMinimum()) != 0) {
            changeSupport.firePropertyChange(HIGH_CLIP_PROPERTY, oldHigh.getMinimum(), newHigh.getMinimum());
        }

        /* if (bottomInterval.getMaximum() != table.getFirstInterval().getMaximum()) {
            ColorInterval oldBottom = bottomInterval;
            bottomInterval = table.getFirstInterval().clone();
            changeSupport.firePropertyChange(LOW_CLIP_PROPERTY, oldBottom.getMaximum(), bottomInterval.getMaximum());
        }  */
    }

    public void addInterval(ColorInterval ival) {
        ColorInterval oldHigh = table.getLastInterval().clone();
        table.addInterval(ival);
        fireHighClipChangeIfNecessary(oldHigh, table.getLastInterval());
    }

    private void decrementMapSize() {

        if (getMapSize() == 1) {
            return;
        }


        ColorInterval lastInterval = table.getLastInterval().clone();

        ColorInterval lastButOne = table.getInterval(table.getNumIntervals() - 2);

        ColorInterval newLast = new ColorInterval(lastButOne.getMinimum(),
                lastInterval.getMaximum(), lastButOne.getColor());

        table.removeLastInterval();
        table.removeLastInterval();

        table.addInterval(newLast);

        fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());


    }

    private void incrementMapSize() {
        ColorInterval lastInterval = table.getLastInterval().clone();
        ColorInterval lastButOne = table.getInterval(table.getNumIntervals() - 2);

        double increment = lastButOne.getMaximum() - lastButOne.getMinimum();
        if ((increment + lastButOne.getMaximum()) > lastInterval.getMaximum()) {
            increment = lastInterval.getMaximum() - lastButOne.getMaximum();
        }


        double newMax = lastButOne.getMaximum() + increment;

        ColorInterval newInterval = new ColorInterval(lastButOne.getMaximum(),
                newMax, lastButOne.getColor());

        ColorInterval newLast = new ColorInterval(newMax,
                getMaximumValue(), lastInterval.getColor());


        table.removeLastInterval();

        table.addInterval(newInterval);
        table.addInterval(newLast);

        fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());


    }

    private void chopDown(int newSize) {
        if (!(newSize < table.getNumIntervals())) {
            throw new IllegalArgumentException("argument must be smaller than current map size");
        }

        ColorInterval lastInterval = table.getLastInterval().clone();

        while (table.getNumIntervals() >= newSize) {
            table.removeLastInterval();
        }

        ColorInterval ival = table.getLastInterval();
        ColorInterval newLast = new ColorInterval(ival.getMinimum(),
                lastInterval.getMaximum(), ival.getColor());

        table.removeLastInterval();
        table.addIntervalToEnd(newLast);

        fireHighClipChangeIfNecessary(lastInterval, table.getLastInterval());

    }

    private void growUp(int newSize) {
        if (!(newSize > table.getNumIntervals())) {
            throw new IllegalArgumentException("argument must be smaller than current map size");
        }
        int curSize = getMapSize();

        while (curSize < newSize) {
            incrementMapSize();
            curSize++;
        }

    }

    public void setMapSize(int size) {
        if (size < 1 || size > IColorMap.MAXIMUM_INTERVALS) {
            throw new IllegalArgumentException("RaggedColorMap.setMapSize(): Size of map cannot be smaller than 1" +
                    " or greater than " + IColorMap.MAXIMUM_INTERVALS);
        }

        if (size == (getMapSize() + 1)) {
            incrementMapSize();
            changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
        } else if (size == (getMapSize() - 1)) {
            decrementMapSize();
            changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
        } else if (size < getMapSize()) {
            chopDown(size);
            changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
        } else if (size > getMapSize()) {
            growUp(size);
            changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

        }


    }

    // these methods are not wrong and should be removed
    public RaggedColorMap extendHigher(double highValue, Color clr) {
        assert highValue > getMaximumValue() : "RaggedColorMap.extendHigher(): New end value must be greater than current maximum";
        addInterval(new ColorInterval(getMaximumValue(), highValue, clr));
        return this;
    }

    public RaggedColorMap extendLower(double lowValue, Color clr) {
        assert lowValue < getMinimumValue() : "RaggedColorMap.extendLower(): New end value must be less than current minimum";
        addInterval(new ColorInterval(lowValue, getMinimumValue(), clr));
        return this;

    }


    public double getHighClip() {
        return table.getLastInterval().getMinimum();
    }


    public double getLowClip() {
        return table.getFirstInterval().getMaximum();

    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void addIntervals(ColorInterval[] ivals) {
        for (ColorInterval i : ivals) {
            addInterval(i);
        }
    }


    public void addInterval(double min, double max, Color clr) {
        assert max > min : "RaggedColorMap.addInterval: max must be greater than min.";
        addInterval(new ColorInterval(min, max, clr));
    }

    public int getMapSize() {
        return table.getNumIntervals();
    }

    public ColorInterval getInterval(int index) {
        return table.getInterval(index);
    }

    public Color getColor(double value) {

        ColorInterval cval = table.lookup(value);
        if (cval != null) return cval.getColor();

        if (value <= table.getFirstInterval().getMinimum()) {
            return table.getFirstInterval().getColor();
        }
        if (value >= table.getLastInterval().getMaximum()) {
            return table.getLastInterval().getColor();
        }

        // this could occur if there were "gaps" in the map.
        // probably should have a color called "gap color".

        throw new AssertionError("Should never get here: value " + value + " not found in map: " + toString());

    }

    public double getMaximumValue() {
        return table.getLastInterval().getMaximum();
    }

    public double getMinimumValue() {
        return table.getFirstInterval().getMinimum();
    }

    public Iterator<ColorInterval> iterator() {
        return table.iterator();

    }

    public Range getRange() {
        return new Range(getMinimumValue(), getMaximumValue());
    }


    public boolean isScaleable() {
        return false;
    }

    public String toString() {
        Iterator iterator = table.iterator();
        StringBuffer sb = new StringBuffer();

        int count = 0;
        while (iterator.hasNext()) {
            ColorInterval cli = (ColorInterval) iterator.next();
            sb.append("Interval ").append(count).append(": ").append(cli);
            sb.append("\n");
            count++;
        }

        return sb.toString();
    }


    public byte[] getInterleavedRGBAComponents(IImageData data) {

        int len = data.getNumElements();
        byte[] rgba = new byte[len * 4];

        double minValue = table.getMinimumValue();
        double maxValue = table.getMaximumValue();

        ColorInterval lastInterval = table.getLastInterval();
        ColorInterval firstInterval = table.getFirstInterval();

        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            int i = iter.index();
            double val = iter.next();
            int offset = i * 4;
            if (val <= minValue) {
                rgba[offset] = (byte) firstInterval.getAlpha();
                rgba[offset + 1] = (byte) firstInterval.getBlue();
                rgba[offset + 2] = (byte) firstInterval.getGreen();
                rgba[offset + 3] = (byte) firstInterval.getRed();
            } else if (val >= maxValue) {
                rgba[offset] = (byte) lastInterval.getAlpha();
                rgba[offset + 1] = (byte) lastInterval.getBlue();
                rgba[offset + 2] = (byte) lastInterval.getGreen();
                rgba[offset + 3] = (byte) lastInterval.getRed();
            } else {
                ColorInterval clr = table.lookup(val);
                rgba[offset] = (byte) clr.getAlpha();
                rgba[offset + 1] = (byte) clr.getBlue();
                rgba[offset + 2] = (byte) clr.getGreen();
                rgba[offset + 3] = (byte) clr.getRed();
            }
        }

        return rgba;
    }


    public byte[][] getRGBAComponents(IImageData data) {
        return null;
    }

    /*public IntervalColorModel createIntervalColorModel(String name) {
        int mapSize = getMapSize();

        int[] reds = new int[mapSize];
        int[] greens = new int[mapSize];
        int[] blues = new int[mapSize];
        int[] alphas = new int[mapSize];

        for (int i=0; i<mapSize; i++) {
            ColorInterval cli = getInterval(i);
            reds[i] = cli.getRed();
            greens[i] = cli.getGreen();
            blues[i] = cli.getBlue();
            alphas[i] = cli.getAlpha();
        }


        IntervalColorModel icm = new IntervalColorModel(new DoubleRange(getMinimumValue(), getMaximumValue()),
                ColorTable.resampleMap(256, reds,greens,blues,alphas), name);

        return icm;

    }      */


    public AbstractColorBar createColorBar() {
        return new RaggedColorBar(this, SwingConstants.HORIZONTAL);
    }

    public static void main(String[] args) {
        RaggedColorMap cmap = new RaggedColorMap();
        cmap.addInterval(new ColorInterval(1.65, 1.97, new Color(1, 3, 5)));
        cmap.extendHigher(2.6, new Color(255, 122, 1));
        cmap.extendHigher(2.8, new Color(3, 6, 7));
        cmap.extendHigher(3.35, new Color(5, 7, 8));
        cmap.extendHigher(3.6, new Color(5, 5, 5));
        cmap.extendHigher(4, new Color(122, 111, 111));
        cmap.extendLower(-2, new Color(3, 3, 3));
        System.out.println(cmap.toString());


    }


}
