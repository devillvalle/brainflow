package com.brainflow.core;

import com.brainflow.application.services.ImageViewCrosshairEvent;
import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.display.Crosshair;
import com.brainflow.display.ICrosshair;
import com.brainflow.display.InterpolationType;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import org.bushe.swing.event.EventBus;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 17, 2004
 * Time: 11:55:26 AM
 * To change this template use File | Settings | File Templates.
 */


public class ImageView extends JComponent implements ListDataListener, ImageDisplayModelListener {

    private static final Logger log = Logger.getLogger(ImageView.class.getName());

    public static final String PRESERVE_ASPECT_PROPERTY = "preserveAspect";

    public final Property<IImageDisplayModel> displayModel = ObservableProperty.create();

    public final Property<Boolean> preserveAspect = new ObservableProperty<Boolean>(false) {
        public void set(Boolean aBoolean) {
            super.set(aBoolean);
            Iterator<IImagePlot> iter = getPlots().iterator();
            while (iter.hasNext()) {
                IImagePlot plot = iter.next();
                plot.setPreserveAspectRatio(aBoolean);
            }

        }
    };

    public final Property<String> identifier = ObservableProperty.create("view");

    public final Property<Double> pixelsPerUnit = ObservableProperty.create(1.5);

    private ICrosshair crosshair;

    private InterpolationType screenInterpolation = InterpolationType.NEAREST_NEIGHBOR;

    private ImagePlotLayout plotLayout = new SimplePlotLayout(this, Anatomy3D.getCanonicalAxial());

    private SliceController sliceController;

    private SelectionInList<IImagePlot> plotSelection;

    protected Viewport3D viewport;

    private ViewportHandler viewportHandler = new ViewportHandler();

    private CrosshairHandler crosshairHandler = new CrosshairHandler();

    private PlotSelectionHandler plotSelectionHandler = new PlotSelectionHandler();

    private ImageLayerSelectionListener layerSelectionListener = new ImageLayerSelectionListener();

    private CommandContainer commandContainer;


    public ImageView(IImageDisplayModel imodel) {
        super();
        BeanContainer.bind(this);
        displayModel.set(imodel);
        commandContainer = new CommandContainer();

        initView();

    }


    public ImageView(IImageDisplayModel imodel, CommandContainer parentContainer) {
        super();
        BeanContainer.bind(this);
        displayModel.set(imodel);
        commandContainer = new CommandContainer(parentContainer);

        initView();


    }


    protected void initCommands() {

    }

    public ImagePlotLayout getPlotLayout() {
        return plotLayout;
    }

    public void setPlotLayout(ImagePlotLayout plotLayout) {
        this.plotLayout = plotLayout;
        List<IImagePlot> plots = plotLayout.layoutPlots();

        assert plots.size() > 0 : "assertion failure, plot layout returned empty plot list";

        IImagePlot selPlot = null;

        if ((plotSelection != null) && (plotSelection.getSelection() != null)) {
            selPlot = plotSelection.getSelection();
        } else {
            selPlot = plots.get(0);
        }

        if (plotSelection == null) {
            plotSelection = new SelectionInList<IImagePlot>();

        } else {
            plotSelection.setList(plots);
        }


        plotSelection.setSelection(selPlot);
        sliceController = plotLayout.createSliceController();

        revalidate();
    }

    protected void clearListeners() {
        viewport.removePropertyChangeListener(viewportHandler);
        displayModel.get().removeImageDisplayModelListener(this);
        crosshair.removePropertyChangeListener(crosshairHandler);
        displayModel.get().getLayerSelection().removePropertyChangeListener(layerSelectionListener);
        removeMouseListener(plotSelectionHandler);
    }


    protected void registerListeners() {
        viewport.addPropertyChangeListener(new ViewportHandler());

        displayModel.get().addImageDisplayModelListener(this);

        addMouseListener(plotSelectionHandler);

        crosshair.addPropertyChangeListener(crosshairHandler);

        displayModel.get().getLayerSelection().addPropertyChangeListener(layerSelectionListener);


    }

    private void updateView() {
        clearListeners();
        plotLayout.setView(this);
        setPlotLayout(plotLayout);

        initView();


    }

    private void initView() {

        viewport = new Viewport3D(getModel());
        crosshair = new Crosshair(viewport);
        registerListeners();
        setPlotLayout(plotLayout);


    }

    public SelectionInList<IImagePlot> getPlotSelection() {
        return plotSelection;
    }

    public SliceController getSliceController() {
        return sliceController;
    }


    public boolean isPreserveAspect() {
        return preserveAspect.get();
    }

