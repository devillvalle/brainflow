package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.zookitec.layout.Expression;

import javax.swing.*;
import java.text.NumberFormat;

import org.pietschy.explicit.TableBuilder;
import org.pietschy.explicit.DebugPanel;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 9:03:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoubleSliderForm3 extends JPanel {



    private TableBuilder builder;

    private JSlider slider1;
    private JSlider slider2;

    private JLabel sliderLabel1;
    private JLabel sliderLabel2;

    private JFormattedTextField valueField1;
    private JFormattedTextField valueField2;

    public DoubleSliderForm3() {
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


    public JFormattedTextField getValueField1() {
        return valueField1;
    }

    public JFormattedTextField getValueField2() {
        return valueField2;
    }


    private void buildGUI() {

        //builder = new TableBuilder(new DebugPanel());
        builder = new TableBuilder();
        builder.setMargin(builder.layoutStyle().dialogMargin());


        slider1 = new JSlider();
        slider2 = new JSlider();



        sliderLabel1 = new JLabel("X:");
        sliderLabel2 = new JLabel("Y:");
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumIntegerDigits(1);


        valueField1 = new JFormattedTextField(format);
        valueField1.setText("   ");
        valueField2 = new JFormattedTextField(format);
        valueField2.setText("   ");

        // add some components.
        int row = 0;

        builder.add(sliderLabel1, row, 0).alignLeft();
        builder.add(valueField1,  row, 1, 1, 2).alignLeft().minimumWidth(45);
        row++;
        builder.add(slider1, row, 0, 1, 2).fillX();

        row++;

        builder.add(sliderLabel2, row, 0).alignLeft();
        builder.add(valueField2,  row, 1, 1, 2).alignLeft().minimumWidth(45);
        row++;
        builder.add(slider2, row, 0, 1, 2).fillX();

        builder.buildLayout();
        add(builder.getPanel());



    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new DoubleSliderForm3());
        frame.pack();
        frame.setVisible(true);

    }


}