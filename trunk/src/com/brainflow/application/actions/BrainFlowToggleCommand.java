package com.brainflow.application.actions;

import com.brainflow.application.toplevel.BrainFlow;
import com.brainflow.application.toplevel.BrainFlowClientSupport;
import com.brainflow.application.IBrainFlowClient;
import com.brainflow.core.ImageView;
import com.brainflow.core.IBrainCanvas;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.image.space.IImageSpace;
import com.pietschy.command.toggle.ToggleCommand;

import javax.swing.event.ListDataEvent;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 13, 2007
 * Time: 10:21:50 PM
 */
public abstract class BrainFlowToggleCommand extends ToggleCommand implements IBrainFlowClient {


    BrainFlowClientSupport clientSupport;

    protected BrainFlowToggleCommand() {
        super();
        clientSupport = new BrainFlowClientSupport(this);
    }


    protected BrainFlowToggleCommand(String s) {
        super(s);
        clientSupport = new BrainFlowClientSupport(this);
    }

    public ImageView getSelectedView() {
        return clientSupport.getSelectedView();

    }

    public IBrainCanvas getSelectedCanvas() {
        return BrainFlow.getInstance().getSelectedCanvas();

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
