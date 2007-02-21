package com.brainflow.display;

import com.brainflow.application.BrainflowException;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.image.Histogram;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.jfreechart.AxisPointer;
import com.brainflow.jfreechart.HistogramDataset;
import com.brainflow.utils.NumberUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.IndexColorModel;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ColoredHistogram extends JPanel implements MouseListener, MouseMotionListener {

    Histogram histogram;
    HistogramDataset dataset;

    Point lastPressed;
    Rectangle2D dataArea;

    AxisPointer xaxisPointer;

    JFreeChart chart;
    ChartPanel chartPanel;
    NumberAxis hAxis;
    NumberAxis yAxis;

    IColorMap colorModel;

    double xmedian;

    JPanel bottom = new JPanel();
    JPanel sliderPanel1 = new JPanel();
    JPanel sliderPanel2 = new JPanel();
    JLabel ylabel = new JLabel("Count");

    JLabel xlabel1 = new JLabel("min value");
    JLabel xlabel2 = new JLabel("max value");

    JSlider xminSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
    JSlider xmaxSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
    JSlider yBoundsSlider = new JSlider(JSlider.VERTICAL, 0, 100, 100);

    double ybound;


    public ColoredHistogram(Histogram _histogram) {
        setLayout(new BorderLayout());
        histogram = _histogram;

        hAxis = new NumberAxis("");
        hAxis.setAutoRange(false);
        yAxis = new NumberAxis("");
        yAxis.setAutoRange(false);

        setHistogram(histogram);

        chart = ChartFactory.createXYBarChart("", "domain", false, "range",
                dataset, org.jfree.chart.plot.PlotOrientation.VERTICAL, false, false, false);

        hAxis.setUpperMargin(2);
        hAxis.setLowerMargin(2);
        yAxis.setUpperMargin(2);
        yAxis.setLowerMargin(2);


        chart.getXYPlot().setDomainAxis(hAxis);
        chart.getXYPlot().setRangeAxis(yAxis);
        chart.getXYPlot().setDomainCrosshairVisible(true);
        chart.getXYPlot().setDomainGridlinesVisible(false);
        chart.getXYPlot().setRangeGridlinesVisible(false);
        chart.getPlot().setBackgroundPaint(Color.black);


        class MyBarRenderer extends org.jfree.chart.renderer.xy.XYBarRenderer {

            public MyBarRenderer() {
                //setDefaultPaint(Color.orange);

            }


            public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                                 XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis,
                                 XYDataset data, int series, int item, CrosshairState crosshairState, int pass) {


                if (dataset.getBinInterval(item).contains(xaxisPointer.getAxisValue())) {

                    int x = (int) domainAxis.valueToJava2D(xaxisPointer.getAxisValue(), dataArea, plot.getDomainAxisEdge());
                    xaxisPointer.draw(g2, x, (int) dataArea.getMinY());

                    crosshairState.updateCrosshairX(xaxisPointer.getAxisValue());
                }

                int numBins = histogram.getNumBins();
                int colorIdx = (int) ((item + 1) / (double) numBins * 255f);
                System.out.println("numbins: " + numBins);
                System.out.println("coloridx: " + colorIdx);

                Color rgb = colorModel.getInterval(colorIdx).getColor();
                super.setSeriesPaint(series, rgb);
                super.drawItem(g2, state, dataArea, info, plot, domainAxis, rangeAxis, data, series, item, crosshairState, pass);

            }
        }


        chartPanel = new ChartPanel(chart);
        chartPanel.addMouseListener(this);
        chartPanel.addMouseMotionListener(this);


        xaxisPointer = new AxisPointer(this, 10, 10);
        xaxisPointer.setAxisValue((double) hAxis.getLowerBound() + 1);

        chart.getXYPlot().setDomainCrosshairValue(xaxisPointer.getAxisValue());
        chart.getXYPlot().setDomainCrosshairPaint(Color.GREEN);
        MyBarRenderer bren = new MyBarRenderer();

        chart.getXYPlot().setRenderer(bren);
        chart.getXYPlot().setDomainCrosshairVisible(true);


        add(chartPanel, "Center");
        //add(bottom, "South");
        //add(yBoundsSlider, "East");
    }

    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        chartPanel.setPreferredSize(d);
    }

    //public void setColorModel(IColorMap  icm) {
    //    colorModel = icm;

    //}


    public void setHistogram(Histogram histogram) {

        xmedian = histogram.intervalMedian();
        dataset = new HistogramDataset(histogram);

        hAxis.setLowerBound(histogram.getMinValue());
        hAxis.setUpperBound(histogram.getMaxValue());

        ybound = histogram.binMedian() + (2 * histogram.binStandardDeviation());
        yAxis.setUpperBound(ybound);
        yAxis.setLowerBound(0);

        if (chart != null) {
            chart.getXYPlot().setDataset(dataset);
        }

    }

    public double getYBound() {
        return ybound;
    }

    public XYPlot getXYPlot() {
        return chart.getXYPlot();
    }

    public HistogramDataset getDataset() {
        return dataset;
    }

    public ChartPanel getChartPanel() {
        return chartPanel;

    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            double xrange = hAxis.getUpperBound() - hAxis.getLowerBound();
            double nrange = .8 * xrange;
            double hloc = hAxis.java2DToValue((float) e.getX(), dataArea, getXYPlot().getDomainAxisEdge());
            hAxis.setUpperBound(hloc + nrange / 2);
            hAxis.setLowerBound(hloc - nrange / 2);
        }
    }

    public void mousePressed(MouseEvent e) {
        lastPressed = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        chart.getXYPlot().setDomainCrosshairValue(xaxisPointer.getAxisValue());
        xaxisPointer.setDragging(true);
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }


    int paintCount = 0;
    int paintAt = 2;
    boolean dragging = true;

    public void mouseDragged(MouseEvent e) {
        dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();
        if (dataArea == null) {
            return;
        }
        double hloc = hAxis.java2DToValue((float) e.getX(), dataArea, getXYPlot().getDomainAxisEdge());

        if (NumberUtils.equals(xaxisPointer.getAxisValue(), hloc, .5)) {
            xaxisPointer.setDragging(true);

        }
        if (xaxisPointer.isDragging())
            xaxisPointer.setAxisValue(hloc);
        // System.out.println("new hloc is: " + hloc);
        if (paintCount == paintAt) {
            chart.getXYPlot().setDomainCrosshairValue(hloc);
            paintCount = 0;
        } else
            paintCount++;

        //System.out.println("repainting");
    }

    public void setIndexColorModel(IndexColorModel icm) {
        colorModel = new LinearColorMap(colorModel.getMaximumValue(), colorModel.getMaximumValue(), icm);
        chartPanel.repaint();
    }

    public void setColorModel(IColorMap iColorMap) {
        colorModel = iColorMap;
        chartPanel.repaint();
    }


    public void mouseMoved(MouseEvent e) {

    }


    public static void main(String[] args) {
        try {
            IImageData data = AnalyzeIO.readAnalyzeImage("C:/DTI/slopes/bAge.Norm");
            Histogram hist = new Histogram(data, 100);
            ColoredHistogram chist = new ColoredHistogram(hist);
            chist.setColorModel(new LinearColorMap(0, 255, ColorTable.SPECTRUM));
            JFrame frame = new JFrame();
            frame.getContentPane().add(chist);
            frame.pack();
            frame.setVisible(true);
        } catch (BrainflowException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
