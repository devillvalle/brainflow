package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 2:38:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayerEditor extends JPanel {

    private JLabel nameLabel;
    //private JCheckBox
    private JSpinner highRangeSpinner;
    private JSpinner lowRangeSpinner;
    private JSpinner highThreshSpinner;
    private JSpinner lowThreshSpinner;

    private JCheckBox visibleCheckBox;

    private JSlider opacitySlider;


    FormLayout layout;

    public ImageLayerEditor() {
        layout = new FormLayout("3dlu, l:p, 3dlu, max(p;35dlu), 5dlu, max(p;35dlu), 3dlu, max(p;25dlu), 3dlu", "3dlu, p, 5dlu, p, 5dlu, p, 5dlu, p, 3dlu, p , 3dlu");
        setLayout(layout);

        visibleCheckBox = new JCheckBox("Visible");

        CellConstraints cc = new CellConstraints();
        add(visibleCheckBox, cc.xywh(2, 2, 5, 1));

        lowRangeSpinner = new JSpinner();
        highRangeSpinner = new JSpinner();

        highThreshSpinner = new JSpinner();
        lowThreshSpinner = new JSpinner();

        add(new JLabel("Intensity"), cc.xy(2, 4));
        add(lowRangeSpinner, cc.xy(4, 4));
        add(highRangeSpinner, cc.xy(6, 4));

        add(new JLabel("Threshold"), cc.xy(2, 6));
        add(lowThreshSpinner, cc.xy(4, 6));
        add(highThreshSpinner, cc.xy(6, 6));

        opacitySlider = new JSlider();
        add(new JLabel("Opacity"), cc.xy(2, 8));
        add(opacitySlider, cc.xywh(4, 8, 3, 1));

        // visibleCheckBox = new JCheckBox("Visible");
        // add(visibleCheckBox, cc.xy(2,10));


    }

    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        JFrame jf = new JFrame();
        jf.add(new ImageLayerEditor(), BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);

    }
}
