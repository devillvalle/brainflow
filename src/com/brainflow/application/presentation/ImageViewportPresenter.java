package com.brainflow.application.presentation;

import com.brainflow.application.toplevel.ImageCanvasManager;
import com.brainflow.core.IImagePlot;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageView;
import com.brainflow.core.SimpleImageView;
import com.brainflow.core.annotations.BoxAnnotation;
import com.brainflow.display.Viewport3D;
import com.brainflow.image.space.Axis;
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
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.*;

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


    private SpinnerNumberModel xspinnerModel;

    private SpinnerNumberModel yspinnerModel;

    private BeanAdapter viewportAdapter;

    private BoundedRangeAdapter xfovAdapter;

    private BoundedRangeAdapter yfovAdapter;

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
        boxView.addAnnotation(boxAnnotation);
        viewPanel.setLayout(new JideBorderLayout());
        viewPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        //viewPanel.setBackground(Color.BLACK);
        viewPanel.add(boxView, BorderLayout.CENTER);

        xfovSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        yfovSlider = new JSlider(JSlider.VERTICAL, 0, 100, 0);

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

    private double getWidth(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getXAxisRange().getAnatomicalAxis(), true).getRange().getInterval();

    }

    private double getHeight(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        return view.getModel().getImageSpace().getImageAxis(plot.getYAxisRange().getAnatomicalAxis(), true).getRange().getInterval();

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

    private void updateView(ImageView view) {
        viewPanel.remove(boxView);

        boxView = new SimpleImageView(view.getModel(), view.getSelectedPlot().getDisplayAnatomy());

        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2, 2, 2, 2));
        boxAnnotation.setVisible(true);
        boxAnnotation.setWidth(getWidth(view));
        boxAnnotation.setHeight(getHeight(view));

        boxView.addAnnotation(boxAnnotation);

        viewPanel.add(boxView);
        boxView.getSelectedPlot().getComponent().addMouseMotionListener(panner);
        boxView.getSelectedPlot().getComponent().addMouseListener((MouseListener)panner);

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
        Viewport3D viewport = view.getViewport();
        IImagePlot plot = view.getSelectedPlot();

        viewportAdapter = new BeanAdapter(viewport, true);

        ValueModel xextent = viewportAdapter.getValueModel(viewport.getWidthPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel yextent = viewportAdapter.getValueModel(viewport.getWidthPropertyName(plot.getYAxisRange().getAnatomicalAxis()));


        xfovAdapter = new BoundedRangeAdapter(new PercentageConverter(xextent,
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.X_AXIS).getRange().getInterval()), 100), 0, 0, 100);
        xfovSlider.setModel(xfovAdapter);

        yfovAdapter = new BoundedRangeAdapter(new PercentageConverter(yextent,
                new ValueHolder(10),
                new ValueHolder(viewport.getBounds().getImageAxis(Axis.Y_AXIS).getRange().getInterval()), 100), 0, 0, 100);
        yfovSlider.setModel(yfovAdapter);

        PropertyConnector.connect(xextent, "value", yextent, "value");

    }



    private void bindComponents() {
        // assumes view not null;

        ImageView view = getSelectedView();

        Bindings.bind(plotSelector, view.getPlotSelection());


        Viewport3D viewport = view.getViewport();
        IImagePlot plot = view.getSelectedPlot();


        if (viewportAdapter == null) {
            intializeAdapters();
        } else {
            viewportAdapter.setBean(viewport);
        }


        ValueModel xval = viewportAdapter.getValueModel(viewport.getMinPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel yval = viewportAdapter.getValueModel(viewport.getMinPropertyName(plot.getYAxisRange().getAnatomicalAxis()));
        ValueModel wval = viewportAdapter.getValueModel(viewport.getWidthPropertyName(plot.getXAxisRange().getAnatomicalAxis()));
        ValueModel hval = viewportAdapter.getValueModel(viewport.getWidthPropertyName(plot.getYAxisRange().getAnatomicalAxis()));

        PropertyAdapter xminAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.XMIN_PROPERTY);
        PropertyAdapter yminAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.YMIN_PROPERTY);
        PropertyAdapter widthAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.WIDTH_PROPERTY);
        PropertyAdapter heightAdapter = new PropertyAdapter(boxAnnotation, BoxAnnotation.HEIGHT_PROPERTY);

        PropertyConnector xconnector = new PropertyConnector(xval, "value", xminAdapter, "value");
        PropertyConnector yconnector = new PropertyConnector(yval, "value", yminAdapter, "value");
        PropertyConnector widthconnector = new PropertyConnector(wval, "value", widthAdapter, "value");
        PropertyConnector heightconnector = new PropertyConnector(hval, "value", heightAdapter, "value");

        xconnector.updateProperty2();
        yconnector.updateProperty2();
        widthconnector.updateProperty2();
        heightconnector.updateProperty2();

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


    }

    public void allViewsDeselected() {
        boxView.removeMouseMotionListener(panner);
        boxView.removeMouseListener((MouseListener)panner);
        setEnabled(false);
    }

    public JComponent getComponent() {
        return form;
    }


    class Panner extends MouseAdapter implements MouseMotionListener {

        private Point2D lastPoint = null;


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
                    Point2D next = boxAnnotation.translateFromJava2D(plot, e.getPoint());
                    Number xold = (Number)xorigin.getValue();
                    Number yold = (Number)yorigin.getValue();

                    //ImageView selView = getSelectedView();
                    //Viewport3D viewport = selView.getViewport();

                    xspinnerModel.setValue(xold.doubleValue() + (next.getX() - lastPoint.getX()) );
                    yspinnerModel.setValue(yold.doubleValue() + (next.getY() - lastPoint.getY()) );
                    lastPoint = next;
                }
            }
        }


}
