package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 9:03:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleSliderForm extends JPanel {

    private FormLayout layout;

    private JSlider slider1;
    private JSlider slider2;

    private JLabel sliderLabel1;
    private JLabel sliderLabel2;

    private JLabel valueLabel1;
    private JLabel valueLabel2;

    public DoubleSliderForm() {
        buildGUI();
    }

    public JSlider getSlider1() {
        return slider1;
    }

    public JSlider getSlider2() {
        return slider2;
    }


    public JLabel getSliderLabel1() {
        return sliderLabel1;
    }

    public JLabel getSliderLabel2() {
        return sliderLabel2;
    }


    public JLabel getValueLabel1() {
        return valueLabel1;
    }

    public JLabel getValueLabel2() {
        return valueLabel2;
    }


    private void buildGUI() {

        layout = new FormLayout("6dlu, p, p, 6dlu:g, 6dlu", "6dlu, p, 3dlu, p, 8dlu, p, 3dlu, p, 6dlu");
        setLayout(layout);

        slider1 = new JSlider();
        slider2 = new JSlider();

        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");


        valueLabel1 = new JLabel("0");
        valueLabel2 = new JLabel("0");

        CellConstraints cc = new CellConstraints();

        add(slider1, cc.xywh(2, 4, 3, 1));
        add(slider2, cc.xywh(2, 8, 3, 1));

        add(sliderLabel1, cc.xy(2, 2));
        add(sliderLabel2, cc.xy(2, 6));

        add(valueLabel1, cc.xy(3, 2));
        add(valueLabel2, cc.xy(3, 6));


    }


}
