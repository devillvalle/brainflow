package com.brainflow.application.presentation;

import com.brainflow.colormap.*;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.display.ColorBandChart;

import net.java.dev.properties.BaseProperty;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.IndexColorModel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 4, 2006
 * Time: 12:54:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class ColorBandChartPresenter extends ImageViewPresenter {

    private IColorMap colorMap;

    private JPanel panel;

    private ColorBandChart chartRed;

    private ColorBandChart chartGreen;

    private ColorBandChart chartBlue;

    private ColorBandChart chartAlpha;


    private ChangeHandler changeHandler = new ChangeHandler();

    public ColorBandChartPresenter(IColorMap parameter) {
        colorMap = parameter;
        buildGUI();
    }


    public JComponent getComponent() {
        return panel;
    }

    private void buildGUI() {
        panel = new JPanel();


        chartRed = new ColorBandChart(ColorBandChart.ColorBand.RED, colorMap);
        chartRed.getComponent().setPreferredSize(new Dimension(325, 125));
        chartRed.addChangeListener(changeHandler);
        chartGreen = new ColorBandChart(ColorBandChart.ColorBand.GREEN, colorMap);
        chartGreen.getComponent().setPreferredSize(new Dimension(325, 125));
        chartGreen.addChangeListener(changeHandler);
        chartBlue = new ColorBandChart(ColorBandChart.ColorBand.BLUE, colorMap);
        chartBlue.getComponent().setPreferredSize(new Dimension(325, 125));
        chartBlue.addChangeListener(changeHandler);
        chartAlpha = new ColorBandChart(ColorBandChart.ColorBand.ALPHA, colorMap);
        chartAlpha.getComponent().setPreferredSize(new Dimension(325, 125));
        chartAlpha.addChangeListener(changeHandler);

        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(new ColorBarPlot(colorMap));
        //panel.add(Box.createVerticalGlue());

        panel.add(chartRed.getComponent());
        panel.add(chartGreen.getComponent());
        panel.add(chartBlue.getComponent());
        panel.add(chartAlpha.getComponent());
    }

   

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }




    class ChangeHandler implements ChangeListener {

        public void stateChanged(ChangeEvent e) {

            byte[] reds = chartRed.getBandData();
            byte[] greens = chartGreen.getBandData();
            byte[] blues = chartBlue.getBandData();
            byte[] alpha = chartAlpha.getBandData();

            IndexColorModel icm = new IndexColorModel(8, 256, reds, greens, blues, alpha);
            colorMap = new LinearColorMap2(colorMap.getMinimumValue(), colorMap.getMaximumValue(), icm);
            ImageLayer layer = getSelectedView().getModel().getSelectedLayer();
            layer.getImageLayerProperties().colorMap.set(colorMap);

        }
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();


        jf.add(new ColorBandChartPresenter(new LinearColorMapDeprecated(0, 300, ColorTable.SPECTRUM)).getComponent());
        jf.pack();
        jf.setVisible(true);

    }


}
