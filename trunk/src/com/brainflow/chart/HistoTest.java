package com.brainflow.chart;

import com.brainflow.image.Histogram;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.display.ColoredHistogram;
import com.brainflow.application.TestUtils;
import com.brainflow.colormap.LinearColorMap2;
import com.brainflow.colormap.ColorTable;

import javax.swing.*;
import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 8, 2008
 * Time: 6:22:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistoTest extends JPanel {

    Histogram histogram;

    public HistoTest(Histogram histo) {
        histogram = histo;
        buildDataSet();
    }

    private void buildDataSet() {


        //HistogramDatasetX dataset = new HistogramDatasetX(histogram);
        ColoredHistogram chist = new ColoredHistogram(histogram);
        LinearColorMap2 lmap = new LinearColorMap2(histogram.getMinValue(), histogram.getMaxValue(), ColorTable.SPECTRUM);
        chist.setColorModel(lmap);
        add(chist, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        IImageDataSource dataSource = TestUtils.quickDataSource("resources/data/global_mean+orig.HEAD");
        Histogram histo = new Histogram(dataSource.getData(),30);

        JFrame jf = new JFrame();


        jf.add(new HistoTest(histo));
        jf.pack();
        jf.setVisible(true);
    }
}
