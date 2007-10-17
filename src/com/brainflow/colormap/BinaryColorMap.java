package com.brainflow.colormap;

import javax.swing.*;
import java.awt.*;
import java.util.ListIterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 14, 2007
 * Time: 5:21:41 PM
 * To change this template use File | Settings | File Templates.
 */
public final class BinaryColorMap extends AbstractColorMap {


    private final Color backgroundColor = new Color(0,0,0,0);

    private final Color foregroundColor;

    public BinaryColorMap(Color color) {
        this.foregroundColor = color;
    }

    public AbstractColorBar createColorBar() {

        return new DiscreteColorBar(this, SwingUtilities.HORIZONTAL);

    }

    public Color getColor(double value) {
        if (value > 0) {
            return foregroundColor;
        } else {
            return backgroundColor;
        }
    }

    public ColorInterval getInterval(int index) {
        if (index == 0) return new ColorInterval(new OpenInterval(0,0), Color.BLACK);
        else if (index == 1) return new ColorInterval(new OpenInterval(1,1),foregroundColor);
        else {
            throw new IllegalArgumentException("index out of bounds");
        }
    }

    public int getMapSize() {
        return 2;
    }

    public ListIterator<ColorInterval> iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public IColorMap newClipRange(double lowClip, double highClip) {
        return this;
    }
}
