package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.NumberUtils;


import javax.swing.*;
import java.util.*;
import java.util.logging.Logger;
import java.awt.Color;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 1, 2007
 * Time: 12:20:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DiscreteColorMap extends AbstractColorMap {

    private static final Logger log = Logger.getLogger(DiscreteColorMap.class.getName());

    private double[] boundaryArray;
    private double[] oldArray;

    private List<ColorInterval> intervals;


    public DiscreteColorMap() {
        boundaryArray = new double[0];
        intervals = new ArrayList<ColorInterval>();

    }

    public DiscreteColorMap(IColorMap cmap) {
        boundaryArray = new double[cmap.getMapSize() + 1];
        ListIterator<ColorInterval> iter = cmap.iterator();
        intervals = new ArrayList<ColorInterval>(cmap.getMapSize());

        boundaryArray[0] = cmap.getInterval(0).getMinimum();
        while (iter.hasNext()) {
            ColorInterval ci = iter.next();
            int i = iter.previousIndex();
            boundaryArray[i + 1] = ci.getMaximum();
            IndexedInterval ival = new IndexedInterval(i + 1, boundaryArray);
            intervals.add(new ColorInterval(ival, ci.getColor()));
        }


    }


    public double getMaximumValue() {
        return boundaryArray[boundaryArray.length - 1];
    }

    public double getMinimumValue() {
        return boundaryArray[0];
    }

    public int getMapSize() {
        return intervals.size();
    }

    public ColorInterval getInterval(int index) {
        return intervals.get(index);
    }

    public Color getColor(double value) {
        int idx = indexOf(value);
        return intervals.get(idx).getColor();
    }

    public void setColor(int index, Color clr) {
        intervals.get(index).setColor(clr);
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

        ColorInterval begin = getInterval(startIndex);
        ColorInterval end = getInterval(endIndex);

        double bucketSize = (end.getMaximum() - begin.getMinimum()) / range;
        double start = begin.getMinimum();

        if (endIndex == (getMapSize()-1)) {
            endIndex--;
        }

        for (int i = startIndex; i <= endIndex; i++) {
            boundaryArray[i + 1] = start + bucketSize;
            start = start + bucketSize;
        }

        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);

    }


    public int indexOf(double value) {
        if (value <= boundaryArray[0]) {
            return 0;
        }
        if (value >= boundaryArray[boundaryArray.length - 1]) {
            return getMapSize() - 1;
        }

        int bottom = 0;
        int top = boundaryArray.length - 1;

        int mid;

        while (top != bottom) {
            mid = (int) ((top + bottom) / 2.0);

            if (value >= boundaryArray[mid] && value < boundaryArray[mid + 1]) {
                return mid;
            } else if (value > boundaryArray[mid]) {
                bottom = mid;
            } else {
                top = mid;
            }
        }

        return -1;
    }

    private boolean isMonotonic() {
        double prev = boundaryArray[0];
        for (int i = 1; i < boundaryArray.length; i++) {
            if (boundaryArray[i] < prev) {
                log.info("i: " + i);
                log.info("prev: " + prev);
                log.info("cur: " + boundaryArray[i]);
                return false;
            }
            prev = boundaryArray[i];
        }

        return true;
    }

    public void setInterval(int index, double min, double max, Color clr) {
        ColorInterval ival = getInterval(index);
        min = Math.max(min, getMinimumValue());
        max = Math.min(max, getMaximumValue());
        if (index == 0) {
            setBoundary(index + 1, max);
        } else if (index == getMapSize() - 1) {
            setBoundary(index, min);
        } else {
            if (!NumberUtils.equals(ival.getMinimum(), min, .0001)) {
                setBoundary(index + 1, min);
            }
            if (!NumberUtils.equals(ival.getMaximum(), max, .0001)) {
                setBoundary(index + 2, max);
            }
        }

        ival.setColor(clr);
        changeSupport.firePropertyChange(COLORS_CHANGED_PROPERTY, null, this);
    }

    public void setBoundary(int boundary, double value) {
        assert boundary > 0 && boundary < (boundaryArray.length - 1);
        assert value >= boundaryArray[0] && value <= boundaryArray[boundaryArray.length - 1];


        double oldValue = boundaryArray[boundary];
        double maxValue = boundaryArray[boundaryArray.length - 1];
        double minValue = boundaryArray[0];

        if (oldArray == null) {
            oldArray = new double[boundaryArray.length];
        }

        System.arraycopy(boundaryArray, 0, oldArray, 0, boundaryArray.length);

        if (value > oldValue) {
            double totalSpan = maxValue - oldValue;
            double difference = value - oldValue;

            int begin = boundary + 1;
            int end = boundaryArray.length - 1;

            boundaryArray[boundary] = value;
            for (int i = begin; i < end; i++) {
                double oldInterval = oldArray[i] - oldArray[i - 1];
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;

                boundaryArray[i] = Math.min(boundaryArray[i - 1] + oldInterval - localfudge, maxValue);

            }
        } else if (value < oldValue) {
            double difference = oldValue - value;
            double totalSpan = oldValue - minValue;


            boundaryArray[boundary] = value;

            for (int i = boundary - 1; i > 0; i--) {
                double oldInterval = oldArray[i + 1] - oldArray[i];
                double perc = oldInterval / totalSpan;
                double localfudge = perc * difference;
                boundaryArray[i] = Math.max(boundaryArray[i + 1] - oldInterval + localfudge, minValue);

            }

        }

        assert isMonotonic() : "not monotonic";


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

    public void setHighClip(double _highClip) {
        ColorInterval ival = intervals.get(intervals.size() - 1);

        if (_highClip > ival.getMaximum()) {
            _highClip = ival.getMaximum();
        }

        if (_highClip == ival.getMinimum()) return;


        setBoundary(boundaryArray.length - 2, _highClip);
        changeSupport.firePropertyChange(HIGH_CLIP_PROPERTY, ival.getMinimum(), getHighClip());

    }

    public void setLowClip(double _lowClip) {
        ColorInterval ival = intervals.get(0);
        if (_lowClip < ival.getMinimum()) {
            _lowClip = ival.getMinimum();

        }

        if (Double.compare(_lowClip, ival.getMaximum()) == 0) return;


        setBoundary(1, _lowClip);

        changeSupport.firePropertyChange(LOW_CLIP_PROPERTY, ival.getMaximum(), getLowClip());


    }

    public double getHighClip() {
        return boundaryArray[boundaryArray.length - 2];
    }


    public double getLowClip() {
        return boundaryArray[1];
    }

    public ListIterator<ColorInterval> iterator() {
        return intervals.listIterator();
    }

    public AbstractColorBar createColorBar() {
        return new DiscreteColorBar(this, SwingUtilities.HORIZONTAL);
    }

    public byte[] getInterleavedRGBAComponents(IImageData data) {

        int len = data.getNumElements();
        byte[] rgba = new byte[len * 4];

        double minValue = getMinimumValue();
        double maxValue = getMaximumValue();

        ColorInterval firstInterval = getInterval(0);
        ColorInterval lastInterval = getInterval(getMapSize() - 1);

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
                Color clr = getColor(val);
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


    public static void main(String[] args) {
        LinearColorMap cmap = new LinearColorMap(0, 255, ColorTable.SPECTRUM);
        DiscreteColorMap tmp = new DiscreteColorMap(cmap);

        tmp.setBoundary(128, 166);
        tmp.setBoundary(128, 88);
        tmp.setBoundary(128, 6);
        tmp.setBoundary(128, 1);

    }
}
