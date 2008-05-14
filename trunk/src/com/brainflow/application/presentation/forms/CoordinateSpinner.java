package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 13, 2008
 * Time: 7:08:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateSpinner extends JPanel {

    private JSpinner xspinner;

    private JSpinner yspinner;

    private JSpinner zspinner;

    private JLabel xlabel;

    private JLabel ylabel;

    private JLabel zlabel;

    private FormLayout layout;

    public CoordinateSpinner() {
        xlabel = new JLabel("X: ");
        ylabel = new JLabel("Y: ");
        zlabel = new JLabel("Z: ");

        xspinner = new JSpinner(new SpinnerNumberModel());
        yspinner = new JSpinner(new SpinnerNumberModel());
        zspinner = new JSpinner(new SpinnerNumberModel());

        layout = new FormLayout("8dlu, l:p, 2dlu, 20dlu, 3dlu, l:p, 2dlu, 24dlu, 3dlu, l:p, 2dlu, 24dlu, 8dlu", "8dlu, p, 8dlu");
        setLayout(layout);
        layout.addGroupedColumn(4);
        layout.addGroupedColumn(8);
        layout.addGroupedColumn(12);

        CellConstraints cc = new CellConstraints();

        add(xlabel, cc.xy(2, 2));
        add(xspinner, cc.xy(4, 2));

        add(ylabel, cc.xy(6, 2));
        add(yspinner, cc.xy(8, 2));


        add(zlabel, cc.xy(10, 2));
        add(zspinner, cc.xy(12, 2));


    }

    public JSpinner getXspinner() {
        return xspinner;
    }

    public JSpinner getYspinner() {
        return yspinner;
    }

    public JSpinner getZspinner() {
        return zspinner;
    }

    public JLabel getYlabel() {
        return ylabel;
    }

    public JLabel getXlabel() {
        return xlabel;
    }

    public JLabel getZlabel() {
        return zlabel;
    }

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.add(new CoordinateSpinner(), BorderLayout.CENTER);

        jf.pack();
        jf.setVisible(true);
    }
}
