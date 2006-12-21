package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.RangeSliderControl;
import com.brainflow.application.services.ImageViewDataEvent;
import com.brainflow.core.ImageCanvas;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageView;
import com.jgoodies.binding.adapter.RadioButtonAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jidesoft.action.CommandBar;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideSplitButton;
import com.jidesoft.swing.JideToggleButton;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 28, 2006
 * Time: 12:56:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class CanvasBar extends ImageViewPresenter {

    private JideSplitButton viewSplitButton;
    private ImageCanvas canvas;
    private CommandBar commandBar;
    private List<AbstractButton> layerButtonList = new ArrayList<AbstractButton>();


    public CanvasBar() {


        EventBus.subscribeStrongly(ImageViewDataEvent.class, new EventSubscriber() {

            public void onEvent(EventServiceEvent evt) {
                ImageViewDataEvent event = (ImageViewDataEvent) evt;

                if (getSelectedView() == event.getImageView()) {
                    update();
                }
            }
        });

        viewSplitButton = new JideSplitButton("Views");
        populateViews();


    }

    public void setImageCanvas(ImageCanvas _canvas) {
        canvas = _canvas;
    }

    public void viewSelected(ImageView view) {
        System.out.println("view is actually null in point of fact");
        update();

    }

    public JComponent getComponent() {
        if (commandBar == null) {
            createCanvasBar();
        }

        return commandBar;
    }


    private List<Action> getActionList() {
        if (canvas == null) {
            return null;
        } else if (canvas.getViews().length == 0) {
            return null;
        } else {
            List<Action> actions = new ArrayList<Action>();
            ImageView[] views = canvas.getViews();
            for (int i = 0; i < views.length; i++) {
                actions.add(new SelectViewAction(canvas, views[i]));
            }

            actions.add(new SelectViewAction(canvas, null));

            return actions;

        }

    }


    public CommandBar createCanvasBar() {
        commandBar = new CommandBar();
        commandBar.add(viewSplitButton);

        layerButtonList = buttonList();

        for (AbstractButton button : layerButtonList) {

            commandBar.add(button);

        }

        commandBar.addExpansion();
        return commandBar;

    }

    private void update() {
        commandBar.removeAll();
        populateViews();
        commandBar.add(viewSplitButton);

        layerButtonList = buttonList();
        for (AbstractButton button : layerButtonList) {
            commandBar.add(button);

        }


        commandBar.revalidate();
        commandBar.repaint();


    }


    private void populateViews() {
        List<Action> actionList = getActionList();
        if (actionList == null) {
            viewSplitButton.setText("View List - Empty ");
            viewSplitButton.setEnabled(false);
        } else {
            viewSplitButton.removeAll();

            for (Action a : actionList) {
                viewSplitButton.add(a);
            }

            viewSplitButton.setEnabled(true);
            if (canvas.getSelectedView() == null) {
                viewSplitButton.setText("View List - No Selection ");
            } else {
                viewSplitButton.setText(canvas.getSelectedView().getName());

            }
        }

    }

    private List<AbstractButton> buttonList() {

        if (getSelectedView() == null) {
            return new ArrayList<AbstractButton>();
        } else {
            List<AbstractButton> list = new ArrayList<AbstractButton>();
            ListModel model = getSelectedView().getImageDisplayModel().getListModel();
            SelectionInList selInList = getSelectedView().getImageDisplayModel().getSelection();

            ValueModel selection = selInList.getSelectionHolder();
            for (int i = 0; i < model.getSize(); i++) {

                ImageLayer layer = getSelectedView().getImageDisplayModel().getImageLayer(i);

                JideToggleButton button = new JideToggleButton("" + (i + 1) + ": " + layer);


                RadioButtonAdapter adapter = new RadioButtonAdapter(selection, layer);
                button.setModel(adapter);

                list.add(button);
            }

            return list;

        }
    }


    class SelectViewAction extends AbstractAction {

        private ImageView view;
        private ImageCanvas canvas;

        public SelectViewAction(ImageCanvas _canvas, ImageView _view) {
            canvas = _canvas;
            view = _view;

            if (view != null)
                putValue(Action.NAME, view.getName());
            else {
                putValue(Action.NAME, "Null");

            }


        }

        public void actionPerformed(ActionEvent e) {
            //if (canvas != null && view != null) {
            //    canvas.setSelectedView(view);
            //}
        }
    }


    public static void main(String[] args) throws Exception {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "Brainflow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");
        //UIManager.setLookAndFeel(LookAndFeelFactory.PLASTICXP_LNF);


        LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
        CollapsiblePanes panes = new CollapsiblePanes();
        CollapsiblePane pane = new CollapsiblePane("Data Range");
        pane.setContentPane(new RangeSliderControl());
        pane.setEmphasized(true);
        pane.setContentPaneHeight(200);
        pane.setContentPaneWidth(300);
        //pane.setStyle(CollapsiblePane.TREE_STYLE);
        panes.add(pane);

        pane = new CollapsiblePane("Color Range");
        pane.setContentPane(new RangeSliderControl());
        pane.setEmphasized(true);
        //pane.setStyle(CollapsiblePane.TREE_STYLE);
        panes.add(pane);


        JFrame frame = new JFrame();
        frame.add(panes);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
}
