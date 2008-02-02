package com.brainflow.colormap;

import test.Testable;

import javax.swing.*;
import java.awt.*;
import java.awt.image.IndexColorModel;
import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 14, 2007
 * Time: 10:30:24 PM
 * To change this template use File | Settings | File Templates.
 */
public final class LinearColorMap2 extends AbstractColorMap {

    private double binSize;

    private java.util.List<Color> colors;

    private double[] segments;

    private double lowClip;

    private double highClip;


    public LinearColorMap2(double min, double max, IndexColorModel icm) {
        assert max >= min : "max must exceed min in LinearColorMapDeprecated";

        initColors(icm);
        init(min, max, icm.getMapSize());
        initSegments();


    }


    public LinearColorMap2(double min, double max, LinearColorMap2 lcm) {
        assert max >= min : "max must exceed min in LinearColorMapDeprecated";

        colors = lcm.colors;
        init(min, max, lcm.getMapSize());

        initSegments();

      
    }

    public LinearColorMap2(double min, double max, double lowClip, double highClip, LinearColorMap2 lcm) {
        assert max >= min : "max must exceed min in LinearColorMapDeprecated";
        assert highClip > lowClip : "highClip must exceeed lowClip";
        assert highClip < max : "maximum must be greater than highClip";
        assert lowClip > min : "minimum must be less than lowClip";

        this.lowClip = lowClip;
        this.highClip = highClip;
        
        init(min, max, lowClip, highClip, lcm.getMapSize());
        colors = lcm.colors;
        initSegments();


        //fillIntervals(mapSize, lcm);

    }

    public LinearColorMap2(double min, double max, double lowClip, double highClip, IndexColorModel icm) {
        assert max >= min : "max must exceed min in LinearColorMapDeprecated";
        assert highClip > lowClip : "highClip must exceeed lowClip";
        assert highClip < max : "maximum must be greater than highClip";
        assert lowClip > min : "minimum must be less than lowClip";

        this.lowClip = lowClip;
        this.highClip = highClip;

        init(min, max, lowClip, highClip, icm.getMapSize());
        initColors(icm);
        initSegments();


        //fillIntervals(mapSize, lcm);

    }

    private void init(double min, double max, int mapSize) {

        binSize = (getMaximumValue() - getMinimumValue()) / (mapSize - 1);

        highClip = max;
        lowClip = min;

        setMinimumValue(min);
        setMaximumValue(max);



        //init(min, max, lowClip, highClip, mapSize);


    }

     private void init(double min, double max, double lowClip, double highClip, int mapSize) {
        setMinimumValue(min);
        setMaximumValue(max);

        if (highClip > max) throw new IllegalArgumentException("high clip must be less than or equal to max");
        if (lowClip < min) throw new IllegalArgumentException("low clip must be greater than or equal to min");

        binSize = (highClip - lowClip) / (mapSize - 1);
      

    }

    public double getClipRange() {
        return getHighClip() - getLowClip();
    }

    private Interval getBinInterval(int bin) {
        if (bin > (getMapSize() -1) ) throw new IllegalArgumentException("requested bin is greater map size");
        if (bin == (getMapSize()-1) ) return new OpenInterval(segments[bin], getMaximumValue());

        return new OpenInterval(segments[bin], segments[bin+1]);


    }

    private void initSegments() {
        segments = new double[getMapSize()];

        segments[0] = getMinimumValue();
        segments[1] = getLowClip();
        double val = getLowClip();

        for (int i=2; i<segments.length; i++) {
            val = val + binSize;
            segments[i] = val;
        }
    }

    private void initColors(IndexColorModel model) {
        assert model.getMapSize() == getMapSize();
        colors = new ArrayList<Color>(model.getMapSize());
        for (int i=0; i<model.getMapSize(); i++) {
            Color clr = new Color(model.getRed(i), model.getGreen(i),
                            model.getBlue(i), model.getAlpha(i));
            colors.add(clr);
        }
    }


    public AbstractColorBar createColorBar() {
        return new LinearColorBar(this, SwingConstants.HORIZONTAL);
    }

    @Testable
    public Color getColor(double value) {
        int bin = (int) Math.round((((value - getLowClip()) / getClipRange()) * getMapSize()));
        if (bin < 0) bin = 0;
        if (bin >= getMapSize()) bin = getMapSize() - 1;

        return colors.get(bin);
    }

    @Testable
    public double getBinSize() {
         return (getHighClip() - getLowClip()) / (getMapSize() - 3);
    }

    public ColorInterval getInterval(int index) {
        Interval ival = getBinInterval(index);
        return new ColorInterval(ival, colors.get(index));
    }

    public int getMapSize() {
        return colors.size();
    }

    public ListIterator<ColorInterval> iterator() {
        java.util.List<ColorInterval> lst = new ArrayList<ColorInterval>();
        for (int i=0; i<getMapSize(); i++) {
            lst.add(getInterval(i));
        }

        return lst.listIterator();
    }

    public double getHighClip() {
        return highClip;
    }

    public double getLowClip() {
        return lowClip;
    }

    public LinearColorMap2 newClipRange(double lowClip, double highClip) {
        return new LinearColorMap2(getMinimumValue(), getMaximumValue(), lowClip, highClip, this);
    }
}
