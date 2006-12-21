package com.brainflow.core;

import com.brainflow.display.DisplayParameter;

import javax.swing.event.ChangeEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 8:00:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayChangeEvent extends ChangeEvent {


    private DisplayAction displayAction;
    private DisplayParameter parameter;


    public DisplayChangeEvent(DisplayParameter _parameter, DisplayAction _displayAction) {
        super(_parameter);
        displayAction = _displayAction;
        parameter = _parameter;


    }

    public DisplayAction getDisplayAction() {
        return displayAction;
    }

    public DisplayParameter getDisplayParameter() {
        return parameter;
    }


}
