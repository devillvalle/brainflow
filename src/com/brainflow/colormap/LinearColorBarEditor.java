package com.brainflow.colormap;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.PropertyConnector;
import com.jgoodies.binding.value.Trigger;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.popup.JidePopup;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Bradley
 * Date: Apr 28, 2005
 * Time: 9:00:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class LinearColorBarEditor extends JPanel implements ChangeListener, ItemListener {

    private LinearColorMap colorMap;
    private FormLayout layout;

    private AbstractColorBar colorBar;
    private JLabel minLabel = new JLabel("min:");
    private JLabel maxLabel = new JLabel("max:");

    // binding
    private final Trigger trigger = new Trigger();


    private JFormattedTextField sliderField;
    private JSlider slider;
    private JSpinner binSpinner;
    private static final int SLIDER_RANGE = 1000;

    private JComboBox sliderSelector = new JComboBox();

    private static String[] sliderLabels = {"Upper Threshold", "Lower Threshold", "Max Value", "Min Value"};

    JFormattedTextField jtf;

    public LinearColorBarEditor(LinearColorMap _colorMap) {
        colorMap = _colorMap;
        colorBar = colorMap.createColorBar();
        colorBar.setBorder(BorderFactory.createLineBorder(Color.black));
        PresentationModel presentation = new PresentationModel(colorMap, trigger);


        ValueModel highClipModel = presentation.getBufferedModel(LinearColorMap.HIGH_CLIP_PROPERTY);


        NumberFormat format = DecimalFormat.getNumberInstance();
        format.setMaximumFractionDigits(1);

        sliderField = new JFormattedTextField(format);
        sliderField.setEditable(false);
        Bindings.bind(sliderField, highClipModel);


        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(colorMap.getMapSize(), 1, 1000, 1);
        binSpinner = new JSpinner(spinnerModel);
        ValueModel mapSizeModel = presentation.getModel(LinearColorMap.MAP_SIZE_PROPERTY);
        binSpinner.setToolTipText("Number of discrete bins in color map");
        final PropertyConnector connector = PropertyConnector.connect(mapSizeModel, "value", binSpinner, "value");
        connector.updateProperty1();


        binSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                connector.updateProperty1();
            }
        });


        slider = new JSlider(JSlider.VERTICAL, 0, colorMap.getMapSize() - 1, 0);
        slider.addChangeListener(this);


        for (String lab : sliderLabels) {
            sliderSelector.addItem(lab);
        }

        sliderSelector.addItemListener(this);

        setupLayout();


    }


    private void setupLayout() {


        layout = new FormLayout("5dlu, 18mm, 2dlu,  max(p;35dlu), l:p, 3dlu, max(p;30dlu), " +
                "l:p:g, 12dlu", "5dlu, p, 3dlu, max(12dlu;p), p:g, 5dlu, 1dlu, p, 3dlu, max(12dlu;p), 5dlu");

        setLayout(layout);

        CellConstraints cc = new CellConstraints();
        add(colorBar, cc.xywh(2, 5, 1, 2));


        add(sliderSelector, cc.xywh(2, 2, 3, 1));
        add(binSpinner, cc.xy(2, 10));
        add(slider, cc.xywh(5, 5, 1, 2));
        add(sliderField, cc.xy(4, 5));

        add(maxLabel, cc.xy(2, 4));
        add(minLabel, cc.xy(2, 8));

    }


    private int valueToSlider(double val, double min, double max) {
        double range = max - min;
        double percent = (val - min) / range;
        return (int) percent * SLIDER_RANGE;
    }


    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "Brainflow", "7.YTcWgxxjx1xjSnUqG:U1ldgGetfRn1");

        try {

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);
        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Could not load Look and Feel, aborting");
        }


        LinearColorMap cmap = new LinearColorMap(-100, 100, ColorTable.SPECTRUM);
        JFrame frame = new JFrame();
        LinearColorBarEditor editor = new LinearColorBarEditor(cmap);

        frame.add(editor, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);


    }


    public void stateChanged(ChangeEvent e) {
        JSlider slider = (JSlider) e.getSource();
        int value = slider.getValue();

        ColorInterval interval = colorMap.getInterval(value);
        sliderField.setValue(interval.getMinimum());

        /*if (!slider.getValueIsAdjusting()) {
            JidePopup popup = new JidePopup();
            ColorIntervalEditor editor = new ColorIntervalEditor(interval);
            popup.setMovable(true);
            popup.getContentPane().setLayout(new BorderLayout());
            popup.getContentPane().add(editor);
            popup.setOwner(colorBar);
            popup.setMovable(true);
            popup.showPopup();

            trigger.triggerCommit();
            System.out.println("trigger = " + trigger.getValue());

        } */


    }

    public void itemStateChanged(ItemEvent e) {
        JComboBox selector = (JComboBox) e.getSource();

    }
}
