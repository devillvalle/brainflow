package com.brainflow.application.presentation;

import com.brainflow.gui.AbstractPresenter;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 1, 2006
 * Time: 6:15:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskControl {

    private CollapsiblePanes cpanes;

    private SelectedLayerPresenter selectedLayerPresenter;
    private MaskSelectionPresenter maskPresenter;

    /**
     * Creates a new instance of LayerControlPanel
     */
    public MaskControl() {
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
        addCollapsiblePane(selectedLayerPresenter, "Layer Selection");

        maskPresenter = new MaskSelectionPresenter();
        addCollapsiblePane(maskPresenter, "Transparency Masking");


        cpanes.addExpansion();


    }

}
