package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jan 4, 2007
 * Time: 9:59:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscreteColorMap extends AbstractColorMap {

    private static final Logger log = Logger.getLogger(DiscreteColorMap.class.getCanonicalName());

    private IntervalLookupTable<ColorInterval> table;

    private Color gapColor = new Color(0, 0, 0, 0);

    public DiscreteColorMap() {
        table = new IntervalLookupTable<ColorInterval>();
    }

    public DiscreteColorMap(IColorMap cmap) {
        table = new IntervalLookupTable<ColorInterval>();

        ListIterator<ColorInterval> iter = cmap.iterator();
        while (iter.hasNext()) {
            ColorInterval ival = iter.next();
            assert ival.getMaximum() <= cmap.getMaximumValue() : "interval max = " + ival.getMaximum() + " cmap max = " + cmap.getMaximumValue();

            int index = iter.nextIndex();
            if (index == cmap.getMapSize()) {
                table.addIntervalToEnd(new ColorInterval(new OpenInterval(ival.getMinimum(), ival.getMaximum()), ival.getColor()));
            } else {
                table.addIntervalToEnd(new ColorInterval(new OpenClosedInterval(ival.getMinimum(), ival.getMaximum()), ival.getColor()));
            }

        }


        assert Double.compare(table.getMaximumValue(), cmap.getMaximumValue()) == 0;
        assert Double.compare(table.getMinimumValue(), cmap.getMinimumValue()) == 0;


        assert!hasGaps();
    }


    public boolean hasGaps() {
        ListIterator<ColorInterval> iter = table.iterator();
        double value = table.getFirstInterval().getMaximum();
        while (iter.hasNext()) {
            Interval ival = iter.next();
            if (value == ival.getMinimum()) {
                value = ival.getMaximum();
                continue;
            } else {
                log.warning("color map has gap at index: " + iter.previousIndex());
                log.warning("interval: " + ival);
                log.warning("boundary: " + value);

            }

        }

        return false;

    }

    public ColorInterval getInterval(int index) {
        assert index >= 0 && index < table.getNumIntervals() : "illegal index " + index;
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

        return gapColor;

    }

    private void setInterval0(int index, double min, double max, Color clr) {
        if (index == (table.getNumIntervals() - 1)) {
            ColorInterval newInterval = new ColorInterval(new OpenInterval(min, max), clr);
            table.setInterval(index, newInterval);
        } else {
            ColorInterval newInterval = new ColorInterval(new OpenClosedInterval(min, max), clr);
            table.setInterval(index, newInterval);
        }

    }

    public void setColor(int index, Color clr) {
        if (index < 0 || index >= table.getNumIntervals()) {
            throw new IllegalArgumentException("illegal index " + index);
        }

        ColorInterval current = table.getInterval(index);
        assert current != null;

        setInterval0(index, current.getMinimum(), current.getMaximum(), clr);

        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
    }

    public void equalizeIntervals(int startIndex, int endIndex) {

        if (startIndex < 0 || endIndex >= getMapSize()) {
            throw new IllegalArgumentException("index range is outside map range: (" + startIndex +
                    ", " + endIndex + "),  map size = " + getMapSize());
        } else if ((endIndex - startIndex) < 1) {
            throw new IllegalArgumentException("index range must be greater than 1!");
        }


        int range = endIndex - startIndex + 1;

        ColorInterval begin = table.getInterval(startIndex);
        ColorInterval end = table.getInterval(endIndex);

        double bucketSize = (end.getMaximum() - begin.getMinimum()) / range;
        double start = begin.getMinimum();

        IntervalLookupTable ntable = new IntervalLookupTable();

        ColorInterval clr = null;
        ColorInterval oldcolor = null;
        for (int i = 0; i < table.getNumIntervals() - 1; i++) {
            oldcolor = getInterval(i);
            if (i < startIndex || i > endIndex) {
                clr = new ColorInterval(new OpenClosedInterval(oldcolor.getMinimum(), oldcolor.getMaximum()), oldcolor.getColor());
            } else {
                clr = new ColorInterval(new OpenClosedInterval(start, start + bucketSize), oldcolor.getColor());
                start = start + bucketSize;
            }

            ntable.addInterval(clr);
        }

        oldcolor = table.getLastInterval();
        clr = new ColorInterval(new OpenInterval(start, start + bucketSize), oldcolor.getColor());
        ntable.addInterval(clr);


        assert(table.getMinimumValue() == ntable.getMinimumValue());
        assert table.getMinimumValue() == ntable.getMinimumValue();

        table = ntable;


        assert hasGaps() == false;

        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }

    public void addInterval(double min, double max, Color clr) {
        if (this.getMapSize() == 0) {
            table.addInterval(new ColorInterval(new OpenInterval(min, max), clr));
        } else {
            ColorInterval ival = table.getLastInterval();
            ColorInterval nlast = new ColorInterval(new OpenInterval(min, max), clr);
            if (nlast.rightAdjacent(nlast)) {
                ival = new ColorInterval(new OpenClosedInterval(ival.getMinimum(), ival.getMaximum()), ival.getColor());
                table.removeLastInterval();
                table.addInterval(ival);
                table.addInterval(nlast);
            } else {
                throw new IllegalArgumentException("non-adjacent interval: " + nlast + " with respect to " + ival);
            }
        }
         //todo fire clip change
         changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }
    public void setInterval(int index, double min, double max, Color clr) {
        if (index < 0 || index >= table.getNumIntervals()) {
            throw new IllegalArgumentException("color map index out of bounds");
        } else if (Double.compare(getMaximumValue(), max) == -1) {
            throw new IllegalArgumentException("interval is out of range: " + "(" + min + ", " + max + ")" +
                    "for map with range: " + table.getLastInterval());
        } else if (min < getMinimumValue()) {
            throw new IllegalArgumentException("interval is out of range");
        }


        setInterval0(index, min, max, clr);

        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
    }


    public void setMapSize(int size) {
        if (size < 1 || size > IColorMap.MAXIMUM_INTERVALS) {
            throw new IllegalArgumentException("DiscreteColorMap.setMapSize(): Size of map cannot be smaller than 1" +
                    " or greater than " + IColorMap.MAXIMUM_INTERVALS);
        }

        int oldSize = getMapSize();

        ColorInterval oldFirst = table.getFirstInterval();
        ColorInterval oldLast = table.getLastInterval();

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

        assert getMapSize() == size;

        changeSupport.firePropertyChange(DiscreteColorMap.MAP_SIZE_PROPERTY, oldSize, getMapSize());

        if (oldFirst.getMaximum() != table.getFirstInterval().getMaximum()) {
            changeSupport.firePropertyChange(DiscreteColorMap.LOW_CLIP_PROPERTY, oldFirst.getMaximum(), getLowClip());
        }

        if (oldLast.getMinimum() != table.getLastInterval().getMinimum()) {
            changeSupport.firePropertyChange(DiscreteColorMap.HIGH_CLIP_PROPERTY, oldLast.getMinimum(), getHighClip());
        }


    }

    private void incrementMapSize() {
        if (table.getNumIntervals() == 0) {
            table.addIntervalToEnd(new ColorInterval(new OpenClosedInterval(getMinimumValue(), getMaximumValue()), Color.WHITE));
            return;
        }


        ColorInterval lastInterval = table.getLastInterval();

        double boundary = lastInterval.getMinimum() + (lastInterval.getMaximum() - lastInterval.getMinimum()) / 2.0;

        ColorInterval penultimate = new ColorInterval(new OpenClosedInterval(lastInterval.getMinimum(), boundary), lastInterval.getColor());
        ColorInterval newLast = new ColorInterval(new OpenInterval(boundary, lastInterval.getMaximum()), lastInterval.getColor().brighter());

        table.removeLastInterval();
        table.addIntervalToEnd(penultimate);
        table.addIntervalToEnd(newLast);


    }

    private void decrementMapSize() {
        if (getMapSize() == 1) {
            throw new IllegalArgumentException("cannot reduce map size to 0");
        }


        ColorInterval lastInterval = table.getLastInterval();
        ColorInterval lastButOne = table.getInterval(table.getNumIntervals() - 2);


        table.removeLastInterval();

        setInterval0(table.getNumIntervals() - 1, lastButOne.getMinimum(),
                lastInterval.getMaximum(), lastButOne.getColor());


    }

    private void chopDown(int newSize) {
        if (!(newSize < table.getNumIntervals())) {
            throw new IllegalArgumentException("argument must be smaller than current map size");
        }

        ColorInterval lastInterval = table.getLastInterval().clone();

        while (table.getNumIntervals() > newSize) {
            table.removeLastInterval();
        }

        ColorInterval ival = table.getLastInterval();
        ColorInterval newLast = new ColorInterval(new OpenInterval(ival.getMinimum(),
                lastInterval.getMaximum()), ival.getColor());

        table.removeLastInterval();
        table.addIntervalToEnd(newLast);

        //equalizeIntervals(0, table.getNumIntervals());

    }

    private void growUp(int newSize) {
        if (!(newSize > table.getNumIntervals())) {
            throw new IllegalArgumentException("argument must be greater than current map size");
        }

        int curSize = getMapSize();

        while (curSize < newSize) {
            incrementMapSize();
            curSize++;
        }


    }




    public void setHighClip(double _highClip) {
        ColorInterval ival = table.getLastInterval();
        if (_highClip > ival.getMaximum()) {
            _highClip = ival.getMaximum();
            //TODO log
            //throw new IllegalArgumentException("highClip cannot exceed global maximum");
        }

        if (_highClip == ival.getMinimum()) return;


        setInterval0(getMapSize() - 1, _highClip, ival.getMaximum(), ival.getColor());
        //highClip = _highClip;

        changeSupport.firePropertyChange(HIGH_CLIP_PROPERTY, ival.getMinimum(), getHighClip());

    }

    public void setLowClip(double _lowClip) {
        ColorInterval ival = table.getFirstInterval();
        if (_lowClip < ival.getMinimum()) {
            _lowClip = ival.getMinimum();
            //TODO log
            //throw new IllegalArgumentException("highClip cannot exceed global maximum");
        }
        if (Double.compare(_lowClip, ival.getMaximum()) == 0) return;


        setInterval0(0, ival.getMinimum(), _lowClip, ival.getColor());
        //lowClip = _lowClip;

        changeSupport.firePropertyChange(LOW_CLIP_PROPERTY, ival.getMaximum(), getLowClip());


    }


    public double getHighClip() {
        return table.getLastInterval().getMinimum();
    }


    public double getLowClip() {
        return table.getFirstInterval().getMaximum();
    }

    public ListIterator<ColorInterval> iterator() {
        return table.iterator();

    }

    public AbstractColorBar createColorBar() {
        return new DiscreteColorBar(this, SwingConstants.HORIZONTAL);

    }

    public Range getRange() {
        return new Range(getMinimumValue(), getMaximumValue());
    }


    public int getMapSize() {
        return table.getNumIntervals();
    }


    public double getMaximumValue() {
        return table.getLastInterval().getMaximum();
    }

    public double getMinimumValue() {
        return table.getFirstInterval().getMinimum();
    }

    public void setUpperAlphaThreshold(double _upperAlphaThreshold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setLowerAlphaThreshold(double _lowerAlphaThreshold) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setAlphaMultiplier(Double amult) {
        //To change body of implemented methods use File | Settings | File Templates.
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
        return new byte[0][];  //To change body of implemented methods use File | Settings | File Templates.
    }


}
