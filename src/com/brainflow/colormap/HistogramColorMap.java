package com.brainflow.colormap;

import com.brainflow.image.Histogram;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 4, 2008
 * Time: 11:38:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramColorMap extends JPanel {

    private Histogram histogram;

    private IColorMap colorMap;

    public HistogramColorMap(Histogram histogram, IColorMap cmap) {
        this.histogram = histogram;
        colorMap = cmap;
    }

    protected void printComponent(Graphics g) {
        super.printComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
