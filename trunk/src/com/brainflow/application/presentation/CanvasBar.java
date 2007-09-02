package com.brainflow.application.presentation;

import com.brainflow.application.actions.RotateLayersCommand;
import com.brainflow.application.dnd.AbstractLayerTransferable;
import com.brainflow.core.AbstractLayer;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.pietschy.command.ActionCommand;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 12, 2007
 * Time: 7:36:06 AM
 */
public class CanvasBar extends ImageViewPresenter {


    private JToolBar toggleBar;

    private ButtonGroup buttonGroup = new ButtonGroup();

    private List<AbstractButton> layerButtonList = new ArrayList<AbstractButton>();

    private ButtonSelectionListener buttonSelectionListener = new ButtonSelectionListener();

    private JToggleButton emptyButton = new JToggleButton("Tabula Rasa");

    private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    private ActionCommand rotateCommand = new RotateLayersCommand();


    public CanvasBar() {
        super();


    }

    public void viewSelected(ImageView view) {
        repopulate();
    }

    public JComponent getComponent() {
        if (toggleBar == null) {
            createToggleBar();
        }

        return toggleBar;

    }

    public void allViewsDeselected() {
        toggleBar.removeAll();
    }

    protected void layerChangeNotification() {
        repopulate();
    }

    protected void layerSelected(ImageLayer layer) {
        ImageView view = getSelectedView();
        int selidx = view.getModel().indexOf(layer);

        AbstractButton button = layerButtonList.get(selidx);
        if (buttonGroup.getSelection() != button.getModel()) {
            button.setSelected(true);
        }


    }

    private JToolBar createToggleBar() {
        toggleBar = new JToolBar();
        //commandBar.setDr
        //toggleBar.setTransferHandler(transferHandler);

        //commandBar.addMouseListener(dragListener);


        if (getSelectedView() == null) {
            emptyButton.setEnabled(false);
            toggleBar.add(emptyButton);
            return toggleBar;
        }

        //layerButtonList = createButtons();
        repopulate();


        return toggleBar;

    }


    private void repopulate() {
        toggleBar.removeAll();
        toggleBar.add(rotateCommand.createButton());

        buttonGroup = new ButtonGroup();

        layerButtonList = createButtons();


        for (AbstractButton button : layerButtonList) {
            toggleBar.add(button);
            buttonGroup.add(button);


        }


        int selIdx = getSelectedView().getSelectedIndex();

        if (selIdx >= 0 && getSelectedView().getModel().getNumLayers() > 0) {
            JToggleButton button = (JToggleButton) layerButtonList.get(selIdx);
            buttonGroup.setSelected(button.getModel(), true);
        }

        toggleBar.revalidate();
        toggleBar.repaint();

    }

    private List<AbstractButton> createButtons() {
        if (getSelectedView() == null) {
            return new ArrayList<AbstractButton>();
        }


        List<AbstractButton> list = new ArrayList<AbstractButton>();


        for (int i = 0; i < getSelectedView().getModel().getNumLayers(); i++) {
            AbstractLayer layer = getSelectedView().getModel().getLayer(i);
            JToggleButton button = new JToggleButton("" + (i + 1) + ": " + layer);

            button.addItemListener(buttonSelectionListener);
            button.addMouseListener(dragListener);
            button.addMouseMotionListener(dragListener);
            button.setTransferHandler(transferHandler);

            list.add(button);
        }

        return list;

    }


    class ButtonSelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {

            AbstractButton button = (AbstractButton) e.getSource();

            int buttonIndex = layerButtonList.indexOf(button);


            if (e.getStateChange() == ItemEvent.SELECTED) {
                ImageView view = getSelectedView();

                int selIdx = view.getModel().getSelectedIndex();
                if (selIdx != buttonIndex) {
                    view.getModel().getLayerSelection().setSelectionIndex(buttonIndex);
                    System.out.println("button " + buttonIndex + " is selected");
                }


            } else {
                System.out.println("button " + buttonIndex + " is deselected");
            }


        }
    }

    class CanvasBarTransferHandler extends TransferHandler {

        private DataFlavor flavor;

        public CanvasBarTransferHandler() {
            try {
                flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=com.brainflow.core.AbstractLayer");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


        public void exportAsDrag(JComponent comp, InputEvent e, int action) {
            int index = layerButtonList.indexOf(comp);
            ImageView view = getSelectedView();
            if (index >= 0) {
                AbstractLayer layer = view.getModel().getLayer(index);
                System.out.println("trying to drag layer : " + layer);
                super.exportAsDrag(comp, e, action);
            }
        }

        public boolean canImport(TransferSupport support) {
            System.out.println("canImport");
            return true;
        }

        public boolean importData(JComponent comp, Transferable t) {
            return super.importData(comp, t);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public boolean importData(TransferSupport support) {
            System.out.println("importing data");
            System.out.println("component is :" + support.getComponent());
            System.out.println("transferable is: " + support.getTransferable());


            try {
                ImageLayer layer = (ImageLayer) support.getTransferable().getTransferData(flavor);

                int dropIndex = layerButtonList.indexOf(support.getComponent());
                int sourceIndex = getSelectedView().getModel().indexOf(layer);

                if (dropIndex != sourceIndex) {
                    getSelectedView().getModel().swapLayers(dropIndex, sourceIndex);
                }

                /*System.out.println("layer is " + layer);
                System.out.println("dragged component is " + support.getComponent());
                System.out.println("index of component is : " + layerButtonList.indexOf(support.getComponent()));
                System.out.println("drop location is " + support.getDropLocation().getDropPoint());

                */
            } catch (UnsupportedFlavorException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }


            return super.importData(support);    //To change body of overridden methods use File | Settings | File Templates.
        }

        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        protected Transferable createTransferable(JComponent c) {
            int index = layerButtonList.indexOf(c);
            ImageView view = getSelectedView();
            if (index >= 0) {
                AbstractLayer layer = view.getModel().getLayer(index);
                return new AbstractLayerTransferable(layer);
            }

            return null;


        }
    }

    class DragListener extends MouseAdapter {


        public void mouseDragged(MouseEvent e) {
            System.out.println("exporting as drag?");
            JComponent c = (JComponent) e.getSource();
            TransferHandler th = c.getTransferHandler();
            th.exportAsDrag(c, e, TransferHandler.MOVE);
        }


    }

    ;

}
