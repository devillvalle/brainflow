/*
 * LayerControlPanel.java
 *
 * Created on July 11, 2006, 4:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.brainflow.application.presentation;

import com.brainflow.gui.AbstractPresenter;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;

import javax.swing.*;

/**
 * @author buchs
 */
public class ColorAdjustmentControl {


    private CollapsiblePanes cpanes;

    private SelectedLayerPresenter selectedLayerPresenter;
    private ColorRangePresenter colorRangePresenter;
    private ThresholdRangePresenter thresholdRangePresenter;
    private OpacityPresenter opacityPresenter;
    private ColorBarPresenter colorBarPresenter;
    private InterpolationPresenter interpolationPresenter;

    /**
     * Creates a new instance of LayerControlPanel
     */
    public ColorAdjustmentControl() {
        init();
    }


    public JComponent getComponent() {
        return cpanes;
    }

    private void addCollapsiblePane(AbstractPresenter presenter, String title) {
        CollapsiblePane cp = new CollapsiblePane();
        cp.setContentPane(presenter.getComponent());
        cp.setTitle(title);
        cp.setEmphasized(true);
        cp.setOpaque(false);

        cpanes.add(cp);


    }


    private void init() {
        cpanes = new CollapsiblePanes();

        selectedLayerPresenter = new SelectedLayerPresenter();
        addCollapsiblePane(selectedLayerPresenter, "Selection");

        colorBarPresenter = new ColorBarPresenter();
        addCollapsiblePane(colorBarPresenter, "Color Map");

        colorRangePresenter = new ColorRangePresenter();
        addCollapsiblePane(colorRangePresenter, "Color Range");

        thresholdRangePresenter = new ThresholdRangePresenter();
        addCollapsiblePane(thresholdRangePresenter, "Thresholds");

        opacityPresenter = new OpacityPresenter();
        addCollapsiblePane(opacityPresenter, "Opacity");

        interpolationPresenter = new InterpolationPresenter();
        addCollapsiblePane(interpolationPresenter, "Resample Method");

        cpanes.addExpansion();


    }


}
