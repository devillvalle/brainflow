package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.utils.Range;
import org.apache.commons.lang.math.NumberUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 10, 2005
 * Time: 12:47:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinearColorMap extends AbstractColorMap {

    private IndexColorModel sourceModel;

    private IndexColorModel derivedModel;

    private double bucketSize;

    private ColorInterval[] clrs;


    public LinearColorMap() {
        this(0, 255, ColorTable.GRAYSCALE);
    }

    public LinearColorMap(double min, double max, IndexColorModel icm) {
        assert max > min : "max must exceed min in LinearColorMap";

        sourceModel = icm;
        derivedModel = sourceModel;
        minimumValue = min;
        maximumValue = max;
        highClip = max;
        lowClip = min;


        int mapSize = sourceModel.getMapSize();
        bucketSize = (max - min) / (mapSize - 1);

        clrs = new ColorInterval[mapSize];
        clrs[0] = new ColorInterval(minimumValue, lowClip, new Color(sourceModel.getRed(0), sourceModel.getGreen(0), sourceModel.getBlue(0), sourceModel.getAlpha(0)));
        clrs[mapSize - 1] = new ColorInterval(highClip, maximumValue, new Color(sourceModel.getRed(mapSize - 1), sourceModel.getGreen(mapSize - 1), sourceModel.getBlue(mapSize - 1), sourceModel.getAlpha(mapSize - 1)));

        double curmin = minimumValue;
        for (int i = 1; i < clrs.length - 1; i++) {
            clrs[i] = new ColorInterval(curmin, curmin + bucketSize, new Color(sourceModel.getRed(i), sourceModel.getGreen(i), sourceModel.getBlue(i), sourceModel.getAlpha(i)));
            curmin = curmin + bucketSize;
        }

        setUpperAlphaThreshold(0);
        setLowerAlphaThreshold(0);


    }


    public void setIndexColorModel(IndexColorModel icm) {
        IndexColorModel oldModel = derivedModel;

        sourceModel = icm;
        derivedModel = icm;
        updateColors();

        changeSupport.firePropertyChange(LinearColorMap.COLORS_CHANGED_PROPERTY, null, sourceModel);

    }


    protected void updateColors() {

        int mapSize = clrs.length;

        //IndexColorModel icm = ColorTable.resampleMap(sourceModel, mapSize);
        IndexColorModel icm = derivedModel;
        bucketSize = (highClip - lowClip) / (mapSize - 1);


        clrs = new ColorInterval[mapSize];

        clrs[0] = new ColorInterval(minimumValue, lowClip,
                new Color(icm.getRed(0), icm.getGreen(0), icm.getBlue(0), icm.getAlpha(0)));
        clrs[mapSize - 1] = new ColorInterval(highClip, maximumValue,
                new Color(icm.getRed(mapSize - 1), icm.getGreen(mapSize - 1), icm.getBlue(mapSize - 1), icm.getAlpha(mapSize - 1)));

        double curmin = lowClip;

        for (int i = 1; i < clrs.length - 1; i++) {

            clrs[i] = new ColorInterval(curmin, curmin + bucketSize,
                    new Color(icm.getRed(i), icm.getGreen(i), icm.getBlue(i), icm.getAlpha(i)));


            curmin = curmin + bucketSize;
        }


    }

    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }

    public IndexColorModel getAsIndexColorModel() {
        return derivedModel;
    }

    public void setBottomColor(Color c) {
        clrs[0] = new ColorInterval(clrs[0].getMinimum(), clrs[0].getMaximum(), c);
    }


    public int getMapSize() {
        return clrs.length;
    }

    public ColorInterval getInterval(int index) {
        assert index >= 0 && index < clrs.length : "index must be between 0 and " + getMapSize();
        return clrs[index];
    }


    public Color getColor(double value) {
        double mapRange = highClip - lowClip;
        int bin = (int) (((value - lowClip) / mapRange) * clrs.length);
        if (bin < 0) bin = 0;
        if (bin >= clrs.length) bin = clrs.length - 1;

        int alpha = 0;
        int blue = clrs[bin].getBlue();
        int green = clrs[bin].getGreen();
        int red = clrs[bin].getRed();

        if (value <= lowerAlphaThreshold || value >= upperAlphaThreshold) {
            alpha = (int) (clrs[bin].getAlpha() * alphaMultiplier);

        }

        return new Color(red, green, blue, alpha);

    }


    public void setMapSize(int _mapSize) {
        int oldValue = getMapSize();
        derivedModel = ColorTable.resampleMap(sourceModel, _mapSize);
        updateColors();

        changeSupport.firePropertyChange(MAP_SIZE_PROPERTY, oldValue, getMapSize());
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
        double oldValue = getAlphaMultiplier();
        alphaMultiplier = Math.min(amult, 1f);
        alphaMultiplier = Math.max(amult, 0f);

        changeSupport.firePropertyChange(AbstractColorMap.ALPHA_MULTIPLIER_PROPERTY,
                oldValue, alphaMultiplier);
    }

    public void setHighClip(double _highClip) {
        double oldValue = getHighClip();

        System.out.println("input range: " + getLowClip() + ", " + _highClip);
        double[] range = super.filterHighValue(getLowClip(), _highClip);
        System.out.println("output range: " + range[0] + ", " + range[1]);
        highClip = range[1];

        updateColors();
        changeSupport.firePropertyChange(AbstractColorMap.HIGH_CLIP_PROPERTY,
                oldValue, highClip);

        if (range[0] != getLowClip()) {
            double oldClip = getLowClip();
            lowClip = range[0];
            changeSupport.firePropertyChange(AbstractColorMap.LOW_CLIP_PROPERTY,
                    oldClip, lowClip);
        }


    }

    public void setLowClip(double _lowClip) {
        double oldValue = getLowClip();
        double[] range = super.filterLowValue(_lowClip, getHighClip());
        lowClip = range[0];

        updateColors();
        changeSupport.firePropertyChange(AbstractColorMap.LOW_CLIP_PROPERTY,
                oldValue, lowClip);

        if (range[1] != getHighClip()) {
            double oldClip = getHighClip();
            highClip = range[1];
            changeSupport.firePropertyChange(AbstractColorMap.HIGH_CLIP_PROPERTY,
                    oldClip, highClip);
        }


    }


    public Iterator<ColorInterval> iterator() {
        List<ColorInterval> clist = Arrays.asList(clrs);
        clist = Collections.unmodifiableList(clist);
        return clist.iterator();
    }

    public Range getRange() {
        return new Range(getMinimumValue(), getMaximumValue());
    }

    private byte[] getThresholdedInterleavedRGBAComponents(IImageData data) {

        int len = data.getNumElements();

        byte[] rgba = new byte[len * 4];

        double mapRange = getHighClip() - getLowClip();
        int offset = 0;
        double mapSize = getMapSize();
        int mapEnd = (int) mapSize - 1;
        //really ABGR

        byte alpha;
        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            double val = iter.next();

            int bin = (int) (((val - lowClip) / mapRange) * mapSize);
            if (bin < 0) bin = 0;
            if (bin >= mapSize) bin = mapEnd;

            alpha = 0;
            if (val < lowerAlphaThreshold || val > upperAlphaThreshold) {
                alpha = (byte) (clrs[bin].getAlpha() * alphaMultiplier);
            }


            rgba[offset++] = alpha;
            rgba[offset++] = (byte) clrs[bin].getBlue();
            rgba[offset++] = (byte) clrs[bin].getGreen();
            rgba[offset++] = (byte) clrs[bin].getRed();
        }
        return rgba;


    }


    public byte[] getInterleavedRGBAComponents(IImageData data) {

        if (NumberUtils.compare(getUpperAlphaThreshold(), getLowerAlphaThreshold()) != 0) {
            return getThresholdedInterleavedRGBAComponents(data);
        }


        int len = data.getNumElements();
        byte[] rgba = new byte[len * 4];

        double mapRange = highClip - lowClip;
        int offset = 0;
        double mapSize = clrs.length;
        //really ABGR

        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            double val = iter.next();

            int bin = (int) (((val - lowClip) / mapRange) * mapSize);
            if (bin < 0) bin = 0;
            if (bin >= mapSize) bin = (int) (mapSize - 1);
            rgba[offset++] = (byte) (clrs[bin].getAlpha() * alphaMultiplier);
            //rgba[offset] = (byte)255;
            //rgba[offset++] = (byte)clrs[bin].getRed();
            rgba[offset++] = (byte) clrs[bin].getBlue();
            rgba[offset++] = (byte) clrs[bin].getGreen();
            rgba[offset++] = (byte) clrs[bin].getRed();
        }
        return rgba;
    }

    public byte[][] getRGBAComponents(IImageData data) {
        // use of DataBuffer not ideal. Should move to commons primitives.

        int len = data.getNumElements();
        byte[][] rgba = new byte[4][len];
        int lastidx = getMapSize() - 1;

        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {

            int i = iter.index();
            double val = iter.next();

            if (val <= lowClip) {
                rgba[0][i] = (byte) clrs[0].getRed();
                rgba[1][i] = (byte) clrs[0].getGreen();
                rgba[2][i] = (byte) clrs[0].getBlue();
                rgba[3][i] = (byte) (clrs[0].getAlpha() * alphaMultiplier);
            } else if (val >= highClip) {
                rgba[0][i] = (byte) clrs[lastidx].getRed();
                rgba[1][i] = (byte) clrs[lastidx].getGreen();
                rgba[2][i] = (byte) clrs[lastidx].getBlue();
                rgba[3][i] = (byte) (clrs[lastidx].getAlpha() * alphaMultiplier);
            } else {
                int bin = (int) Math.round((val - lowClip) / clrs.length);
                rgba[0][i] = (byte) clrs[bin].getRed();
                rgba[1][i] = (byte) clrs[bin].getGreen();
                rgba[2][i] = (byte) clrs[bin].getBlue();
                rgba[3][i] = (byte) (clrs[bin].getAlpha() * alphaMultiplier);

            }
        }

        return rgba;
    }

    public boolean isScaleable() {
        return true;
    }

    public LinearColorBar createColorBar() {
        return new LinearColorBar(this, SwingConstants.HORIZONTAL);
    }

    public String toString() {
        Iterator iterator = Arrays.asList(clrs).iterator();
        StringBuffer sb = new StringBuffer();

        int count = 0;
        while (iterator.hasNext()) {
            ColorInterval cli = (ColorInterval) iterator.next();
            sb.append("Interval " + count + ": " + cli);
            sb.append("\n");
            count++;
        }

        return sb.toString();
    }

    public static void main(String[] args) {
        LinearColorMap cmap = new LinearColorMap(0, 32555, ColorTable.GRAYSCALE);
        System.out.println(cmap.getInterval(0));
        System.out.println(cmap.getInterval(1));

        RaggedColorMap rmap = new RaggedColorMap(cmap);
        System.out.println(rmap.getInterval(0));
        System.out.println(rmap.getInterval(1));


    }


}
