package com.brainflow.core;

import com.brainflow.display.Property;

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

    private Property parameter;

    //private ImageLayer affectedLayer;

    public DisplayChangeEvent(Property _parameter, DisplayAction _displayAction) {
        super(_parameter);
        displayAction = _displayAction;
        parameter = _parameter;
        //affectedLayer = layer;

    }

    public DisplayAction getDisplayAction() {
        return displayAction;
    }

    public Property getDisplayParameter() {
        return parameter;
    }


   // public ImageLayer getAffectedLayer() {
   //     return affectedLayer;
    //}
}
