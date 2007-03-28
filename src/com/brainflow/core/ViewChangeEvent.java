package com.brainflow.core;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 25, 2007
 * Time: 4:56:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ViewChangeEvent extends DisplayChangeEvent {


    private ImageView view;

    public ViewChangeEvent(ImageView view, DisplayChangeType changeType) {
        super(changeType);
        this.view = view;
        
    }


    public ImageView getView() {
        return view;
    }

    public IImageDisplayModel getModel() {
        return view.getModel();
    }
}
