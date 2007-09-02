package com.brainflow.core;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.*;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.CoordinateAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import org.bushe.swing.event.EventBus;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 11:55:26 AM
 * To change this template use File | Settings | File Templates.
 */


public abstract class ImageView extends JComponent implements ListDataListener, ImageDisplayModelListener {


    public static final String PRESERVE_ASPECT_PROPERTY = "preserveAspect";

    private static final Logger log = Logger.getLogger(ImageView.class.getName());

    private String id = "";

    private boolean preserveAspect = false;

    private InterpolationHint screenInterpolation = InterpolationHint.NEAREST_NEIGHBOR;


    private IImageDisplayModel displayModel;

    private Property<ICrosshair> crosshair;

    protected Property<Viewport3D> viewport;

    private ViewportHandler viewportHandler = new ViewportHandler();

    private CrosshairHandler crosshairHandler = new CrosshairHandler();


    private CommandContainer commandContainer;


    public ImageView(IImageDisplayModel imodel) {
        super();
        displayModel = imodel;
        initView();
        commandContainer = new CommandContainer();

    }

    public ImageView(IImageDisplayModel imodel, CommandContainer parentContainer) {
        super();
        displayModel = imodel;
        initView();

        commandContainer = new CommandContainer(parentContainer);


    }


    protected void initCommands() {

    }


    protected void clearListeners() {
        viewport.removePropertyChangeListener(viewportHandler);
        displayModel.removeImageDisplayModelListener(this);
        //crosshair.removePropertyChangeListener();
        displayModel.getLayerSelection().removePropertyChangeListener(crosshairHandler);

    }


