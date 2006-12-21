/*
 * ColorBarPresenter.java
 *
 * Created on July 13, 2006, 2:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.actions.ActionContext;
import com.brainflow.actions.SelectColorMapAction;
import com.brainflow.application.managers.ImageCanvasManager;
import com.brainflow.application.managers.ResourceManager;
import com.brainflow.application.presentation.forms.ColorBarForm;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageView;
import com.brainflow.display.DisplayParameter;
import com.jidesoft.swing.JideSplitButton;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.image.IndexColorModel;
import java.util.*;

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
        colorMap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        init();


    }

    public ColorBarPresenter(IColorMap _colorMap) {
        colorMap = _colorMap;
        init();

    }

    private void init() {
        form = new ColorBarForm(colorMap);
        List<Action> actions = createColorMapActions();

        Iterator<Action> iter = actions.iterator();
        JideSplitButton colorMenu = form.getColorMenu();
        while (iter.hasNext()) {
            colorMenu.add(iter.next());
        }
    }

    public void viewSelected(ImageView view) {
        super.viewSelected(view);
        Iterator<Action> iter = actions.iterator();
        while (iter.hasNext()) {
            BasicAction action = (BasicAction) iter.next();
            action.putContextValue(ActionContext.SELECTED_IMAGE_VIEW, view);
        }

    }


    public void setColorMap(DisplayParameter<IColorMap> param) {
        form.setColorMap(param.getParameter());
    }


    public JComponent getComponent() {
        return form;
    }

    public List<Action> createColorMapActions() {
        Map<String, IndexColorModel> maps = ResourceManager.getInstance().getColorMaps();
        actions = new ArrayList<Action>();
        Iterator<String> iter = maps.keySet().iterator();

        while (iter.hasNext()) {
            String name = iter.next();
            IndexColorModel icm = maps.get(name);
            SelectColorMapAction action = new SelectColorMapAction(name,
                    ColorTable.createImageIcon(icm, 30, 12), icm);


            Map map = new HashMap();
            map.put(ActionContext.SELECTED_IMAGE_VIEW, ImageCanvasManager.getInstance().
                    getSelectedCanvas().getSelectedView());
            action.setContext(map);
            actions.add(action);


        }

        return actions;
    }


}
