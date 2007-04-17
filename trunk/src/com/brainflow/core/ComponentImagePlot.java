package com.brainflow.core;

import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.image.anatomy.Anatomy2D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.axis.ImageAxis;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 2, 2006
 * Time: 3:10:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentImagePlot extends JComponent implements IImagePlot {

    private AnatomicalPoint1D slice;

    private AxisRange xAxis;

    private AxisRange yAxis;

    private LinkedHashMap<String, IAnnotation> annotationMap = new LinkedHashMap<String, IAnnotation>();

    private IImageProducer producer;

    private IImageDisplayModel model;

    private Anatomy3D displayAnatomy;

    private Insets plotInsets = new Insets(18, 18, 18, 18);

    private Rectangle plotArea = new Rectangle(300,300);


    private String name;

    private Rectangle oldArea = null;

    private PropertyChangeListener annotationListener;


    private ComponentImagePlot(IImageDisplayModel model, IImageProducer _producer, Anatomy3D displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.displayAnatomy = displayAnatomy;
        this.model = model;
        producer = _producer;
       
        initAnnotationListener();

    }

    public ComponentImagePlot(IImageDisplayModel model, Anatomy3D displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.displayAnatomy = displayAnatomy;
        this.model = model;
        //producer = new CompositeImageProducer(this,  displayAnatomy);
        initAnnotationListener();

    }

    private void initAnnotationListener() {
        annotationListener = new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                IAnnotation annot = (IAnnotation)evt.getSource();
                System.out.println("annotation changed " + annot.getIdentifier());
                repaint();
            }
        };
    }


    public void setSlice(AnatomicalPoint1D slice) {
        if (getSlice() == null || !getSlice().equals(slice)) {
            this.slice = slice;
            producer.setSlice(slice);

        }

        repaint();
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }

    public IImageDisplayModel getModel() {
        return model;
    }

    public JComponent getComponent() {
        return this;
    }


    public void setImageProducer(IImageProducer producer) {
        this.producer = producer;
        producer.setPlot(this);
    }

    public IImageProducer getImageProducer() {
        return producer;
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;
        Dimension size = getSize();

        Rectangle plotArea = getPlotArea();

        
        if (!plotArea.equals(oldArea)) {
            oldArea = plotArea;
            producer.setScreenSize(plotArea);
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

        for (String annot : annotationMap.keySet()) {
           IAnnotation ia = annotationMap.get(annot);
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

    public Anatomy3D getDisplayAnatomy() {
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
        producer.setXAxis(xrange);
        repaint();
    }

    public void setYAxisRange(AxisRange yrange) {
        yAxis = yrange;
        producer.setYAxis(yrange);
        repaint();
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
        return new AnatomicalPoint2D(Anatomy2D.matchAnatomy(
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



    public void setAnnotation(String name, IAnnotation annotation) {      
        annotationMap.put(name, annotation);
        annotation.addPropertyChangeListener(annotationListener);
        repaint();
    }

    public IAnnotation getAnnotation(String name) {
        return annotationMap.get(name);
    }

    public void removeAnnotation(String name) {
        IAnnotation annotation = annotationMap.remove(name);
        if (annotation != null) {
            annotation.removePropertyChangeListener(annotationListener);
        }
    }

    public void clearAnnotations() {
        annotationMap.clear();
    }

    public Map<String, IAnnotation> getAnnotations() {
        return Collections.unmodifiableMap(annotationMap);
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


    public static ComponentImagePlot createComponentImagePlot(IImageDisplayModel model, IImageProducer _producer, Anatomy3D displayAnatomy, AxisRange xAxis, AxisRange yAxis) {
        ComponentImagePlot plot =  new ComponentImagePlot(model, _producer, displayAnatomy, xAxis, yAxis);
        plot.producer.setPlot(plot);
        return plot;
    }
}
