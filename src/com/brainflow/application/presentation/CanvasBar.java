package com.brainflow.application.presentation;

import com.brainflow.application.actions.RotateLayersCommand;
import com.brainflow.application.presentation.binding.ExtBind;
import com.brainflow.core.ImageView;
import com.brainflow.gui.ToggleBarX;
import com.pietschy.command.ActionCommand;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 7:36:06 AM
 */
public class CanvasBar extends ImageViewPresenter {


    private JToolBar canvasBar;

    private ToggleBarX toggleBar;



    //private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    private ActionCommand rotateCommand = new RotateLayersCommand();




    public CanvasBar() {
        super();

        buildGUI();


    }

    private void buildGUI() {
        canvasBar = new JToolBar();

        BoxLayout layout = new BoxLayout(canvasBar, BoxLayout.X_AXIS);
        canvasBar.setLayout(layout);

        AbstractButton rotateButton = rotateCommand.createButton();
        
        canvasBar.add(rotateButton);

        toggleBar = new ToggleBarX(Arrays.asList("Tabula Rasa"));
        canvasBar.add(toggleBar);

    }
    private void unbind() {
        ExtBind.get().unbind(toggleBar);
    }


    private void bind() {

        ExtBind.get().bindContent(getSelectedView().getModel().getListModel(), toggleBar);
        ExtBind.get().bindToggleBar(getSelectedView().getModel().getListSelection(), toggleBar);
        
        
    }


    public void viewSelected(ImageView view) {
        bind();
    }

    public void viewDeselected(ImageView view) {
        ExtBind.get().unbind(toggleBar);
    }

    public JComponent getComponent() {
        return canvasBar;

    }

    public void allViewsDeselected() {
        unbind();
    }














    class DragListener extends MouseAdapter {


        public void mouseDragged(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler th = c.getTransferHandler();
            th.exportAsDrag(c, e, TransferHandler.MOVE);
        }


    }


}