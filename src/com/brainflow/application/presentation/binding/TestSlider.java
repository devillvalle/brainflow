package com.brainflow.application.presentation.binding;

import net.java.dev.properties.Property;
import net.java.dev.properties.binding.swing.adapters.SwingBind;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 10, 2007
 * Time: 10:12:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSlider extends JPanel {

    public final Property<Double> dvalue = ObservableProperty.create(.5);

    public TestSlider() {
        BeanContainer.bind(this);
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        SwingBind.get().bind(new PercentageConverterProperty(dvalue, 0, 1, 100), slider);
        add(slider);


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new TestSlider(), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
