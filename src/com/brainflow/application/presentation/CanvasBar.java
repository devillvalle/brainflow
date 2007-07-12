package com.brainflow.application.presentation;

import com.brainflow.application.actions.ActionContext;
import com.brainflow.application.actions.LayerVisibilityAction;
import com.brainflow.application.services.ImageDisplayModelEvent;
import com.brainflow.application.toplevel.ImageCanvasManager;
import com.brainflow.application.dnd.AbstractLayerTransferable;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.brainflow.core.AbstractLayer;
import com.brainflow.display.Visibility;
import com.jgoodies.binding.adapter.Bindings;
import com.jidesoft.action.CommandBar;
import com.jidesoft.swing.JideToggleSplitButton;
import com.jidesoft.swing.SplitButtonGroup;
import com.jidesoft.swing.JideSplitButton;
import org.bushe.swing.action.ActionUIFactory;
import org.bushe.swing.action.BasicAction;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;
import java.awt.event.*;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 24, 2006
 * Time: 11:28:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class CanvasBar extends ImageViewPresenter {

    private CommandBar commandBar;

    private SplitButtonGroup buttonGroup = new SplitButtonGroup();

    private List<AbstractButton> layerButtonList = new ArrayList<AbstractButton>();

    private ButtonSelectionListener listener = new ButtonSelectionListener();

    private JideSplitButton emptyButton = new JideSplitButton("Tabula Rasa");

    private TransferHandler transferHandler = new CanvasBarTransferHandler();

    private MouseAdapter dragListener = new DragListener();

    public CanvasBar() {
        super();


        EventBus.subscribeStrongly(ImageDisplayModelEvent.class, new EventSubscriber() {

            public void onEvent(Object evt) {
                ImageDisplayModelEvent event = (ImageDisplayModelEvent) evt;
                ImageView view = getSelectedView();
                if (view == null) return;
                if (view.getModel() == event.getModel()) {
                    update();
                }
            }
        });

    }


    public void allViewsDeselected() {
        for (AbstractButton button : layerButtonList) {
            button.setEnabled(false);
        }
    }

    public void viewSelected(ImageView view) {
        update();
    }

    @Override
    protected void layerSelected(AbstractLayer layer) {

        ImageView view = getSelectedView();
        int idx = view.getSelectedIndex();

        buttonGroup.setSelected(layerButtonList.get(idx).getModel(), true);

    }

    private CommandBar createCanvasBar() {
        commandBar = new CommandBar();
        //commandBar.setDr
        commandBar.setTransferHandler(transferHandler);
        //ImageCanvasManager.getInstance().getSelectedCanvas().setTransferHandler(transferHandler);


        commandBar.addMouseListener(dragListener);

        //
        commandBar.setPaintBackground(false);
        commandBar.setOpaque(false);
        //

        layerButtonList = buttonList();

        if (layerButtonList.isEmpty()) {
            emptyButton.setEnabled(false);
            commandBar.add(emptyButton);
        }

        for (AbstractButton button : layerButtonList) {
            commandBar.add(button);
        }


        commandBar.addExpansion();
        return commandBar;

    }

    private List<AbstractButton> buttonList() {
        if (getSelectedView() == null) {
            return new ArrayList<AbstractButton>();
        }


        List<AbstractButton> list = new ArrayList<AbstractButton>();
        HashMap map = new HashMap();
        map.put(ActionContext.SELECTED_IMAGE_VIEW, getSelectedView());

        for (int i = 0; i < getSelectedView().getModel().getNumLayers(); i++) {
            AbstractLayer layer = getSelectedView().getModel().getLayer(i);
            JideToggleSplitButton button = new JideToggleSplitButton("" + (i + 1) + ": " + layer);

            button.addItemListener(listener);
            button.addMouseListener(dragListener);
            button.setTransferHandler(transferHandler);


            BasicAction visAction = new LayerVisibilityAction(layer.getImageLayerProperties());

            visAction.setContext(map);

            AbstractButton visButton = ActionUIFactory.getInstance().createButton(visAction);
            Bindings.bind((JCheckBox) visButton, layer.getImageLayerProperties().getVisible().getModel(Visibility.VISIBLE_PROPERTY));
            visButton.setText("Visible");

            button.add(visButton);
            buttonGroup.add(button);
            list.add(button);
        }

        return list;

    }


    private void update() {
        commandBar.removeAll();

        layerButtonList = buttonList();


        for (AbstractButton button : layerButtonList) {
            commandBar.add(button);

        }

        if (getSelectedView() != null) {
            int selIdx = getSelectedView().getSelectedIndex();
            buttonGroup.setSelected(layerButtonList.get(selIdx).getModel(), true);
        }


        commandBar.revalidate();
        commandBar.repaint();
    }


    public JComponent getComponent() {
        if (commandBar == null) {
            createCanvasBar();
        }

        return commandBar;
    }


    class ButtonSelectionListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {

            JideToggleSplitButton button = (JideToggleSplitButton) e.getSource();

            int buttonIndex = layerButtonList.indexOf(button);
            if (buttonGroup.isSelected(button.getModel())) {
                ImageView view = getSelectedView();
                view.setSelectedIndex(buttonIndex);

            }


        }
    }


    class CanvasBarTransferHandler extends TransferHandler {

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

        public boolean importData(TransferSupport support) {
            System.out.println("importData");
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
        public void mousePressed(MouseEvent e) {
            JComponent c = (JComponent) e.getSource();
            TransferHandler th = c.getTransferHandler();
            th.exportAsDrag(c, e, TransferHandler.MOVE);
        }


    }

    ;
}
