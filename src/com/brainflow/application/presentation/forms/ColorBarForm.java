/*
 * ColorBarForm.java
 *
 * Created on July 13, 2006, 12:45 PM
 */

package com.brainflow.application.presentation.forms;

import com.brainflow.colormap.ColorBarPlot;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.combobox.ColorComboBox;

/**
 * @author buchs
 */
public class ColorBarForm extends javax.swing.JPanel {

    private IColorMap colorMap;

    private ColorBarPlot colorPlot;

    private JideSplitButton colorMenu;

    private FormLayout layout;

    /**
     * Creates new form ColorBarForm
     */
    public ColorBarForm(IColorMap _colorMap) {
        colorMap = _colorMap;
        colorPlot = new ColorBarPlot(colorMap);
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();
    }

    public ColorBarForm() {
        colorMap = new LinearColorMap(0, 255, ColorTable.SPECTRUM);
        colorPlot = new ColorBarPlot(colorMap);
        colorMenu = new JideSplitButton("Select Map");

        buildGUI();

    }


    public JideSplitButton getColorMenu() {
        return colorMenu;
    }

    public void setColorMap(IColorMap colorMap) {
        colorPlot.setColorMap(colorMap);

    }


    private void buildGUI() {

        layout = new FormLayout("3dlu, l:max(100dlu;p):g, 3dlu, 6dlu", "3dlu, p, 6dlu, max(35dlu;p), 6dlu");
        colorPlot = new ColorBarPlot(colorMap);
        CellConstraints cc = new CellConstraints();
        setLayout(layout);

        add(colorPlot, cc.xywh(2, 4, 2, 1));
        add(colorMenu, cc.xy(2, 2));

    }


}