    public void setPreserveAspect(boolean preserveAspect) {
        boolean old = preserveAspect;
        this.preserveAspect.set(preserveAspect);


        firePropertyChange(ImageView.PRESERVE_ASPECT_PROPERTY, old, isPreserveAspect());
    }


    public InterpolationType getScreenInterpolation() {
        return screenInterpolation;
    }

    public void setScreenInterpolation(InterpolationType screenInterpolation) {
        this.screenInterpolation = screenInterpolation;
        Iterator<IImagePlot> iter = getPlots().iterator();
        while (iter.hasNext()) {
            IImagePlot plot = iter.next();
            plot.setScreenInterpolation(screenInterpolation);
        }


    }


    public IImagePlot getSelectedPlot() {
        return getPlotSelection().getSelection();
    }

    public ImageLayer getSelectedLayer() {
        return displayModel.get().getSelectedLayer();
    }
    
    public int getSelectedLayerIndex() {
        return displayModel.get().getLayerSelection().getSelectionIndex();
    }

    public void setSelectedLayerIndex(int selectedIndex) {
        displayModel.get().setSelectedIndex(selectedIndex);
    }

    public void setModel(IImageDisplayModel model) {
        displayModel.set(model);
        updateView();
    }

    public IImageDisplayModel getModel() {
        return displayModel.get();
    }


    public ICrosshair getCrosshair() {
        return crosshair;
    }

    public Viewport3D getViewport() {
        return viewport;
    }


    public AnatomicalPoint3D getCentroid() {
        ICoordinateSpace compositeSpace = displayModel.get().getImageSpace();
        return (AnatomicalPoint3D) compositeSpace.getCentroid();
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

    public void setAnnotation(String name, IAnnotation annotation) {
        Iterator<IImagePlot> iter = plotLayout.iterator();
        while (iter.hasNext()) {
            setAnnotation(iter.next(), name, annotation);
        }
    }

    public void setAnnotation(IImagePlot plot, String name, IAnnotation annotation) {
        if (!plotLayout.containsPlot(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        plot.setAnnotation(name, annotation);


    }

    public Point getCrosshairLocation(IImagePlot plot) {
        if (!plotLayout.containsPlot(plot)) {
            throw new IllegalArgumentException("View does not contain plot : " + plot);
        }

        ICrosshair cross = getCrosshair();

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
                getCrosshair().getValue(displayAnatomy.ZAXIS).getX());


        return ap3d;
    }


    public IImagePlot whichPlot(Point p) {
        return plotLayout.whichPlot(p);
    }

    public List<IImagePlot> getPlots() {
        return plotLayout.getPlots();
    }

    public ListIterator<IImagePlot> plotIterator() {
        return plotLayout.iterator();
    }

    public RenderedImage captureImage() {
        BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        paint(img.createGraphics());
        return img;


    }


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
        Viewport3D viewport = getViewport();
        List<IImagePlot> plots = getPlots();
        for (IImagePlot plot : plots) {
            plot.setXAxisRange(viewport.getRange(plot.getDisplayAnatomy().XAXIS));
            plot.setYAxisRange(viewport.getRange(plot.getDisplayAnatomy().YAXIS));

        }

    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d =  plotLayout.getPreferredSize();

        d.setSize(d.getWidth()*pixelsPerUnit.get().doubleValue(), d.getHeight() * pixelsPerUnit.get().doubleValue());
        return d;
    }

    public String toString() {
        return identifier.get();
    }

    class ImageLayerSelectionListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(SelectionInList.PROPERTYNAME_SELECTION_INDEX)) {
                int selectionIndex = (Integer) evt.getNewValue();
                if (selectionIndex >= 0) {
                    EventBus.publish(new ImageViewLayerSelectionEvent(ImageView.this, (Integer) evt.getNewValue()));
                }
            }

        }
    }


    class CrosshairHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            ICrosshair cross = (ICrosshair) evt.getSource();
            AnatomicalPoint1D slice = cross.getLocation().getValue(ImageView.this.getSelectedPlot().getDisplayAnatomy().ZAXIS);
            System.out.println("crosshair changed to " + slice);
            sliceController.setSlice(slice);
            

            //todo could be gratuitous because repaint called in sliceController ...
            getSelectedPlot().getComponent().repaint();

            EventBus.publish(new ImageViewCrosshairEvent(ImageView.this));

        }
    }



    class ViewportHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            List<IImagePlot> plots = ImageView.this.getPlots();
            for (IImagePlot plot : plots) {
                plot.setXAxisRange(viewport.getRange(plot.getDisplayAnatomy().XAXIS));
                plot.setYAxisRange(viewport.getRange(plot.getDisplayAnatomy().YAXIS));

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
