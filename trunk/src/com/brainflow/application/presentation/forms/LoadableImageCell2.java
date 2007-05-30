package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.StyledLabel;

import javax.swing.*;
import java.awt.*;

/**
 * @author Brad Buchsbaum
 */
public class LoadableImageCell2 extends javax.swing.JPanel {


    private JLabel filenameLabel;
    private JLabel iconLabel;
    private JLabel infoLabel;

    private JideButton openButton = new JideButton("Open");
    private JideButton removeButton = new JideButton("Remove");

    private FormLayout layout;


    /**
     * Creates new form LoadableImageCell
     */
    public LoadableImageCell2() {
        initComponents();
    }


    public JLabel getFilenameLabel() {
        return filenameLabel;
    }

    public void setFilenameLabel(JLabel filenameLabel) {
        this.filenameLabel = filenameLabel;
    }

    //public JLabel getIconLabel() {
    //    return iconLabel;
    //}

    //public void setIconLabel(JLabel iconLabel) {
    //    this.iconLabel = iconLabel;
    //}

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(JLabel infoLabel) {
        this.infoLabel = infoLabel;
    }


    private void initComponents() {

        layout = new FormLayout("3dlu, l:p, 6dlu, l:p, 3dlu:g, l:p, 3dlu",
                "3dlu, 1dlu, p, p, 1dlu, 3dlu");

        setLayout(layout);
        CellConstraints cc = new CellConstraints();


        //iconLabel = new JLabel();
        filenameLabel = new JLabel();
        infoLabel = new StyledLabel("test");

        openButton.setButtonStyle(JideButton.HYPERLINK_STYLE);
        openButton.setAlwaysShowHyperlink(true);
        openButton.setForeground(Color.BLUE);
        removeButton.setButtonStyle(JideButton.HYPERLINK_STYLE);
        removeButton.setAlwaysShowHyperlink(true);

        //add(iconLabel, cc.xywh(2, 1, 1, 5));
        add(filenameLabel, cc.xywh(2, 1, 1, 5));
        add(infoLabel, cc.xy(4, 4));
        add(openButton, cc.xy(6, 3));
        add(removeButton, cc.xy(6, 4));


    }

    


}
