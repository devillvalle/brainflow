/*
 * ColorBarPresenter.java
 *
 * Created on July 13, 2006, 2:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.application.actions.ActionContext;
import com.brainflow.application.actions.DesignColorMapAction;
import com.brainflow.application.actions.SelectColorMapAction;
import com.brainflow.application.presentation.forms.ColorBarForm;
import com.brainflow.application.toplevel.ImageCanvasManager;
import com.brainflow.application.toplevel.ResourceManager;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.brainflow.core.ImageView;
import com.brainflow.display.Property;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideBoxLayout;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.image.IndexColorModel;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author buchs
 */
public class ColorBarPresenter extends AbstractColorMapPresenter {

    private ColorBarForm form;

    private IColorMap colorMap;

    private List<Action> actions;

    /**
     * Creates a new instance of ColorBarPresenter
     */


    public ColorBarPresenter() {
        colorMap = new LinearColorMapDeprecated(0, 255, ColorTable.GRAYSCALE);
        init();


    }

    public ColorBarPresenter(IColorMap _colorMap) {
        colorMap = _colorMap;
        init();

    }

    private void init() {
        form = new ColorBarForm(colorMap);
        List<Action> actions = createColorMapActions();
        JideSplitButton colorMenu = form.getColorMenu();


        Iterator<Action> iter = actions.iterator();

        while (iter.hasNext()) {
            colorMenu.add(iter.next());
        }

        colorMenu.add(new GradientColorAction("Solid Color ..."));
    }

    public void viewSelected(ImageView view) {
        super.viewSelected(view);
        Iterator<Action> iter = actions.iterator();
        while (iter.hasNext()) {
            BasicAction action = (BasicAction) iter.next();
            action.putContextValue(ActionContext.SELECTED_IMAGE_VIEW, view);
        }

    }


    public void setColorMap(Property<IColorMap> param) {
        form.setColorMap(param.getProperty());
    }


    public JComponent getComponent() {
        return form;
    }

    public List<Action> createColorMapActions() {
        Map<String, IndexColorModel> maps = ResourceManager.getInstance().getColorMaps();
        actions = new ArrayList<Action>();
        Iterator<String> iter = maps.keySet().iterator();
        Map map = new HashMap();
        map.put(ActionContext.SELECTED_IMAGE_VIEW, ImageCanvasManager.getInstance().
                getSelectedCanvas().getSelectedView());
        map.put(ActionContext.SELECTED_CANVAS, ImageCanvasManager.getInstance().
                getSelectedCanvas());

        while (iter.hasNext()) {
            String name = iter.next();
            IndexColorModel icm = maps.get(name);
            SelectColorMapAction action = new SelectColorMapAction(name,
                    ColorTable.createImageIcon(icm, 30, 12), icm);


            action.setContext(map);
            actions.add(action);


        }

        BasicAction designAction = new DesignColorMapAction("Custom Colors...");
        designAction.setContext(map);
        actions.add(designAction);

        return actions;
    }

    class SolidColorPanel extends JPanel {
        JCheckBox intensityCheckBox = new JCheckBox("ramp intensity");

        JColorChooser chooser = new JColorChooser();

        JPanel southPanel = new JPanel();

        public SolidColorPanel() {
            setLayout(new BorderLayout());

            chooser.setBorder(BorderFactory.createEtchedBorder());
            add(chooser, BorderLayout.CENTER);

            southPanel.setLayout(new JideBoxLayout(southPanel, JideBoxLayout.X_AXIS));
            southPanel.add(intensityCheckBox, JideBoxLayout.FIX);
            southPanel.setBorder(BorderFactory.createEtchedBorder());

            add(southPanel, BorderLayout.SOUTH);

        }

        public Color getColor() {
            return chooser.getColor();
        }

        public boolean getIntensityRamp() {
            return intensityCheckBox.isSelected();
        }
    }


    class GradientColorAction extends AbstractAction {


        public GradientColorAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            ImageView view = getSelectedView();
            int layer = view.getModel().getSelectedIndex();

            IColorMap oldMap = view.getModel().getLayer(layer).
                                    getImageLayerProperties().getColorMap().getProperty();

            ColorGradientEditor chooser = new ColorGradientEditor(oldMap.getMinimumValue(), oldMap.getMaximumValue());

            String[] options = {
                    "OK",
                    "Cancel",

            };


            int result = JOptionPane.showOptionDialog(
                    getComponent(),                             // the parent that the dialog blocks
                    chooser,                                  // the dialog message array
                    "Create Color Map",                 // the title of the dialog window
                    JOptionPane.OK_CANCEL_OPTION,                 // option type
                    JOptionPane.INFORMATION_MESSAGE,            // message type
                    null,                                       // optional icon, use null to use the default icon
                    options,                                    // options string array, will be made into buttons
                    options[0]                                  // option that should be made into a default button
            );


            if (result == 0) {




                IColorMap newMap = chooser.getColorMap();
               
                view.getModel().getLayer(layer).
                        getImageLayerProperties().getColorMap().setProperty(newMap);
            }


        }
    }


}
