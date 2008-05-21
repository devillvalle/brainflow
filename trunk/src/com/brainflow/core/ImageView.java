package com.brainflow.core;

import com.brainflow.application.services.ImageViewLayerSelectionEvent;
import com.brainflow.core.annotations.IAnnotation;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.display.InterpolationType;
import com.brainflow.image.anatomy.*;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.IImageSpace3D;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.toggle.ToggleCommand;
import com.pietschy.command.toggle.ToggleVetoException;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableWrapper;
import net.java.dev.properties.events.PropertyListener;
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


    public final Property<IImageDisplayModel> displayModel = ObservableProperty.create();

    public final IndexedProperty<IImagePlot> plotList = new ObservableIndexed<IImagePlot>();

    public final Property<Integer> plotSelection = ObservableProperty.create(-1);

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


    public final Property<AnatomicalPoint3D> cursorPos = new ObservableProperty<AnatomicalPoint3D>() {

        public void set(AnatomicalPoint3D ap) {
            super.set(ap);

            IImagePlot selectedPlot = getSelectedPlot();
            if (selectedPlot != null) {
                //this is a hack.
                //todo this should give rise to an event, whcih slice controller responds to
                //AnatomicalPoint1D slice = ap.getValue(selectedPlot.getDisplayAnatomy().ZAXIS);
                sliceController.setSlice(ap);
                selectedPlot.getComponent().repaint();
            }

        }
    };

    public final RProperty<AnatomicalPoint3D> worldCursorPos = new ObservableWrapper.ReadWrite<AnatomicalPoint3D>(cursorPos) {

        public void set(AnatomicalPoint3D ap) {
            RProperty<AnatomicalPoint3D> cpos = (RProperty<AnatomicalPoint3D>) getProperties()[0];
            IImageSpace3D space = (IImageSpace3D)getModel().getImageSpace();
            float[] gridpos = space.worldToGrid((float)ap.getX(), (float)ap.getY(), (float)ap.getZ());
            double x1 = space.getImageAxis(Axis.X_AXIS).gridToReal(gridpos[0]);
            double y1 = space.getImageAxis(Axis.Y_AXIS).gridToReal(gridpos[1]);
            double z1 = space.getImageAxis(Axis.Z_AXIS).gridToReal(gridpos[2]);

            cursorPos.set(new AnatomicalPoint3D(space, x1, y1, z1));
        }

        public AnatomicalPoint3D get() {
            RProperty<AnatomicalPoint3D> cpos = (RProperty<AnatomicalPoint3D>) getProperties()[0];
            IImageSpace3D space = (IImageSpace3D)getModel().getImageSpace();
            double gridx = space.getImageAxis(Axis.X_AXIS).gridPosition(cpos.get().getX());
            double gridy = space.getImageAxis(Axis.Y_AXIS).gridPosition(cpos.get().getY());
            double gridz =  space.getImageAxis(Axis.Z_AXIS).gridPosition(cpos.get().getZ());

            float[] ret = space.gridToWorld((float)gridx, (float)gridy, (float)gridz);
            return new AnatomicalPoint3D(Anatomy3D.REFERENCE_ANATOMY, ret[0], ret[1], ret[2]);
        }
    };


    public final Property<Double> cursorX = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D((IImageSpace3D) getModel().getImageSpace(), aDouble, cursorPos.get().getY(), cursorPos.get().getZ()));
        }

        public Double get() {
            return cursorPos.get().getX();
        }
    };

    public final Property<Double> cursorY = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D((IImageSpace3D) getModel().getImageSpace(), cursorPos.get().getX(), aDouble, cursorPos.get().getZ()));
        }

        public Double get() {
            return cursorPos.get().getY();
        }
    };

    public final Property<Double> cursorZ = new ObservableProperty<Double>() {
        public void set(Double aDouble) {
            super.set(aDouble);
            cursorPos.set(new AnatomicalPoint3D((IImageSpace3D) getModel().getImageSpace(), cursorPos.get().getX(), cursorPos.get().getY(), aDouble));
        }

        public Double get() {
            return cursorPos.get().getZ();
        }
    };


    private InterpolationType screenInterpolation = InterpolationType.NEAREST_NEIGHBOR;

    private ImagePlotLayout plotLayout = new SimplePlotLayout(this, Anatomy3D.getCanonicalAxial());

    private SliceController sliceController;

    protected Viewport3D viewport;

    private ViewportHandler viewportHandler = new ViewportHandler();


    private PlotSelectionHandler plotSelectionHandler = new PlotSelectionHandler();

    private ImageLayerSelectionListener layerSelectionListener = new ImageLayerSelectionListener();

    //private CommandContainer commandContainer;


    public ImageView(IImageDisplayModel imodel) {
        super();
        BeanContainer.bind(this);
        displayModel.set(imodel);
        //commandContainer = new CommandContainer();

        initView();

        // getInputMap().put(KeyStroke.getKeyStroke("LEFT"),
        //                      "none");


    }


    public ImageView(IImageDisplayModel imodel, CommandContainer parentContainer) {
        super();

        BeanContainer.bind(this);
        displayModel.set(imodel);

        //commandContainer = new CommandContainer(parentContainer);

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


        plotList.set(plots);
        plotSelection.set(0);

        sliceController = plotLayout.createSliceController();

        revalidate();
    }

    public void clearListeners() {
        viewport.removePropertyChangeListener(viewportHandler);
        displayModel.get().removeImageDisplayModelListener(this);
        BeanContainer.get().removeListener(displayModel.get().getListSelection(), layerSelectionListener);

        removeMouseListener(plotSelectionHandler);

    }


    protected void registerListeners() {
        viewport.addPropertyChangeListener(viewportHandler);

        displayModel.get().addImageDisplayModelListener(this);

        addMouseListener(plotSelectionHandler);
        BeanContainer.get().addListener(displayModel.get().getListSelection(), layerSelectionListener);


    }

    private void updateView() {
        clearListeners();
        plotLayout.setView(this);
        setPlotLayout(plotLayout);

        initView();


    }

    private void initView() {

        viewport = new Viewport3D(getModel());

        AnatomicalPoint3D centroid = (AnatomicalPoint3D) getModel().getImageSpace().getCentroid();
        cursorPos.set(new CursorPosition((IImageSpace3D) getModel().getImageSpace(), centroid.getX(), centroid.getY(), centroid.getZ()));

        registerListeners();
        setPlotLayout(plotLayout);


    }


    public SliceController getSliceController() {
        return sliceController;
    }


    public boolean isPreserveAspect() {
        return preserveAspect.get();
    }

    public void setPreserveAspect(boolean _preserveAspect) {
        preserveAspect.set(_preserveAspect);
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

    public void setSelectedPlot(IImagePlot plot) {
        int idx = plotList.get().indexOf(plot);
        if (idx >= 0) {
            plotSelection.set(idx);
        } else {
            throw new IllegalArgumentException("plot argument not contained in this view");
        }

    }

    public IImagePlot getSelectedPlot() {
        int idx = plotSelection.get();
        if (idx >= 0) {
            return plotList.get(idx);
        }

        return null;

    }

    public ImageLayer getSelectedLayer() {
        return displayModel.get().getSelectedLayer();
    }

    public int getSelectedLayerIndex() {
        return displayModel.get().getSelectedIndex();
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


    public AnatomicalPoint3D getCursorPos() {
        return cursorPos.get();
    }

    public Viewport3D getViewport() {
        return viewport;
    }

    public ImageAxis getImageAxis(AnatomicalAxis axis) {
        return getModel().getImageAxis(axis);
    }

    public ImageAxis getImageAxis(Axis axis) {
        return getModel().getImageAxis(axis);
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

        AnatomicalPoint3D ap = cursorPos.get();
        //todo fix me
        AnatomicalPoint3D dispAP = null;

        double xpt = dispAP.getValue(Axis.X_AXIS.getId());
        double ypt = dispAP.getValue(Axis.Y_AXIS.getId());

        double percentX = (xpt - plot.getXAxisRange().getBeginning().getValue()) / plot.getXAxisRange().getInterval();
        double percentY = (ypt - plot.getYAxisRange().getBeginning().getValue()) / plot.getYAxisRange().getInterval();

        Rectangle2D plotArea = plot.getPlotArea();

        double screenX = (percentX * plotArea.getWidth()) + plotArea.getX();
        double screenY = (percentY * plotArea.getHeight()) + plotArea.getY();

        Point location = new Point((int) Math.round(screenX), (int) Math.round(screenY));
        return SwingUtilities.convertPoint(plot.getComponent(), location, this);


    }

    public boolean pointInPlot(Component source, Point p) {
        Point viewPoint = SwingUtilities.convertPoint(source, p, this);
        return (!(whichPlot(viewPoint) == null));

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
                getCursorPos().getValue(displayAnatomy.ZAXIS).getValue());


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
        Dimension d = plotLayout.getPreferredSize();

        d.setSize(d.getWidth() * pixelsPerUnit.get().doubleValue(), d.getHeight() * pixelsPerUnit.get().doubleValue());
        return d;
    }

    public String toString() {
        return identifier.get();
    }

    class ImageLayerSelectionListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {

            int selectionIndex = (Integer) newValue;
            if (selectionIndex >= 0) {

                EventBus.publish(new ImageViewLayerSelectionEvent(ImageView.this, selectionIndex));
            }

        }


    }


    class CrosshairHandler implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {


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
                setSelectedPlot(plot);
                //getPlotSelection().setSelection(plot);
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
