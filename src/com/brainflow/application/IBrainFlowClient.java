package com.brainflow.application;

import com.brainflow.core.AbstractLayer;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageView;
import com.brainflow.image.space.IImageSpace;

import javax.swing.event.ListDataEvent;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Aug 13, 2007
 * Time: 10:24:49 PM
 */
public interface IBrainFlowClient {

    public void viewSelected(ImageView view);

    public void allViewsDeselected();

    public void layerChangeNotification();

    public void layerSelected(AbstractLayer layer);

    public void layerAdded(ListDataEvent event);

    public void layerRemoved(ListDataEvent event);

    public void layerChanged(ListDataEvent event);

    public void layerIntervalAdded(ListDataEvent event);

    public void layerIntervalRemoved(ListDataEvent event);

    public ImageView getSelectedView();

    public AbstractLayer getSelectedLayer();

    public void imageSpaceChanged(IImageDisplayModel model, IImageSpace space);


}
