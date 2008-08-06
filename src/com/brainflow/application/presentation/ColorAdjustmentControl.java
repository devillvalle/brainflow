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
import java.beans.PropertyVetoException;

/**
 * @author buchs
 */
public class ColorAdjustmentControl extends AbstractPresenter {


    private CollapsiblePanes cpanes;

    private SelectedLayerPresenter selectedLayerPresenter;

    private ColorRangePresenter2 colorRangePresenter;

    private HistogramPresenter histogramPresenter;

    private ThresholdRangePresenter2 thresholdRangePresenter;

    private RenderingParamsPresenter renderParamsPresenter;

    private ColorBarPresenter colorBarPresenter;

    //private InterpolationPresenter interpolationPresenter;

    /**
     * Creates a new instance of LayerControlPanel
     */
    public ColorAdjustmentControl() {
        init();
    }


    public JComponent getComponent() {
        return cpanes;
    }

    private void addCollapsiblePane(AbstractPresenter presenter, String title, boolean collapsed) {
        CollapsiblePane cp = new CollapsiblePane();

        //cp.setUI(new VsnetCollapsiblePaneUI());
        cp.setContentPane(presenter.getComponent());
        cp.setTitle(title);

        cp.setEmphasized(true);
        cp.setOpaque(false);
        //cp.setStyle(CollapsiblePane.PLAIN_STYLE);

        try {
            cp.setCollapsed(collapsed);
        } catch (PropertyVetoException e) {
            // not that important ...
        }

        cpanes.add(cp);


    }


    private void init() {
        cpanes = new CollapsiblePanes();

        selectedLayerPresenter = new SelectedLayerPresenter();
        addCollapsiblePane(selectedLayerPresenter, "Selection", false);

        colorBarPresenter = new ColorBarPresenter();
        addCollapsiblePane(colorBarPresenter, "Color Map", false);


        colorRangePresenter = new ColorRangePresenter2();
        addCollapsiblePane(colorRangePresenter, "Color Range", false);

        histogramPresenter = new HistogramPresenter();
        addCollapsiblePane(histogramPresenter, "Histogram", true);


        thresholdRangePresenter = new ThresholdRangePresenter2();
        addCollapsiblePane(thresholdRangePresenter, "Thresholds", false);

        renderParamsPresenter = new RenderingParamsPresenter();
        addCollapsiblePane(renderParamsPresenter, "Rendering", true);

        //interpolationPresenter = new InterpolationPresenter();
        //addCollapsiblePane(interpolationPresenter, "Resample Method", true);

        cpanes.addExpansion();


    }


}
