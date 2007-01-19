package com.brainflow.display;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IntervalColorModel;
import com.brainflow.colormap.LinearColorBar;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.jfreechart.DynamicSplineXYDataset;
import com.brainflow.jfreechart.DynamicXYDataset;
import com.brainflow.utils.ArrayUtils;
import com.brainflow.utils.NumberUtils;
import com.brainflow.utils.Range;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ColorChart extends JPanel implements MouseListener, MouseMotionListener, ActionListener {

    ChartPanel chartPanel;
    JTabbedPane controlPane = new JTabbedPane();
    XYPlot plot1;
    XYPlot plot2;

    XYPlot plot;
    JFreeChart chart;

    Rectangle2D dataArea;
    DynamicXYDataset dataset;
    DynamicSplineXYDataset splineDataset;


    Point lastMousePressed;

    LinearColorBar colorBar;
    ColoredHistogram chist;

    double xmin = -15;
    double ymin = -15;
    double xmax = 260;
    double ymax = 260;

    ArrayList menuItems = new ArrayList();


    JRadioButton redButton = new JRadioButton("Red");
    JRadioButton greenButton = new JRadioButton("Green");
    JRadioButton blueButton = new JRadioButton("Blue");
    JRadioButton alphaButton = new JRadioButton("Alpha");
    ButtonGroup bgroup = new ButtonGroup();

    protected final int ADJUST_RED = 0;
    protected final int ADJUST_GREEN = 1;
    protected final int ADJUST_BLUE = 2;
    protected final int ADJUST_ALPHA = 3;

    int selectedColor = ADJUST_RED;

    boolean dragging = false;
    int lockedOnItem = -1;
    boolean backgroundDirty = true;

    public static final String COLORMAP_CHANGED_PROPERTY = "RaggedColorMap Changed";

    IntervalColorModel currentModel = null;
    BufferedImage seeThroughImage = null;
    Timer timer = new Timer();

    public ColorChart() {

        setLayout(new BorderLayout());

        double[][] ydata = createDefaultYData();

        double[] xdata = new double[]{0, 50, 100, 150, 200, 255};


        dataset = new DynamicXYDataset();
        dataset.addXYSeries(xdata, ydata[0]);
        dataset.addXYSeries(xdata, ydata[1]);
        dataset.addXYSeries(xdata, ydata[2]);
        dataset.addXYSeries(xdata, ydata[3]);

        dataset.setSeriesNames(new String[]{"Red", "Green", "Blue", "Alpha"});


        splineDataset = new DynamicSplineXYDataset(dataset, 256);
        currentModel = new IntervalColorModel(new Range(0, 255), RenderUtils.createIndexColorModel(splineDataset.getYSplineData()), "color map");


        ValueAxis hAxis = new NumberAxis("Domain");
        ValueAxis vAxis = new NumberAxis("Value");
        DefaultXYItemRenderer shapeRenderer = new DefaultXYItemRenderer();
        shapeRenderer.setLinesVisible(false);

        plot = new XYPlot(dataset, hAxis, vAxis, shapeRenderer);
        plot.setDataset(1, splineDataset);
        plot.setDomainAxis(1, hAxis);
        plot.setRangeAxis(1, vAxis);

        DefaultXYItemRenderer renderer = new DefaultXYItemRenderer();
        renderer.setDrawSeriesLineAsPath(true);
        renderer.setShapesVisible(false);


        plot.setRenderer(1, renderer);
        plot.setDomainGridlinesVisible(false);
        plot.setRangeGridlinesVisible(false);
        plot.setBackgroundAlpha(1);


        hAxis.setAutoRange(false);
        vAxis.setAutoRange(false);
        hAxis.setLowerBound(xmin);
        hAxis.setUpperBound(xmax);
        vAxis.setLowerBound(ymin);
        vAxis.setUpperBound(ymax);


        Color[] cols = new Color[]{Color.red, Color.green, Color.blue, Color.white};
        // set the color for each series...
        //Stroke[] seriesStrokeArray = new Stroke[4];
        Stroke seriesStroke = new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        for (int i = 0; i < 4; i++) {
            plot.getRenderer().setSeriesPaint(i, cols[i]);
            plot.getRenderer(1).setSeriesPaint(i, cols[i]);
            plot.getRenderer().setSeriesStroke(i, seriesStroke);
            plot.getRenderer(1).setSeriesStroke(i, seriesStroke);
        }


        plot.getDomainAxis().setVisible(false);
        plot.getRangeAxis().setVisible(false);


        chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        //plot.setBackgroundAlpha(0);


        chartPanel = new ChartPanel(chart);
        chartPanel.addMouseListener(this);
        chartPanel.addMouseMotionListener(this);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);


        add(chartPanel, "Center");

        //initControls();
        //initMenuItems();

        bgroup.add(redButton);
        bgroup.add(greenButton);
        bgroup.add(blueButton);
        bgroup.add(alphaButton);

        redButton.setSelected(true);

        JPanel topPanel = new JPanel();
        topPanel.add(redButton);
        topPanel.add(greenButton);
        topPanel.add(blueButton);
        topPanel.add(alphaButton);

        redButton.addActionListener(this);
        greenButton.addActionListener(this);
        blueButton.addActionListener(this);
        alphaButton.addActionListener(this);

        add(topPanel, "North");


        try {
            seeThroughImage = ImageIO.read(getClass().getClassLoader().getResource("resources/icons/checkerboard.jpg"));
        } catch (IOException e) {
            e.printStackTrace();

        }


        updateBackgroundImage();


        timer.schedule(new TimerTask() {
            public void run() {
                ColorChart.this.updateBackgroundImage();
            }
        }, 0, 80);


    }


    protected double[][] createDefaultYData() {
        double[][] ydata = new double[4][6];
        byte[][] tmp = ColorTable.extractTable(ColorTable.SPECTRUM);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                ydata[i][j] = NumberUtils.ubyte(tmp[i][(int) (j * (255 / 5))]);
            }
        }

        return ydata;
    }


    public void setIntervalColorModel(IntervalColorModel icm) {
        byte[][] table = RenderUtils.extractTable(icm.getWrappedModel());
        double[] xdata = ColorTable.linearRamp(0, 255, 6);
        for (int i = 0; i < 4; i++) {
            double[] raw = ArrayUtils.unsignedBytesToDoubles(table[i]);
            double[] ydata = new double[xdata.length];
            for (int j = 0; j < ydata.length; j++) {
                ydata[j] = raw[(int) xdata[j]];

            }
            splineDataset.setXYSeries(i, xdata, ydata);
        }

        currentModel = icm;
        backgroundDirty = true;
        updateBackgroundImage();
    }


    protected void createPopup(Point point) {

        JPopupMenu popup = new JPopupMenu();
        for (Iterator iter = menuItems.iterator(); iter.hasNext();) {
            JMenuItem item = (JMenuItem) iter.next();
            popup.add(item);
        }

        popup.show(this, point.x, point.y);
    }


    private void notifyListeners() {
        firePropertyChange(ColorChart.COLORMAP_CHANGED_PROPERTY, "nothing", currentModel);
    }

    public synchronized void updateBackgroundImage() {
        if (currentModel == null) return;
        if (!backgroundDirty) return;

        double[][] table = splineDataset.getYSplineData();
        IntervalColorModel oldModel = currentModel;

        currentModel = new IntervalColorModel(oldModel.getDisplayRange(), RenderUtils.createIndexColorModel(table), "color map");
        colorBar = new LinearColorBar(new LinearColorMap(0, 256, currentModel.getWrappedModel()), SwingConstants.HORIZONTAL);

        colorBar.setSize(256, 25);
        colorBar.setBackgroundImage(seeThroughImage);

        BufferedImage bimg = new BufferedImage(256, 20, BufferedImage.TYPE_4BYTE_ABGR);

        colorBar.paintComponent(bimg.createGraphics());
        plot.setBackgroundImage(bimg);
        backgroundDirty = false;

    }


    /**
     * Starting point for the demonstration application.
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new ColorChart());
        frame.setVisible(true);
        frame.pack();

    }


    private void setNewYValue(int series, int item, double value) {
        if (value > 255) value = 255;
        else if (value < 0) value = 0;
        splineDataset.setYValue(series, item, value);
        backgroundDirty = true;
        //updateBackgroundImage();

    }

    private void addNewXYValue(int series, double x, double y) {
        splineDataset.addXYValue(series, x, y);
        backgroundDirty = true;
        //updateBackgroundImage();
    }


    public void mouseClicked(MouseEvent e) {


        if (e.getClickCount() == 2) {
            ValueAxis hAxis = plot.getDomainAxis();
            ValueAxis vAxis = plot.getRangeAxis();
            double y = vAxis.java2DToValue((double) e.getY(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getRangeAxisEdge());

            double x = hAxis.java2DToValue((double) e.getX(), chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea(), plot.getDomainAxisEdge());

            int series = splineDataset.onLine(x, y);
            if (series != -1) {
                addNewXYValue(series, x, y);
            }
        }

    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            createPopup(e.getPoint());
            return;
        }

        lastMousePressed = e.getPoint();
    }

    public void mouseReleased(MouseEvent e) {
        if (dragging) {
            notifyListeners();
            dragging = false;
            lockedOnItem = -1;
            updateBackgroundImage();
        }
    }

    public void mouseEntered(MouseEvent e) {
        requestFocus();
    }

    public void mouseExited(MouseEvent e) {

    }


    public void mouseDragged(MouseEvent e) {
        dragging = true;

        dataArea = chartPanel.getChartRenderingInfo().getPlotInfo().getDataArea();

        ValueAxis hAxis = plot.getDomainAxis();
        ValueAxis vAxis = plot.getRangeAxis();
        double y = vAxis.java2DToValue(e.getY(), dataArea, plot.getRangeAxisEdge());
        double x = hAxis.java2DToValue(e.getX(), dataArea, plot.getDomainAxisEdge());


        if (y > ymax || y < ymin) return;
        if (x > xmax || x < xmin) return;

        if (lockedOnItem != -1) {
            double hdist = dataset.horizontalDistance(selectedColor, x, y, lockedOnItem);

            if (hdist > 50) {
                lockedOnItem = -1;
            } else {
                setNewYValue(selectedColor, lockedOnItem, y);
            }

            return;
        }

        int item = dataset.nearestItem(selectedColor, x, y);

        if (item != -1) {
            lockedOnItem = item;
            setNewYValue(selectedColor, item, y);
        }

        System.out.println(" y value: " + y);
        System.out.println(" x value: " + x);


    }


    public void mouseMoved(MouseEvent e) {

    }

    /*public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();

        if (propName == ColorLevelControl.RED_LEVEL_PROPERTY) {
            int red = levelControl.getRedLevel();
            
            splineDataset.setYBaseLine(0, red, 255.0);
            //updateData();
        }
        
        else if (propName == ColorLevelControl.GREEN_LEVEL_PROPERTY) {
            int green = levelControl.getGreenLevel();
            splineDataset.setYBaseLine(1, green, 255.0);
            //updateData();
        }
        
        else if (propName == ColorLevelControl.BLUE_LEVEL_PROPERTY) {
            int blue = levelControl.getBlueLevel();
            splineDataset.setYBaseLine(2, blue, 255.0);
            //updateData();
        }
        else if (propName == ColorLevelControl.ALPHA_LEVEL_PROPERTY) {
            int alpha = levelControl.getAlphaLevel();
            splineDataset.setYBaseLine(3, alpha, 255.0);
            //updateData();
        }
        
        else if (propName == ColorChartGeneratorPanel.RED_LINEAR_ASCENDING_PROPERTY) {
            double[] red = ColorTable.linearRamp(.01, 255, 5);
            splineDataset.setXYSeries(0,(double[])red.clone(), red);
            //updateData();
        }
        
        else if (propName == ColorChartGeneratorPanel.GREEN_LINEAR_ASCENDING_PROPERTY) {
            double[] green = ColorTable.linearRamp(.01, 255, 5);
            splineDataset.setXYSeries(1,(double[])green.clone(), green);
            //updateData();
        }
        
        else if (propName == ColorChartGeneratorPanel.BLUE_LINEAR_ASCENDING_PROPERTY) {
            double[] blue = ColorTable.linearRamp(.01, 255, 5);
            splineDataset.setXYSeries(2,(double[])blue.clone(), blue);
            //updateData();
        }
        
        else if (propName == ColorChartGeneratorPanel.RED_LINEAR_DESCENDING_PROPERTY) {
            double[] red = ColorTable.linearRamp(255, .01, 5);
            double[] xred = ColorTable.linearRamp(0,255,5);
            splineDataset.setXYSeries(0,xred, red);
            //updateData();
        }
        else if (propName == ColorChartGeneratorPanel.GREEN_LINEAR_DESCENDING_PROPERTY) {
            double[] green = ColorTable.linearRamp(255, .01, 5);
            double[] xgreen = ColorTable.linearRamp(0,255,5);
            splineDataset.setXYSeries(1,xgreen, green);
            //updateData();
        }
        else if (propName == ColorChartGeneratorPanel.BLUE_LINEAR_DESCENDING_PROPERTY) {
            double[] blue = ColorTable.linearRamp(255, .01, 5);
            double[] xblue = ColorTable.linearRamp(0,255,5);
            splineDataset.setXYSeries(2,xblue, blue);
            //updateData();
        }
        else if (propName == ColorChartGeneratorPanel.ALPHA_LINEAR_DESCENDING_PROPERTY) {
            double[] alpha = ColorTable.linearRamp(255, .01, 5);
            double[] xalpha = ColorTable.linearRamp(0,255,5);
            splineDataset.setXYSeries(3,xalpha, alpha);
            //updateData();
        }
        
        
    }*/


    public void ancestorAdded(AncestorEvent e) {
    }

    public void ancestorRemoved(AncestorEvent e) {
    }

    public void ancestorMoved(AncestorEvent e) {
    }

    public void stateChanged(ChangeEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == redButton)
            selectedColor = ADJUST_RED;
        if (e.getSource() == greenButton)
            selectedColor = ADJUST_GREEN;
        if (e.getSource() == blueButton)
            selectedColor = ADJUST_BLUE;
        if (e.getSource() == alphaButton)
            selectedColor = ADJUST_ALPHA;

    }


}