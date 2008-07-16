package com.brainflow.colormap;

import com.brainflow.image.Histogram;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.BrainflowException;
import com.brainflow.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import cern.colt.list.DoubleArrayList;
import test.TestUtils;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 13, 2008
 * Time: 6:37:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class HistogramColorBar extends JComponent {

    private IColorMap colorMap;

    private Histogram histogram;

    public HistogramColorBar(IColorMap map, Histogram histogram) {
        this.colorMap = map;
        this.histogram = histogram;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        int width = getWidth();
        int height = getHeight();

        float binSize = (float)width/(float)histogram.getNumBins();

        histogram.computeBins();

        DoubleArrayList binHeights = histogram.computeBins();
        DoubleArrayList binIntervals = histogram.getBinIntervals();

        double minx = binIntervals.get(0);
        double xextent = binIntervals.get(binIntervals.size()-1) - minx;

        double ymax = Math.log(binHeights.get(histogram.getHighestBin()));

        for (int i=0; i<binHeights.size(); i++) {
            double xstart = binIntervals.get(i);
            double xend = binIntervals.get(i+1);

            Color startColor = colorMap.getColor(xstart);
            Color endColor = colorMap.getColor(xend);



            double nxstart = (xstart - minx)/xextent;
            double nxend = (xend - minx)/xextent;

            double y = Math.log(binHeights.get(i));
            double ny = (y/ymax)*(double)height;



            Rectangle2D rect = new Rectangle2D.Double(nxstart*width, height-ny, (nxend-nxstart)*width, ny);

            GradientPaint gp = new GradientPaint((float)(nxstart*width), 0, startColor, (float)(nxend*width), 0, endColor);


            g2.setPaint(gp);

            g2.fill(rect);

            gp = new GradientPaint((float)(nxstart*width), 0, Color.BLACK,
                    (float)(nxstart*width), height,
                    new Color((int)(startColor.getRed()*.85), (int)(startColor.getGreen()*.85), (int)(startColor.getBlue()*.85)));

            g2.setPaint(gp);
            g2.draw(rect);

        }



    }

    public static void main(String[] args) {
        IImageDataSource dataSource = TestUtils.quickDataSource("resources/data/global_mean+orig.HEAD");
        try {
            dataSource.load();
        } catch(BrainflowException e) {
            e.printStackTrace();
        }
        Histogram histo = new Histogram(dataSource.getData(),75);
        histo.ignoreRange(new Range(0,10));
        histo.computeBins();
        IColorMap map = new LinearColorMap2(histo.getMinValue(), histo.getMaxValue(), ColorTable.GRAYSCALE);
        HistogramColorBar bar = new HistogramColorBar(map, histo);
        bar.setPreferredSize(new Dimension(350, 100));
        JFrame jf = new JFrame();
        jf.add(bar, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
