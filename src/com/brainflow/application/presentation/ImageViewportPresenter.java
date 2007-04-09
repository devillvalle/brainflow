package com.brainflow.application.presentation;

import com.brainflow.application.toplevel.ImageCanvasManager;
import com.brainflow.application.YokeHandler;
import com.brainflow.core.IImagePlot;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageView;
import com.brainflow.core.SimpleImageView;
import com.brainflow.core.annotations.BoxAnnotation;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.anatomy.AnatomicalPoint2D;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.adapter.SpinnerAdapterFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideBorderLayout;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewportPresenter extends ImageViewPresenter {

    private JComboBox plotSelector = new JComboBox();

    private JPanel form;

    private JLabel choiceLabel;

    private YokeHandler yokeHandler;

    private SpinnerNumberModel xspinnerModel;

    private SpinnerNumberModel yspinnerModel;

    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter xfovAdapter;

    private BoundedRangeAdapter yfovAdapter;

    private PropertyConnector xoriginConnector;

    private PropertyConnector yoriginConnector;

    private PropertyConnector widthConnector;

    private PropertyConnector heightConnector;


    private JSpinner xorigin;

    private JSpinner yorigin;

    private JSlider xfovSlider;

    private JSlider yfovSlider;

    private FormLayout layout;

    private SimpleImageView boxView;

    private BoxAnnotation boxAnnotation = new BoxAnnotation();

    private JPanel viewPanel;

    private MouseMotionListener panner = new Panner();


    public ImageViewportPresenter() {
        buildGUI();
    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("8dlu, p:g, 6dlu, p:g, 8dlu", "8dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, max(150dlu;p):g, 1dlu, 3dlu, p, 6dlu");
        form.setLayout(layout);
        layout.addGroupedColumn(2);
        layout.addGroupedColumn(4);
        CellConstraints cc = new CellConstraints();

        choiceLabel = new JLabel("Selected Plot: ");
        form.add(choiceLabel, cc.xy(2, 2));
        form.add(plotSelector, cc.xywh(2, 4, 3, 1));

        xspinnerModel = new SpinnerNumberModel(0, 0, 100, 1);
        yspinnerModel = new SpinnerNumberModel(0, 0, 100, 1);

        xorigin = new JSpinner();
        xorigin.setModel(xspinnerModel);
        //xorigin.setEditor(new JSpinner.NumberEditor(xorigin, "###.#");

        yorigin = new JSpinner();
        yorigin.setModel(yspinnerModel);
        //yorigin.setEditor(yspinnerModel);

        form.add(new JLabel("Origin: "), cc.xy(2, 6));

        JPanel xpan = new JPanel();
        xpan.setLayout(new BoxLayout(xpan, BoxLayout.X_AXIS));
        xpan.add(new JLabel("X: "));
        xpan.add(xorigin);

        JPanel ypan = new JPanel();
        ypan.setLayout(new BoxLayout(ypan, BoxLayout.X_AXIS));
        ypan.add(new JLabel("Y: "));
        ypan.add(yorigin);

        form.add(xpan, cc.xy(2, 8));
        form.add(ypan, cc.xy(4, 8));

        viewPanel = new JPanel();
        boxView = new SimpleImageView(new ImageDisplayModel("NULL"));

        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2, 2, 2, 2));
        boxAnnotation.setVisible(false);
        boxView.setAnnotation(boxView.getSelectedPlot(), BoxAnnotation.ID, boxAnnotation);

        viewPanel.setLayout(new JideBorderLayout());
        viewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //viewPanel.setBackground(Color.BLACK);
        viewPanel.add(boxView, BorderLayout.CENTER);

        xfovSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        yfovSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);
        xfovSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateTooltips(getSelectedView());
            }
        });

        yfovSlider.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                updateTooltips(getSelectedView());
            }
        });


        viewPanel.setBorder(BorderFactory.createEmptyBorder());
        viewPanel.add(xfovSlider, BorderLayout.SOUTH);
        viewPanel.add(yfovSlider, BorderLayout.WEST);

        form.add(viewPanel, cc.xywh(1, 10, 4, 2));

        //form.add(fovSlider, cc.xywh(2,13,3,1));

        setEnabled(false);

    }

    private int convertXRangeToInteger(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        double interval = view.getModel().getImageSpace().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getInterval();
        double viewRange = plot.getXAxisRange().getInterval();

        return (int) ((viewRange / interval) * 100);

    }

    private int convertYRangeToInteger(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        double interval = view.getModel().getImageSpace().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getInterval();
        double viewRange = plot.getYAxisRange().getInterval();

        return (int) ((viewRange / interval) * 100);

    }

    private double getBoxWidth(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport().getProperty();
        return viewport.getRange(plot.getXAxisRange().getAnatomicalAxis()).getInterval();

    }

    private double getBoxMinX(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport().getProperty();
        return viewport.getRange(plot.getXAxisRange().getAnatomicalAxis()).getMinimum();

    }

    private double getBoxMinY(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport().getProperty();
        return viewport.getRange(plot.getYAxisRange().getAnatomicalAxis()).getMinimum();

    }


    private double getBoxHeight(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Viewport3D viewport = view.getViewport().getProperty();
        return viewport.getRange(plot.getYAxisRange().getAnatomicalAxis()).getInterval();


    }

    private double getXLowerBound(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getMinimum();
    }

    private double getYLowerBound(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getMinimum();
    }

    private double getXUpperBound(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getMaximum();
    }

    private double getYUpperBound(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getMaximum();
    }

    private void updateTooltips(ImageView view) {

        xfovSlider.setToolTipText(view.getSelectedPlot().getXAxisRange().getAnatomicalAxis().toString() + " : " +
                 view.getSelectedPlot().getXAxisRange().getInterval());

        yfovSlider.setToolTipText(view.getSelectedPlot().getYAxisRange().getAnatomicalAxis().toString() + " : " +
                 view.getSelectedPlot().getYAxisRange().getInterval());

    }

    private void updateView(ImageView view) {
        viewPanel.remove(boxView);

        updateTooltips(view);


        xorigin.setToolTipText("origin: " + view.getSelectedPlot().getXAxisRange().getAnatomicalAxis().min.toString());
        yorigin.setToolTipText("origin: " + view.getSelectedPlot().getYAxisRange().getAnatomicalAxis().min.toString());

        boxView = new SimpleImageView(view.getModel(), view.getSelectedPlot().getDisplayAnatomy());

        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2, 2, 2, 2));
        boxAnnotation.setVisible(true);

        // initialize box with correct origin for current plot viewport
        boxAnnotation.setXmin(getBoxMinX(view));
        boxAnnotation.setYmin(getBoxMinY(view));

        // initialize box with correct height and width for current plot viewport
        boxAnnotation.setWidth(getBoxWidth(view));
        boxAnnotation.setHeight(getBoxHeight(view));

        boxView.setAnnotation(boxView.getSelectedPlot(), BoxAnnotation.ID, boxAnnotation);
       
        viewPanel.add(boxView);
        boxView.getSelectedPlot().getComponent().addMouseMotionListener(panner);
        boxView.getSelectedPlot().getComponent().addMouseListener((MouseListener) panner);

        if (yokeHandler == null) {
            yokeHandler = new YokeHandler(boxView);
            yokeHandler.addSource(view);
        } else {
            yokeHandler.clearSources();
            yokeHandler.setTarget(boxView);
            yokeHandler.addSource(view);
        }

        /// what about unyoking old views?
        ImageCanvasManager.getInstance().yoke(view, boxView);

    }

    public void viewSelected(ImageView view) {

        bindComponents();
        updateView(view);
        setEnabled(true);
        form.revalidate();

    }

    private void intializeAdapters() {
        ImageView view = getSelectedView();
        Viewport3D viewport = view.getViewport().getProperty();
        IImagePlot plot = view.getSelectedPlot();

        if (viewportAdapter == null) {
            viewportAdapter = new BeanAdapter(viewport, true);
        } else {
            viewportAdapter.setBean(viewport);
        }

        ValueModel xextent = viewportAdapter.getValueModel(viewport.getExtentPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel yextent = viewportAdapter.getValueModel(viewport.getExtentPropertyName(plot.getYAxisRange().getAnatomicalAxis()));


        xfovAdapter = new BoundedRangeAdapter(new PercentageConverter(xextent,
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getInterval()), 100), 0, 0, 100);
        xfovSlider.setModel(xfovAdapter);

        yfovAdapter = new BoundedRangeAdapter(new PercentageConverter(yextent,
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getInterval()), 100), 0, 0, 100);
        yfovSlider.setModel(yfovAdapter);

        // this line links x and y extent so that changing one changes the other
        //PropertyConnector.connect(xextent, "value", yextent, "value");

    }

    private void connectBox(ValueModel xval, ValueModel yval, ValueModel width, ValueModel height) {
        PropertyAdapter xminAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.XMIN_PROPERTY);
        PropertyAdapter yminAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.YMIN_PROPERTY);
        PropertyAdapter widthAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.WIDTH_PROPERTY);
        PropertyAdapter heightAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.HEIGHT_PROPERTY);

        if (xoriginConnector != null) {
            xoriginConnector.release();
            yoriginConnector.release();
            widthConnector.release();
            heightConnector.release();
        }

        xoriginConnector = new PropertyConnector(xval, "value", xminAdapter, "value");
        yoriginConnector = new PropertyConnector(yval, "value", yminAdapter, "value");
        widthConnector = new PropertyConnector(width, "value", widthAdapter, "value");
        heightConnector = new PropertyConnector(height, "value", heightAdapter, "value");

        xoriginConnector.updateProperty2();
        yoriginConnector.updateProperty2();
        widthConnector.updateProperty2();
        heightConnector.updateProperty2();

    }

    private void bindComponents() {
        // assumes view not null;

        ImageView view = getSelectedView();

        Bindings.bind(plotSelector, view.getPlotSelection());


        Viewport3D viewport = view.getViewport().getProperty();
        IImagePlot plot = view.getSelectedPlot();


        intializeAdapters();


        ValueModel xval = viewportAdapter.getValueModel(viewport.getMinPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel yval = viewportAdapter.getValueModel(viewport.getMinPropertyName(plot.getYAxisRange().getAnatomicalAxis()));
        ValueModel wval = viewportAdapter.getValueModel(viewport.getExtentPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel hval = viewportAdapter.getValueModel(viewport.getExtentPropertyName(plot.getYAxisRange().getAnatomicalAxis()));

        connectBox(xval, yval, wval, hval);


        xspinnerModel = new SpinnerNumberModel((Number) xval.getValue(), getXLowerBound(view), getXUpperBound(view), 1);
        yspinnerModel = new SpinnerNumberModel((Number) yval.getValue(), getYLowerBound(view), getYUpperBound(view), 1);

        xorigin.setModel(xspinnerModel);
        yorigin.setModel(yspinnerModel);


        SpinnerAdapterFactory.connect(xspinnerModel, xval, xval.getValue());
        SpinnerAdapterFactory.connect(yspinnerModel, yval, yval.getValue());


    }

    private void setEnabled(boolean b) {
        form.setEnabled(b);
        xorigin.setEnabled(b);
        yorigin.setEnabled(b);
        xfovSlider.setEnabled(b);
        yfovSlider.setEnabled(b);


    }

    public void allViewsDeselected() {
        boxView.removeMouseMotionListener(panner);
        boxView.removeMouseListener((MouseListener) panner);
        setEnabled(false);
    }

    public JComponent getComponent() {
        return form;
    }


    class Panner extends MouseAdapter implements MouseMotionListener {

        private AnatomicalPoint2D lastPoint = null;


        public void mousePressed(MouseEvent e) {
            lastPoint = boxAnnotation.translateFromJava2D(boxView.getSelectedPlot(), e.getPoint());
        }

        public void mouseReleased(MouseEvent e) {
            lastPoint = null;
        }

        public void mouseMoved(MouseEvent e) {

        }


        public void mouseDragged(MouseEvent e) {
            if (lastPoint == null) return;

            IImagePlot plot = boxView.getSelectedPlot();

            if (boxAnnotation.containsPoint(plot, e.getPoint())) {
                AnatomicalPoint2D next = boxAnnotation.translateFromJava2D(plot, e.getPoint());
                   Number xold = (Number) xorigin.getValue();
                Number yold = (Number) yorigin.getValue();


                double newx = xold.doubleValue() + (next.getX() - lastPoint.getX());
                double newy = yold.doubleValue() + (next.getY() - lastPoint.getY());
                if (newx < plot.getXAxisRange().getMinimum()) {
                    return;
                } else if (newy < plot.getYAxisRange().getMinimum()) {
                    return;
                }


        
                boxView.setToolTipText("x : " + xorigin.toString());
                boxView.setToolTipText("y : " + yorigin.toString());


                xspinnerModel.setValue(newx);
                yspinnerModel.setValue(newy);
                lastPoint = next;
            }
        }
    }


}
