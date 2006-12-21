/*
 * ColorBandChart.java
 *
 * Created on July 20, 2006, 12:02 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.display;

import com.brainflow.colormap.ColorTable;
import com.brainflow.jfreechart.DynamicSplineXYDataset;
import com.brainflow.jfreechart.DynamicXYDataset;
import com.brainflow.utils.ArrayUtils;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;


/**
 * @author buchs
 */
public class ColorBandChart implements MouseMotionListener, MouseListener {

    public enum ColorBand {
        RED,
        GREEN,
        BLUE,
        ALPHA
    }


    public static final int BAND_MAX = 255;
    public static final int BAND_SAMPLES = 256;
    public static final int DEFAULT_INTERCEPT = 127;

    private EventListenerList listeners = new EventListenerList();


    private JFreeChart chart;
    private ChartPanel chartPanel;
    private XYPlot plot;
    private DynamicXYDataset controlPoints;
    private DynamicSplineXYDataset fittedLine;
    private ColorBand colorBand = ColorBandChart.ColorBand.RED;

    private boolean dragging = false;
    private int lockedOnItem = -1;


    private int xmin = -15;
    private int ymin = -15;
    private int xmax = 270;
    private int ymax = 270;

    /**
     * Creates a new instance of ColorBandChart
     */
    public ColorBandChart(ColorBand cband) {

        colorBand = cband;
        controlPoints = new DynamicXYDataset();

        controlPoints.addXYSeries(new double[]{0, 127, 255}, new double[]{0, 55, 255});
        fittedLine = new DynamicSplineXYDataset(controlPoints, BAND_SAMPLES);

        initPlot();
        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        initPanel();
    }


    public ColorBandChart(ColorBand cband, byte[] bandValues) {
        if (bandValues.length != 256) {
            throw new
                    IllegalArgumentException("supplied band array must have 256 values");
        }


        colorBand = cband;
        controlPoints = new DynamicXYDataset();


        controlPoints.addXYSeries(new double[]{0, 100, 175, 255},
                ArrayUtils.unsignedBytesToDoubles(new byte[]{bandValues[0], bandValues[100], bandValues[175], bandValues[255]}));


        fittedLine = new DynamicSplineXYDataset(controlPoints, BAND_SAMPLES);

        initPlot();
        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        initPanel();


    }

    private void initPanel() {
        chartPanel = new ChartPanel(chart);
        chartPanel.setBackground(Color.BLACK);

        chartPanel.addMouseListener(this);
        chartPanel.addMouseMotionListener(this);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setMinimumDrawWidth(50);
        chartPanel.setMinimumDrawHeight(50);
        chartPanel.setMaximumDrawWidth(800);
        chartPanel.setMaximumDrawHeight(800);

    }


