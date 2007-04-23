package com.brainflow.core;

import com.brainflow.image.space.ImageSpace2D;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.CoordinateSpace2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.operations.ImageSlicer;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.CoordinateSet3D;
import com.brainflow.image.axis.CoordinateAxis;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.colormap.IColorMap;

import java.awt.image.BufferedImage;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.*;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 21, 2007
 * Time: 10:11:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class BasicCoordinateSliceRenderer implements SliceRenderer {


    private AnatomicalPoint1D slice;

    private CoordinateLayer layer;

    private Anatomy3D displayAnatomy = Anatomy3D.getCanonicalAxial();

    private BufferedImage image;

    private ICoordinateSpace space;

    public BasicCoordinateSliceRenderer(CoordinateLayer layer, AnatomicalPoint1D slice) {
        this.slice = slice;
        this.layer = layer;

    }

    public ICoordinateSpace getImageSpace() {
        if (space == null) {

            CoordinateAxis axis1 = layer.getCoordinateSpace().getImageAxis(displayAnatomy.XAXIS, true);
            CoordinateAxis axis2 = layer.getCoordinateSpace().getImageAxis(displayAnatomy.YAXIS, true);

            AxisRange a1 = new AxisRange(displayAnatomy.XAXIS, axis1.getRange().getMinimum(), axis1.getRange().getMaximum());
            AxisRange a2 = new AxisRange(displayAnatomy.YAXIS, axis2.getRange().getMinimum(), axis2.getRange().getMaximum());


            space = new CoordinateSpace2D(new CoordinateAxis(a1), new CoordinateAxis(a2));
        }


        return space;


    }

    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D anatomy) {
        if (getDisplayAnatomy() != anatomy) {
            displayAnatomy = anatomy;
            flush();
        }

    }

    public void setSlice(AnatomicalPoint1D slice) {
        if (!getSlice().equals(slice)) {
            this.slice = slice;
            flush();
        }
    }

    public AnatomicalPoint1D getSlice() {
        return slice;
    }

    public BufferedImage render() {
        CoordinateSet3D set = layer.getData();
        List<AnatomicalPoint3D> pts = set.pointsWithinPlane(getSlice());

        for (AnatomicalPoint3D pt : pts) {
            System.out.println("found point : " + pts);
        }

        return null;
    }

    public void renderUnto(Rectangle2D frame, Graphics2D g2) {
        CoordinateSet3D set = layer.getData();
        List<Integer> indices = set.indicesWithinPlane(getSlice());

        if (indices.size() == 0) {
            return;
        }


        double minx = getImageSpace().getImageAxis(Axis.X_AXIS).getRange().getMinimum();
        double miny = getImageSpace().getImageAxis(Axis.Y_AXIS).getRange().getMinimum();


        double transx = (minx - frame.getMinX()); //+ (-frameBounds.getMinX());
        double transy = (miny - frame.getMinY()); //+ (-frameBounds.getMinY());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        AlphaComposite composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)getLayer().getOpacity());
        g2.setComposite(composite);



        System.out.println("trans x : " + transx);
        System.out.println("trans y : " + transy);

        System.out.println("min x : " + minx);
        System.out.println("min y : " + miny);

        System.out.println("frame bounds : " + frame);

        System.out.println("clip : " + g2.getClip());

        IColorMap map = getLayer().getImageLayerProperties().getColorMap().getProperty();
        for (int i : indices) {
            AnatomicalPoint3D pt = set.getAnatomicalPoint(i);
            double value = set.getValue(i);

            double radius = set.getRadius(i);

            double x = pt.getValue(displayAnatomy.XAXIS).getX();
            double y = pt.getValue(displayAnatomy.YAXIS).getX();

            System.out.println("anat x : " + x);
            System.out.println("anat y : " + y);

            Color c = map.getColor(value);
            g2.setColor(c);

            Shape shape = new Ellipse2D.Float((int) ((x-minx) - (radius/2.0)), (int) ((y-miny) - (radius/2.0)), (float)radius, (float)radius);
            g2.fill(shape);
        }

    }

    public void flush() {
        image = null;
    }

    public boolean isVisible() {
        return layer.isVisible();
    }

    public CoordinateLayer getLayer() {
        return layer;
    }
}
