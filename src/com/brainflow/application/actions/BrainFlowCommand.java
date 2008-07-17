package com.brainflow.application.actions;

import com.brainflow.application.toplevel.Brainflow;
import com.brainflow.application.toplevel.BrainFlowClientSupport;
import com.brainflow.application.IBrainFlowClient;
import com.brainflow.core.BrainCanvas;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.image.space.IImageSpace;
import com.pietschy.command.ActionCommand;

import javax.swing.event.ListDataEvent;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 19, 2007
 * Time: 12:28:23 PM
 */
public abstract class BrainFlowCommand extends ActionCommand implements IBrainFlowClient {

    BrainFlowClientSupport clientSupport;

    protected BrainFlowCommand() {
        super();
        clientSupport = new BrainFlowClientSupport(this);
    }

    protected BrainFlowCommand(String s, boolean b) {
        super(s, b);
        clientSupport = new BrainFlowClientSupport(this);
    }

    protected BrainFlowCommand(String s) {
        super(s);
        clientSupport = new BrainFlowClientSupport(this);
    }

    public ImageView getSelectedView() {
        return clientSupport.getSelectedView();

    }

    public IBrainCanvas getSelectedCanvas() {
        return Brainflow.getInstance().getSelectedCanvas();

    }

    public void viewSelected(ImageView view) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChangeNotification() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerSelected(ImageLayer layer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerChanged(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalAdded(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layerIntervalRemoved(ListDataEvent event) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public AbstractLayer getSelectedLayer() {
        return clientSupport.getSelectedLayer();
    }

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space) {
        //To change body of implemented methods use File | Settings | File Templates.
    }




}