    public void addChangeListener(ChangeListener listener) {
        listeners.add(ChangeListener.class, listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(ChangeListener.class, listener);
    }

    protected void fireChangeEvent(ChangeEvent e) {
        ChangeListener[] cl = listeners.getListeners(ChangeListener.class);
        for (ChangeListener c : cl) {
            c.stateChanged(e);
        }
    }


    public byte[] getBandData() {
        double[] splineVals = fittedLine.getSpline(0, DynamicXYDataset.RANGE).elements();

        byte[] vals = ArrayUtils.castToUnsignedBytes(splineVals);

        return vals;
    }

    private void initPlot() {
        ValueAxis hAxis = new NumberAxis("");
        ValueAxis vAxis = new NumberAxis("");


        DefaultXYItemRenderer controlRenderer = new ControlPointRenderer();
        controlRenderer.setSeriesStroke(0, new BasicStroke(2));


        StandardXYItemRenderer renderer = new StandardXYItemRenderer(StandardXYItemRenderer.LINES);
        renderer.setDrawSeriesLineAsPath(true);
        //renderer.setShapesVisible(false);
        renderer.setSeriesStroke(0, new BasicStroke(2f));

        plot = new XYPlot(fittedLine, hAxis, vAxis, renderer);
        plot.setDataset(0, fittedLine);

        plot.setDomainAxis(0, hAxis);
        plot.setRangeAxis(0, vAxis);
        plot.setRenderer(0, renderer);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setDataset(1, controlPoints);
        plot.setRenderer(1, controlRenderer);


        hAxis.setAutoRange(false);
        vAxis.setAutoRange(false);
        hAxis.setLowerBound(-15);
        hAxis.setUpperBound(BAND_SAMPLES + 15);
        vAxis.setLowerBound(-30);
        vAxis.setUpperBound(300);


        switch (colorBand) {
            case ALPHA:
                renderer.setSeriesPaint(0, Color.WHITE);
                controlRenderer.setSeriesPaint(0, Color.WHITE);
                break;
            case RED:
                renderer.setSeriesPaint(0, Color.RED);
                controlRenderer.setSeriesPaint(0, Color.RED);
                break;
            case GREEN:
                renderer.setSeriesPaint(0, Color.GREEN);
                controlRenderer.setSeriesPaint(0, Color.GREEN);
                break;
            case BLUE:
                renderer.setSeriesPaint(0, Color.BLUE);
                controlRenderer.setSeriesPaint(0, Color.BLUE);
                break;

            default:

        }

        plot.setDomainAxis(hAxis);
        plot.setRangeAxis(vAxis);
        plot.setBackgroundPaint(Color.BLACK);

        plot.getDomainAxis().setVisible(true);
        plot.getRangeAxis().setVisible(false);


    }


    private void setNewYValue(int series, int item, double value) {
        if (value > 255) value = 255;
        else if (value < 0) value = 0;
        fittedLine.setYValue(series, item, value);

        fireChangeEvent(new ChangeEvent(this));


    }

    private void addNewXYValue(int series, double x, double y) {
        fittedLine.addXYValue(series, x, y);
        fireChangeEvent(new ChangeEvent(this));

    }

    public void mouseClicked(MouseEvent e) {

        if (e.getClickCount() == 2) {
            ValueAxis hAxis = plot.getDomainAxis();
            ValueAxis vAxis = plot.getRangeAxis();
            double y = vAxis.java2DToValue((double) e.getY(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getRangeAxisEdge());
            double x = hAxis.java2DToValue((double) e.getX(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getDomainAxisEdge());

            int series = fittedLine.onLine(x, y);
            if (series != -1) {
                addNewXYValue(series, x, y);
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            dragging = false;
            lockedOnItem = -1;
        }
    }

    public void mouseEntered(MouseEvent e) {
        chartPanel.requestFocus();
    }

    public void mouseExited(MouseEvent e) {

    }


    public void mouseDragged(MouseEvent e) {
        dragging = true;

        Rectangle2D dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

        ValueAxis hAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();
        double y = vAxis.java2DToValue(e.getY(), dataArea, plot.getRangeAxisEdge());
        double x = hAxis.java2DToValue(e.getX(), dataArea, plot.getDomainAxisEdge());


        if (y > ymax || y < ymin) return;
        if (x > xmax || x < xmin) return;

        if (lockedOnItem != -1) {
            double hdist = controlPoints.horizontalDistance(0, x, y, lockedOnItem);

            if (hdist > 50) {
                lockedOnItem = -1;
            } else {
                setNewYValue(0, lockedOnItem, y);
            }

            return;
        }

        int item = controlPoints.nearestItem(0, x, y);

        if (item != -1) {
            lockedOnItem = item;
            setNewYValue(0, item, y);
        }


    }


    public void mouseMoved(MouseEvent e) {

    }


    public JComponent getComponent() {
        return chartPanel;

    }


    public static void main(String[] args) {
        JFrame jf = new JFrame();

        byte[] rvals = new byte[256];
        ColorTable.SPECTRUM.getReds(rvals);

        ColorBandChart chart1 = new ColorBandChart(ColorBand.RED, rvals);
        ColorBandChart chart2 = new ColorBandChart(ColorBand.GREEN);
        ColorBandChart chart3 = new ColorBandChart(ColorBand.BLUE);

        JPanel panel = new JPanel();

        BoxLayout layout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(layout);
        panel.add(chart1.getComponent());
        panel.add(chart2.getComponent());
        panel.add(chart3.getComponent());
        jf.add(panel);
        jf.pack();
        jf.setVisible(true);

    }


    class ControlPointRenderer extends DefaultXYItemRenderer {
        public void drawItem(Graphics2D g2, XYItemRendererState state, Rectangle2D dataArea, PlotRenderingInfo info,
                             XYPlot plot, ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset,
                             int series, int item, CrosshairState crosshairState, int pass) {

            double x = dataset.getX(series, item).doubleValue();
            double y = dataset.getY(series, item).doubleValue();

            double xtrans = domainAxis.valueToJava2D(
                    x, dataArea, plot.getDomainAxisEdge());

            double ytrans = rangeAxis.valueToJava2D(
                    y, dataArea, plot.getRangeAxisEdge());

            //Line2D line = new Line2D.Double(xtrans, ytrans, xtrans, ytrans);
            Ellipse2D ellipse = new Ellipse2D.Double(xtrans - 4, ytrans - 4, 8, 8);

            g2.setStroke(getSeriesStroke(0));
            //g2.setPaint(getSeriesPaint(0));
            //g2.draw(line);

            //g2.setPaint(Color.YELLOW);
            //g2.fill(ellipse);
            g2.setPaint(getSeriesPaint(0));
            g2.fill(ellipse);


        }


    }


}
