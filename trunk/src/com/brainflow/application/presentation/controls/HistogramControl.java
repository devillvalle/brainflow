package com.brainflow.application.presentation.controls;

import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.HistogramColorBar;
import com.brainflow.image.Histogram;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 27, 2008
 * Time: 4:19:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramControl extends JPanel {

    private Histogram histogram;

    private IColorMap colorMap;

    private HistogramColorBar colorBar;

    private JSlider slider;

    public HistogramControl(IColorMap map, Histogram histogram) {
        this.colorMap = map;
        this.histogram = histogram;

        colorBar = new HistogramColorBar(map, histogram);
        slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);

        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double frac = (double)slider.getValue()/100.0;
                colorBar.setYaxisFraction(frac);
            }
        });

        setLayout(new BorderLayout());
        add(colorBar, BorderLayout.CENTER);
        add(slider, BorderLayout.WEST);
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        colorBar.setColorMap(colorMap);
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
    }
}
