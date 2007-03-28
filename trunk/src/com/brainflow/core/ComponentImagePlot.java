package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.AnatomicalPlane;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 3:10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImagePlot extends JComponent implements IImagePlot {

    private AxisRange xAxis;

    private AxisRange yAxis;

    private java.util.List<IAnnotation> annotationList = new ArrayList<IAnnotation>();

    private IImageProducer producer;

    private IImageDisplayModel model;

    private AnatomicalVolume displayAnatomy;

    private Insets plotInsets = new Insets(30, 30, 30, 30);

    private Rectangle plotArea = new Rectangle(300,300);


    private String name;

    private Rectangle oldArea = null;



    public ComponentImagePlot(IImageDisplayModel model, IImageProducer _producer, AnatomicalVolume displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.displayAnatomy = displayAnatomy;
        this.model = model;
        producer = _producer;
        producer.setModel(model);
        producer.setPlot(this);

    }

    public ComponentImagePlot(IImageDisplayModel model, AnatomicalVolume displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.displayAnatomy = displayAnatomy;
        this.model = model;
        producer = new CompositeImageProducer(this, model, displayAnatomy);

    }


    public IImageDisplayModel getModel() {
        return model;
    }

    public JComponent getComponent() {
        return this;
    }


    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();

        Rectangle plotArea = getPlotArea();
        if (!plotArea.equals(oldArea)) {
            oldArea = plotArea;
            producer.updateImage(new DisplayChangeEvent(DisplayChangeType.SCREEN_SIZE_CHANGE));
        }

        Insets insets = getInsets();
        Color oldColor = g2.getColor();
        g2.setColor(Color.BLACK);

        g2.fillRect(insets.left, insets.top,
                (int) (size.getWidth() - insets.left - insets.right),
                (int) (size.getHeight() - insets.top - insets.bottom));


        g2.setColor(oldColor);
        g2.drawRenderedImage(producer.getImage(), AffineTransform.getTranslateInstance((int)plotArea.getMinX(), (int)plotArea.getMinY()));
        //g2.drawImage(producer.getImage(), null, (int)plotArea.getMinX(), (int)plotArea.getMaxX());

        for (IAnnotation ia : annotationList) {
            if (ia.isVisible()) {
                ia.draw(g2, plotArea, this);
            }
        }
    }

    public Insets getPlotInsets() {
        return plotInsets;
    }


    public Rectangle getPlotArea() {
        Insets insets = getInsets();
        Dimension size = getSize();

        Rectangle2D available = new Rectangle2D.Double(
                insets.left + plotInsets.left, insets.top + plotInsets.top,
                size.getWidth() - insets.left - insets.right - plotInsets.left - plotInsets.right,
                size.getHeight() - insets.top - insets.bottom - plotInsets.top - plotInsets.bottom
        );


        int drawWidth = (int) available.getWidth();
        int drawHeight = (int) available.getHeight();



        plotArea = new Rectangle(
                insets.left + plotInsets.left, insets.top + plotInsets.top, drawWidth, drawHeight
        );

        return plotArea;
    }

    public double getScaleX() {
        return plotArea.getWidth() / getXAxisRange().getInterval();

    }

    public double getScaleY() {
        return plotArea.getHeight() / getYAxisRange().getInterval();

    }

    public void setPlotInsets(Insets insets) {
        plotInsets = insets;
    }

    public AnatomicalVolume getDisplayAnatomy() {
        return displayAnatomy;

    }

    public void updateAxis(AxisRange range) {
        if (range.getAnatomicalAxis().sameAxis(xAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(xAxis.getAnatomicalAxis())) {
                setXAxisRange(range.flip());
            } else {
                setXAxisRange(range);
            }
        } else if (range.getAnatomicalAxis().sameAxis(yAxis.getAnatomicalAxis())) {
            if (!range.getAnatomicalAxis().sameDirection(yAxis.getAnatomicalAxis())) {
                setYAxisRange(range.flip());
            } else {
                setYAxisRange(range);
            }
        }
    }


    public void setXAxisRange(AxisRange xrange) {
        xAxis = xrange;
    }

    public void setYAxisRange(AxisRange yrange) {
        yAxis = yrange;
    }

    public Point translateAnatToScreen(AnatomicalPoint2D pt) {
        if (pt.getAnatomy().XAXIS != getXAxisRange().getAnatomicalAxis()) {
            throw new ImageAxis.IncompatibleAxisException("supplied point does not match image plot axes: " +
                    "X: " + pt.getAnatomy().XAXIS + " Y: " + pt.getAnatomy().YAXIS +
                    " Plot X: " + getXAxisRange().getAnatomicalAxis() +
                    " Plot Y: " + getYAxisRange().getAnatomicalAxis());
        }

        Insets insets = getInsets();
        Insets plotInsets = getPlotInsets();
        int x = (int) (pt.getX() * getScaleX() + plotInsets.left + insets.left);
        int y = (int) (pt.getY() * getScaleY() + plotInsets.top + insets.top);
        return new Point(x, y);
    }


    public AnatomicalPoint2D translateScreenToAnat(Point screenPoint) {
        Insets insets = getInsets();
        Insets plotInsets = getPlotInsets();
        double x = (screenPoint.getX() - insets.left - plotInsets.left) / getScaleX();
        double y = (screenPoint.getY() - insets.top - plotInsets.top) / getScaleY();
        return new AnatomicalPoint2D(AnatomicalPlane.matchAnatomy(
                getXAxisRange().getAnatomicalAxis(),
                getYAxisRange().getAnatomicalAxis()),
                x + getXAxisRange().getMinimum(),
                y + getYAxisRange().getMinimum());
    }


    public AxisRange getXAxisRange() {
        return xAxis;
    }

    public AxisRange getYAxisRange() {
        return yAxis;
    }



    public void setAnnotations(java.util.List<IAnnotation> _annotationList) {
        annotationList = _annotationList;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String toString() {
        if (name != null) return name;
        else {
            return super.toString();
        }
    }



}