    protected void registerListeners() {
        viewport.addPropertyChangeListener(new ViewportHandler());

        displayModel.addImageDisplayModelListener(this);

        addMouseListener(new PlotSelectionHandler());

        crosshair.addPropertyChangeListener(crosshairHandler);

        displayModel.getLayerSelection().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent e) {
                if (e.getPropertyName().equals(SelectionInList.PROPERTYNAME_SELECTION_INDEX)) {
                    int selectionIndex = (Integer) e.getNewValue();
                    if (selectionIndex >= 0) {
                        EventBus.publish(new ImageViewLayerSelectionEvent(ImageView.this, (Integer) e.getNewValue()));
                    }
                }

            }
        });


    }


    private void initView() {


        viewport = new Property<Viewport3D>(new Viewport3D(getModel()));
        crosshair = new Property<ICrosshair>(new Crosshair(viewport.getProperty()));
        registerListeners();


    }

    public abstract SelectionInList getPlotSelection();

    public abstract SliceController getSliceController();


    public boolean isPreserveAspect() {
        return preserveAspect;
    }

    public void setPreserveAspect(boolean preserveAspect) {
        boolean old = preserveAspect;
        this.preserveAspect = preserveAspect;
        Iterator<IImagePlot> iter = getPlots().iterator();
        while (iter.hasNext()) {
            IImagePlot plot = iter.next();
            plot.setPreserveAspectRatio(preserveAspect);
        }


        this.firePropertyChange(ImageView.PRESERVE_ASPECT_PROPERTY, old, isPreserveAspect());
    }


    public InterpolationHint getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationHint screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
        Iterator<IImagePlot> iter = getPlots().iterator();
        while (iter.hasNext()) {
            IImagePlot plot = iter.next();
            plot.setScreenInterpolation(screenInterpolation);
        }


    }


    public IImagePlot getSelectedPlot() {
        return (IImagePlot) getPlotSelection().getSelection();
    }


    public int getSelectedIndex() {
        return displayModel.getLayerSelection().getSelectionIndex();
    }

    public void setSelectedIndex(int selectedIndex) {
        assert selectedIndex < getModel().getNumLayers() & selectedIndex >= 0;

        if (selectedIndex != displayModel.getSelectedIndex())
            displayModel.getLayerSelection().setSelectionIndex(selectedIndex);
    }

    public void setModel(IImageDisplayModel model) {
        displayModel = model;
    }

    public IImageDisplayModel getModel() {
        return displayModel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Property<ICrosshair> getCrosshair() {
        return crosshair;
    }

    public Property<Viewport3D> getViewport() {
        return viewport;
    }


    public AnatomicalPoint3D getCentroid() {
        ICoordinateSpace compositeSpace = displayModel.getImageSpace();
        CoordinateAxis a1 = compositeSpace.getImageAxis(Axis.X_AXIS);
        CoordinateAxis a2 = compositeSpace.getImageAxis(Axis.Y_AXIS);
        CoordinateAxis a3 = compositeSpace.getImageAxis(Axis.Z_AXIS);

        AnatomicalPoint1D x = a1.getRange().getCenter();
        AnatomicalPoint1D y = a2.getRange().getCenter();
        AnatomicalPoint1D z = a3.getRange().getCenter();

        return new AnatomicalPoint3D((Anatomy3D) compositeSpace.getAnatomy(), x.getX(), y.getX(), z.getX());


    }

    public void clearAnnotations() {
        for (IImagePlot plot : getPlots()) {
            plot.clearAnnotations();
        }
    }

    public IAnnotation getAnnotation(IImagePlot plot, String name) {

        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        return plot.getAnnotation(name);
    }

    public void removeAnnotation(IImagePlot plot, String name) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.removeAnnotation(name);

    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.setAnnotation(name, annotation);


    }

    public Point getCrosshairLocation(IImagePlot plot) {
        if (!getPlots().contains(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        ICrosshair cross = getCrosshair().getProperty();

        AnatomicalPoint1D xpt = cross.getValue(plot.getDisplayAnatomy().XAXIS);
        AnatomicalPoint1D ypt = cross.getValue(plot.getDisplayAnatomy().YAXIS);

        double percentX = (xpt.getX() - plot.getXAxisRange().getBeginning().getX()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt.getX() - plot.getYAxisRange().getBeginning().getX()) / plot.getYAxisRange().getInterval();

        Rectangle2D plotArea = plot.getPlotArea();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));
        return SwingUtilities.convertPoint(plot.getComponent(), location, this);


    }

    public AnatomicalPoint3D getAnatomicalLocation(Component source, Point p) {

        Point viewPoint = SwingUtilities.convertPoint(source, p, this);
        IImagePlot plot = whichPlot(viewPoint);

        if (plot == null) {
            return null;
        }

        Point plotPoint = SwingUtilities.convertPoint(this, viewPoint, plot.getComponent());

        AnatomicalPoint2D apoint = plot.translateScreenToAnat(plotPoint);

        Anatomy3D displayAnatomy = plot.getDisplayAnatomy();
        AnatomicalPoint3D ap3d = new AnatomicalPoint3D(
                Anatomy3D.matchAnatomy(
                        plot.getXAxisRange().getAnatomicalAxis(),
                        plot.getYAxisRange().getAnatomicalAxis(),
                        plot.getDisplayAnatomy().ZAXIS),
                apoint.getX(), apoint.getY(),
                getCrosshair().getProperty().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public abstract IImagePlot whichPlot(Point p);

    public abstract List<IImagePlot> getPlots();


    public abstract RenderedImage captureImage();


    public void intervalAdded(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }

    }

    public void intervalRemoved(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void contentsChanged(ListDataEvent e) {
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {

            plot.getImageProducer().reset();
            plot.getComponent().repaint();
        }
    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
        Viewport3D viewport = getViewport().getProperty();
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.setXAxisRange(viewport.getRange(plot.getDisplayAnatomy().XAXIS));
            plot.setYAxisRange(viewport.getRange(plot.getDisplayAnatomy().YAXIS));

        }

    }


    class CrosshairHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            EventBus.publish(new ImageViewCrosshairEvent(ImageView.this));

        }
    }


    class ViewportHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            List<IImagePlot> plots = ImageView.this.getPlots();
            for (IImagePlot plot : plots) {
                plot.setXAxisRange(viewport.getProperty().getRange(plot.getDisplayAnatomy().XAXIS));
                plot.setYAxisRange(viewport.getProperty().getRange(plot.getDisplayAnatomy().YAXIS));

            }


        }
    }

    class PlotSelectionHandler extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            IImagePlot plot = whichPlot(e.getPoint());
            if (plot != null && plot != getSelectedPlot()) {
                IImagePlot oldPlot = getSelectedPlot();

                getPlotSelection().setSelection(plot);
                plot.getComponent().repaint();

                if (oldPlot != null)
                    oldPlot.getComponent().repaint();
            }
        }

    }


    public class SetPreserveAspectCommand extends ToggleCommand {

        private ImageView view;

        public SetPreserveAspectCommand(ImageView view) {
            super("preserve-aspect");
            this.view = view;
            setSelected(view.isPreserveAspect());
        }

        protected void handleSelection(boolean b) throws ToggleVetoException {
            if (view.isPreserveAspect() != b)
                view.setPreserveAspect(b);

        }


    }

}
