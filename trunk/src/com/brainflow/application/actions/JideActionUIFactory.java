package com.brainflow.application.actions;

import com.jidesoft.action.CommandBar;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.JideToggleButton;
import org.bushe.swing.action.ActionList;
import org.bushe.swing.action.ActionManager;
import org.bushe.swing.action.ActionUIFactory;
import org.bushe.swing.action.Separator;

import javax.swing.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 14, 2006
 * Time: 6:13:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class JideActionUIFactory extends ActionUIFactory {


    public JideActionUIFactory(ActionManager actionManager) {
        super(actionManager);
    }


    protected CommandBar instantiateCommandBar() {
        CommandBar cbar = new CommandBar();
        return cbar;
    }

    public CommandBar createCommandBar(ActionList actions) {
        CommandBar toolBar = instantiateCommandBar();
        loadActions(actions, toolBar);
        return toolBar;
    }

    public AbstractButton createJideButton(Action action) {
        AbstractButton button = null;
        Object buttonType = action.getValue(ActionManager.BUTTON_TYPE);
        if (action.getValue(ActionManager.GROUP) != null) {
            button = new JideToggleButton(action);
        } else if (ActionManager.BUTTON_TYPE_VALUE_TOGGLE.equals(buttonType)) {
            button = new JideToggleButton(action);
        } else if (ActionManager.BUTTON_TYPE_VALUE_RADIO.equals(buttonType)) {
            button = new JRadioButton(action);
        } else if (ActionManager.BUTTON_TYPE_VALUE_CHECKBOX.equals(buttonType)) {
            button = instantiateJCheckBox(action);
        } else {
            button = new JideButton(action);
        }
        configureToolBarButton(button);
        return button;
    }

    /**
     * Loads a toolbar with a list of actions.
     *
     * @param actions a List of actions, add in null or a Separator
     *                if you want separators
     * @param toolBar the JToolBar to set the actions on
     */
    public void loadActions(ActionList actions, CommandBar toolBar) {
        if (actions == null) {
            return;
        }
        HashMap buttonGroupsByGroupID = new HashMap();
        Iterator iter = actions.iterator();
        while (iter.hasNext()) {
            Object elem = iter.next();
            if (elem == null || elem instanceof JToolBar.Separator) {
                if (elem instanceof Separator && !((Separator) elem).isLineVisible()) {
                    toolBar.add(instantiateSeparator());
                } else {
                    toolBar.add(instantiateInvisibleSeparator());
                }
            } else {
                Action action = (Action) elem;
                AbstractButton button = createJideButton(action);
                toolBar.add(button);
                if (action.getValue(ActionManager.GROUP) != null) {
                    ButtonGroup buttonGroup = (ButtonGroup) buttonGroupsByGroupID.get(action.getValue(ActionManager.GROUP));
                    if (buttonGroup == null) {
                        buttonGroup = new ButtonGroup();
                        buttonGroupsByGroupID.put(action.getValue(ActionManager.GROUP), buttonGroup);
                    }
                    buttonGroup.add(button);
                }
            }
        }

        /*if (sToolbarRequestFocusEnabled14) {
           toolBar.setRequestFocusEnabled(false);
       } */
    }


}
