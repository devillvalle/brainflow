package com.brainflow.application.presentation;

import com.brainflow.application.actions.ActionContext;
import com.brainflow.application.actions.LayerVisibilityAction;
import com.brainflow.application.services.ImageDisplayModelEvent;
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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

    public CanvasBar() {
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

   
    protected void layerSelected(AbstractLayer layer) {
        if (layer != getSelectedLayer()) {
            ImageView view = getSelectedView();
            int idx = view.getSelectedIndex();
            buttonGroup.setSelected(layerButtonList.get(idx).getModel(), true);
        }
    }

    private CommandBar createCanvasBar() {
        commandBar = new CommandBar();

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
}
