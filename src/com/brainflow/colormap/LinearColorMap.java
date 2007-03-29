package com.brainflow.colormap;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.RGBAImage;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.UByteImageData2D;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.utils.NumberUtils;
import com.brainflow.utils.Range;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.Annotations;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.XStream;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Mar 10, 2005
 * Time: 12:47:22 PM
 * To change this template use File | Settings | File Templates.
 */



public class LinearColorMap extends AbstractColorMap {


    private double binSize;

    private double mapRange;

    private List<ColorInterval> intervals;


    public LinearColorMap() {
        this(0, 255, ColorTable.GRAYSCALE);
    }

    public LinearColorMap(double min, double max, IndexColorModel icm) {
        assert max > min : "max must exceed min in LinearColorMap";


        minimumValue = min;
        maximumValue = max;
        highClip = max;
        lowClip = min;
        mapRange = highClip - lowClip;

        int mapSize = icm.getMapSize();
        fillIntervals(mapSize, icm);




    }


    public LinearColorMap(double min, double max, LinearColorMap lcm) {
        assert max > min : "max must exceed min in LinearColorMap";


        minimumValue = min;
        maximumValue = max;
        highClip = max;
        lowClip = min;
        mapRange = highClip - lowClip;

        int mapSize = lcm.getMapSize();
        fillIntervals(mapSize, lcm);




    }


    public LinearColorMap copy() {
        LinearColorMap cmap = new LinearColorMap(this.getMinimumValue(), this.getMaximumValue(), this);


        cmap.setHighClip(getHighClip());
        cmap.setLowClip(getLowClip());


        return cmap;

    }

    private void fillIntervals(int mapSize, LinearColorMap lcm) {
        //todo grotesque code duplicatation
        ColorInterval[] colors = new ColorInterval[mapSize];
        binSize = (getHighClip() - getLowClip()) / (mapSize - 1);
        colors = new ColorInterval[mapSize];

        Color c0 = lcm.getInterval(0).getColor();
        colors[0] = new ColorInterval(new OpenClosedInterval(minimumValue, lowClip),
                new Color(c0.getRed(), c0.getGreen(),
                        c0.getBlue(), c0.getAlpha()));

        Color cn = lcm.getInterval(lcm.getMapSize() - 1).getColor();
        colors[mapSize - 1] = new ColorInterval(new OpenInterval(highClip, maximumValue),
                new Color(cn.getRed(), cn.getGreen(),
                        cn.getBlue(), cn.getAlpha()));

        double curmin = getLowClip();
        for (int i = 1; i < colors.length - 2; i++) {
            Color ci = lcm.getInterval(i).getColor();
            colors[i] = new ColorInterval(new OpenClosedInterval(curmin, curmin + binSize),
                    new Color(ci.getRed(), ci.getGreen(),
                            ci.getBlue(), ci.getAlpha()));
            curmin = curmin + binSize;
        }

        int penultimate = colors.length - 2;
        Color cp = lcm.getInterval(penultimate).getColor();
        colors[penultimate] = new ColorInterval(new OpenClosedInterval(curmin, highClip),
                new Color(cp.getRed(), cp.getGreen(),
                        cp.getBlue(), cp.getAlpha()));

        intervals = new ArrayList<ColorInterval>(Arrays.asList(colors));

    }

