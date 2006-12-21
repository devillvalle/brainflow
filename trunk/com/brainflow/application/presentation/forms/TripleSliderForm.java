package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 1:44:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TripleSliderForm extends JPanel {

    private FormLayout layout;

    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;

    private JLabel sliderLabel1;
    private JLabel sliderLabel2;
    private JLabel sliderLabel3;

    private JLabel valueLabel1;
    private JLabel valueLabel2;
    private JLabel valueLabel3;

    public TripleSliderForm() {
        buildGUI();

    }


    public JSlider getSlider1() {
        return slider1;
    }

    public JSlider getSlider2() {
        return slider2;
    }

    public JSlider getSlider3() {
        return slider3;
    }

    public JLabel getSliderLabel1() {
        return sliderLabel1;
    }

    public JLabel getSliderLabel2() {
        return sliderLabel2;
    }

    public JLabel getSliderLabel3() {
        return sliderLabel3;
    }

    public JLabel getValueLabel1() {
        return valueLabel1;
    }

    public JLabel getValueLabel2() {
        return valueLabel2;
    }

    public JLabel getValueLabel3() {
        return valueLabel3;
    }

    private void buildGUI() {
        layout = new FormLayout("6dlu, l:p, 3dlu, 125dlu:g, 3dlu, l:max(p;25dlu), 3dlu", "8dlu, p, 12dlu, p, 12dlu, p, 8dlu");
        setLayout(layout);

        slider1 = new JSlider();
        slider2 = new JSlider();
        slider3 = new JSlider();

        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");
        sliderLabel3 = new JLabel("Z:");


        valueLabel1 = new JLabel("0");
        valueLabel2 = new JLabel("0");
        valueLabel3 = new JLabel("0");

        CellConstraints cc = new CellConstraints();

        add(slider1, cc.xy(4, 2));
        add(slider2, cc.xy(4, 4));
        add(slider3, cc.xy(4, 6));

        add(sliderLabel1, cc.xy(2, 2));
        add(sliderLabel2, cc.xy(2, 4));
        add(sliderLabel3, cc.xy(2, 6));

        add(valueLabel1, cc.xy(6, 2));
        add(valueLabel2, cc.xy(6, 4));
        add(valueLabel3, cc.xy(6, 6));

        //layout.addGroupedRow();


    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new TripleSliderForm(), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);
    }


}
