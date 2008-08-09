package com.brainflow.colormap;

import com.brainflow.image.Histogram;
import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.BrainFlowException;
import com.brainflow.utils.Range;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
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

    private double yaxisFraction = 1;

    private int RIGHT_CUSHION = 12;

    private int BOTTOM_CUSHION = 12;

    private int TOP_CUSHION = 4;



    public HistogramColorBar(IColorMap map, Histogram histogram) {
        this.colorMap = map;
        this.histogram = histogram;

    }

    public double getYaxisFraction() {
        return yaxisFraction;
    }

    public void setYaxisFraction(double yaxisFraction) {
        if (yaxisFraction <= 0) throw new IllegalArgumentException("yaxisFraction must be > 0 and <= 1");
        this.yaxisFraction = yaxisFraction;
        repaint();
    }

    public IColorMap getColorMap() {
        return colorMap;
    }

    public void setColorMap(IColorMap colorMap) {
        this.colorMap = colorMap;
        repaint();
    }

    public Histogram getHistogram() {
        return histogram;
    }

    public int getYOffset() {
        return getInsets().top + TOP_CUSHION;
    }

    public int getXOffset() {
        return getInsets().left;
    }

    public double getMinValue() {
        return histogram.getMinValue();
    }

    public double getMaxValue() {
        return histogram.getMaxValue();
    }

    public double valueToLocationX(double value) {
        double perc = (value - histogram.getMinValue())/(histogram.getMaxValue() - histogram.getMinValue());
        return perc*getDataArea().width;
    }

    public double locationToValueX(int loc) {
        double perc = ((double)loc - getXOffset())/getDataArea().getWidth();
        return perc*(histogram.getMaxValue() - histogram.getMinValue()) + histogram.getMinValue();


    }

    public double locationToValueY(int loc) {
        double perc = ((double)loc - getYOffset())/getDataArea().getHeight();
        if (perc < 0 || perc > 1) return -1;

        DoubleArrayList binHeights = histogram.computeBins();
        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        return (1-perc)*ymax;


    }




    public int getBin(double value) {
        return histogram.whichBin(value);
    }
    public double getCount(double value) {
        int bin = histogram.whichBin(value);
        if (bin < 0) return 0;
        return histogram.getCount(bin);
    }

    public void setHistogram(Histogram histogram) {
        this.histogram = histogram;
        repaint();
    }

    public Rectangle getDataArea() {

        Insets insets = getInsets();
        return new Rectangle(insets.left, insets.top + TOP_CUSHION,
                getWidth() - insets.left - insets.right - RIGHT_CUSHION,
                getHeight() - insets.top - insets.bottom - BOTTOM_CUSHION - TOP_CUSHION);
    }





    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        Rectangle dataArea = getDataArea();

        int width = dataArea.width;
        int height = dataArea.height;

        histogram.computeBins();

        DoubleArrayList binHeights = histogram.computeBins();
        DoubleArrayList binIntervals = histogram.getBinIntervals();

        double minx = binIntervals.get(0);
        double xextent = binIntervals.get(binIntervals.size()-1) - minx;

        double ymax = binHeights.get(histogram.getHighestBin()) * yaxisFraction;

        for (int i=0; i<binHeights.size(); i++) {
            double xstart = binIntervals.get(i);
            double xend = binIntervals.get(i+1);

            Color startColor = colorMap.getColor(xstart);
            Color endColor = colorMap.getColor(xend);



            double nxstart = (xstart - minx)/xextent;
            double nxend = (xend - minx)/xextent;

            //double y = Math.log(binHeights.get(i));
            double y = binHeights.get(i);
            double ny = Math.min((y/ymax), 1)*(double)height;


            Rectangle2D rect = new Rectangle2D.Double(dataArea.x + nxstart*width, height-ny + dataArea.y, (nxend-nxstart)*width, ny);

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
        } catch(BrainFlowException e) {
            e.printStackTrace();
        }
        Histogram histo = new Histogram(dataSource.getData(),75);
        histo.ignoreRange(new Range(0,10));
        histo.computeBins();
        IColorMap map = new LinearColorMap2(histo.getMinValue(), histo.getMaxValue(), ColorTable.GRAYSCALE);
        final HistogramColorBar bar = new HistogramColorBar(map, histo);
        bar.setPreferredSize(new Dimension(350, 100));

        JPanel jp = new JPanel();
        jp.setLayout(new BorderLayout());
        jp.add(bar, BorderLayout.CENTER);
        final JSlider slider = new JSlider(JSlider.VERTICAL, 0, 100, 100);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                double frac = (double)slider.getValue()/100.0;
                bar.setYaxisFraction(frac);
            }
        });

        jp.add(slider, BorderLayout.WEST);
        JFrame jf = new JFrame();
        jf.add(jp, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }
}
