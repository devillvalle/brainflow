package com.brainflow.application.presentation;

import com.brainflow.application.actions.RotateLayersCommand;
import com.brainflow.application.dnd.AbstractLayerTransferable;
import com.brainflow.application.presentation.controls.CoordinateSpinner;
import com.brainflow.application.presentation.binding.ExtBind;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.SliceRenderer;
import com.brainflow.gui.ToggleBar;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.space.IImageSpace;
import com.pietschy.command.ActionCommand;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 7:36:06 AM
 */
public class CanvasBarX extends ImageViewPresenter {


    private JToolBar canvasBar;

    private ToggleBar toggleBar;



    //private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    private ActionCommand rotateCommand = new RotateLayersCommand();




    public CanvasBarX() {
        super();

        buildGUI();


    }

    private void buildGUI() {
        canvasBar = new JToolBar();

        BoxLayout layout = new BoxLayout(canvasBar, BoxLayout.X_AXIS);
        canvasBar.setLayout(layout);

        AbstractButton rotateButton = rotateCommand.createButton();
        
        canvasBar.add(rotateButton);

        toggleBar = new ToggleBar(Arrays.asList("Tabula Rasa"));
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