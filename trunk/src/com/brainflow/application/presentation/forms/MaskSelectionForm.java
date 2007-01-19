package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.CheckBoxList;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 1, 2006
 * Time: 5:21:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskSelectionForm extends JPanel {

    private CheckBoxList maskList;

    private JLabel heading;

    private FormLayout layout;


    public MaskSelectionForm() {
        maskList = new CheckBoxList(new String[]{"Hello", "Goodbye"});

        heading = new JLabel("Mask With:");
        layout = new FormLayout("5dlu, max(100dlu;p):g, 5dlu", "8dlu, p, 5dlu, max(80dlu;p):g, 1dlu, 8dlu");
        CellConstraints cc = new CellConstraints();
        setLayout(layout);


        add(heading, cc.xy(2, 2));
        add(maskList, cc.xywh(2, 4, 1, 2));


    }


    public CheckBoxList getMaskList() {
        return maskList;
    }

    public static void main(String[] args) {
        MaskSelectionForm form = new MaskSelectionForm();
        JFrame jf = new JFrame();
        jf.add(form, BorderLayout.CENTER);
        jf.pack();
        jf.setVisible(true);


    }
}