    private void fillIntervals(int mapSize, IndexColorModel sourceModel) {
        //todo grotesque code duplicatation

        ColorInterval[] colors = new ColorInterval[mapSize];
        binSize = (getHighClip() - getLowClip()) / (mapSize - 1);
        colors = new ColorInterval[mapSize];
        colors[0] = new ColorInterval(new OpenClosedInterval(minimumValue, lowClip),
                new Color(sourceModel.getRed(0), sourceModel.getGreen(0),
                        sourceModel.getBlue(0), sourceModel.getAlpha(0)));
        colors[mapSize - 1] = new ColorInterval(new OpenInterval(highClip, maximumValue),
                new Color(sourceModel.getRed(mapSize - 1), sourceModel.getGreen(mapSize - 1),
                        sourceModel.getBlue(mapSize - 1), sourceModel.getAlpha(mapSize - 1)));

        double curmin = getLowClip();
        for (int i = 1; i < colors.length - 2; i++) {
            colors[i] = new ColorInterval(new OpenClosedInterval(curmin, curmin + binSize),
                    new Color(sourceModel.getRed(i), sourceModel.getGreen(i),
                            sourceModel.getBlue(i), sourceModel.getAlpha(i)));
            curmin = curmin + binSize;
        }

        int penultimate = colors.length - 2;
        colors[penultimate] = new ColorInterval(new OpenClosedInterval(curmin, highClip),
                new Color(sourceModel.getRed(penultimate), sourceModel.getGreen(penultimate),
                        sourceModel.getBlue(penultimate), sourceModel.getAlpha(penultimate)));

        intervals = new ArrayList<ColorInterval>(Arrays.asList(colors));

    }



    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }



    public int getMapSize() {
        return intervals.size();
    }

    public ColorInterval getInterval(int index) {
        return intervals.get(index);
    }


    public Color getColor(double value) {
        int bin = (int) (((value - getLowClip()) / mapRange) * getMapSize());
        if (bin < 0) bin = 0;
        if (bin >= getMapSize()) bin = intervals.size() - 1;

        return intervals.get(bin).getColor();

    }




    /*public void setUpperAlphaThreshold(double _upperAlphaThreshold) {
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


    } */

    public void setHighClip(double _highClip) {
        double oldValue = getHighClip();


        double[] range = super.filterHighValue(getLowClip(), _highClip);

        highClip = range[1];


        changeSupport.firePropertyChange(AbstractColorMap.HIGH_CLIP_PROPERTY,
                oldValue, highClip);

        if (range[0] != getLowClip()) {
            
            double oldClip = getLowClip();
            lowClip = range[0];
            changeSupport.firePropertyChange(AbstractColorMap.LOW_CLIP_PROPERTY,
                    oldClip, lowClip);
        }

        mapRange = getHighClip() - getLowClip();


    }

    /*public void setLowerAlphaThreshold(double _lowerAlphaThreshold) {
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

    } */

    public void setLowClip(double _lowClip) {
        double oldValue = getLowClip();


        double[] range = super.filterLowValue(_lowClip, getHighClip());

        lowClip = range[0];


        changeSupport.firePropertyChange(AbstractColorMap.LOW_CLIP_PROPERTY,
                oldValue, lowClip);


        if (range[1] != getHighClip()) {
            
            double oldClip = getHighClip();
            highClip = range[1];

            changeSupport.firePropertyChange(AbstractColorMap.HIGH_CLIP_PROPERTY,
                    oldClip, highClip);
        }

        mapRange = getHighClip() - getLowClip();


    }



    public ListIterator<ColorInterval> iterator() {
        return intervals.listIterator();
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

            Color clr = intervals.get(bin).getColor();

            rgba[offset++] = (byte)clr.getAlpha();
            rgba[offset++] = (byte) clr.getBlue();
            rgba[offset++] = (byte) clr.getGreen();
            rgba[offset++] = (byte) clr.getRed();
        }
        return rgba;


    }


    public byte[] getInterleavedRGBAComponents(IImageData data) {


        int len = data.getNumElements();
        byte[] rgba = new byte[len * 4];

        double mapRange = highClip - lowClip;
        int offset = 0;
        double mapSize = intervals.size();
        //really ABGR

        ImageIterator iter = data.iterator();
        while (iter.hasNext()) {
            double val = iter.next();

            int bin = (int) (((val - lowClip) / mapRange) * mapSize);
            if (bin < 0) bin = 0;
            if (bin >= mapSize) bin = (int) (mapSize - 1);

            ColorInterval clr = intervals.get(bin);
            rgba[offset++] = (byte) clr.getAlpha();
            rgba[offset++] = (byte) clr.getBlue();
            rgba[offset++] = (byte) clr.getGreen();
            rgba[offset++] = (byte) clr.getRed();
        }
        return rgba;
    }

    public byte[][] getRGBAComponents(IImageData data) {
       int len = data.getNumElements();
       byte[][] rgba = new byte[4][len];
       int lastidx = getMapSize() - 1;

        ImageIterator iter = data.iterator();

        Color c0 = getInterval(0).getColor();
        Color cn = getInterval(lastidx).getColor();


        while (iter.hasNext()) {

            int i = iter.index();
            double val = iter.next();

            if (val <= lowClip) {
                rgba[0][i] = (byte) c0.getRed();
                rgba[1][i] = (byte) c0.getGreen();
                rgba[2][i] = (byte) c0.getBlue();
                rgba[3][i] = (byte) c0.getAlpha();
            } else if (val >= highClip) {
                rgba[0][i] = (byte) cn.getRed();
                rgba[1][i] = (byte) cn.getGreen();
                rgba[2][i] = (byte) cn.getBlue();
                rgba[3][i] = (byte) cn.getAlpha();
            } else {
                int bin = (int) Math.round((val - lowClip) / intervals.size());
                Color ci = getInterval(bin).getColor();
                rgba[0][i] = (byte) ci.getRed();
                rgba[1][i] = (byte) ci.getGreen();
                rgba[2][i] = (byte) ci.getBlue();
                rgba[3][i] = (byte) ci.getAlpha();

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
        Iterator<ColorInterval> iterator = iterator();
        StringBuffer sb = new StringBuffer();

        int count = 0;
        while (iterator.hasNext()) {
            ColorInterval cli = iterator.next();
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


		XStream stream = new XStream(new DomDriver());
		Annotations.configureAliases(stream, LinearColorMap.class);

		System.out.println(stream.toXML(cmap));
	}





}
