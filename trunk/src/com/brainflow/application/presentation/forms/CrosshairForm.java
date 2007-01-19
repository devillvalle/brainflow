package com.brainflow.application.presentation.forms;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.combobox.ColorComboBox;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 12, 2007
 * Time: 8:35:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrosshairForm extends JPanel {

    private FormLayout layout;

    private ColorComboBox colorChooser;
    private JCheckBox visibleBox = new JCheckBox("Crosshair Visible");
    private JSpinner lineWidthSpinner;
    private JSpinner gapSpinner;

    public CrosshairForm() {
        buildGUI();

    }


    private void buildGUI() {

        layout = new FormLayout("6dlu, l:p, 10dlu, r:p:g, 3dlu, 6dlu", "6dlu, p, 12dlu, p, 12dlu, p, 12dlu, p, 8dlu");
        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        layout.addGroupedColumn(4);

        colorChooser = new ColorComboBox();
        //
        lineWidthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        gapSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 1, .1));


        add(visibleBox, cc.xyw(2, 2, 3));
        add(new JLabel("Line Color:"), cc.xy(2, 4));
        add(colorChooser, cc.xyw(4, 4, 2));
        add(new JLabel("Line Width:"), cc.xy(2, 6));
        add(lineWidthSpinner, cc.xyw(4, 6, 2));
        add(new JLabel("Gap:"), cc.xy(2, 8));
        add(gapSpinner, cc.xyw(4, 8, 2));
        colorChooser.setSelectedColor(Color.GREEN);
        colorChooser.setColorValueVisible(false);
        //colorChooser.setColorValueVisible(false);

    }


    public FormLayout getLayout() {
        return layout;
    }

    public ColorComboBox getColorChooser() {
        return colorChooser;
    }

    public JCheckBox getVisibleBox() {
        return visibleBox;
    }

    public JSpinner getLineWidthSpinner() {
        return lineWidthSpinner;
    }

    public JSpinner getGapSpinner() {
        return gapSpinner;
    }

    public static void main(String[] args) throws Exception {
        SyntheticaLookAndFeel lf = new SyntheticaStandardLookAndFeel();

        javax.swing.UIManager.setLookAndFeel(lf);
        JFrame jf = new JFrame();
        jf.add(new CrosshairForm());
        jf.setVisible(true);
        jf.pack();
    }
}
