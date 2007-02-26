package com.brainflow.application.toplevel;

import com.brainflow.application.BrainflowProject;
import com.brainflow.application.ILoadableImage;
import com.brainflow.application.services.ImageDisplayModelEvent;
import com.brainflow.application.services.LoadableImageStatusEvent;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import com.brainflow.display.ImageLayerParameters;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 2, 2006
 * Time: 1:39:29 PM
 * To change this template use File | Settings | File Templates.
 */


public class ProjectManager implements EventSubscriber, BrainflowProjectListener {

    private List<BrainflowProject> projects = new ArrayList<BrainflowProject>();
    private BrainflowProject activeProject = new BrainflowProject();

    protected ProjectManager() {
        // Exists only to thwart instantiation.
        EventBus.subscribe(LoadableImageStatusEvent.class, this);
        activeProject.addListDataListener(this);
        activeProject.setName("Project-1");
    }

    public static ProjectManager getInstance() {
        return (ProjectManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.ProjectManager");
    }

    public BrainflowProject getActiveProject() {
        return activeProject;
    }


    public IImageDisplayModel addToActiveProject(ILoadableImage limg) {

        boolean registered = LoadableImageManager.getInstance().isRegistered(limg);

        if (!registered) {
            LoadableImageManager.getInstance().registerLoadableImage(limg);
        }

        //todo give sensible name
        IImageDisplayModel displayModel = new ImageDisplayModel("model #" + (activeProject.size() + 1));
        activeProject.addModel(displayModel);


        ImageLayerParameters params = new ImageLayerParameters();
        params.setColorMap(new LinearColorMap(limg.getData().getMinValue(), limg.getData().getMaxValue(),
                ResourceManager.getInstance().getDefaultColorMap()));

        ImageLayer layer = new ImageLayer3D(limg, params);
        displayModel.addLayer(layer);


        return displayModel;

    }


    public void onEvent(Object evt) {
        LoadableImageStatusEvent event = (LoadableImageStatusEvent) evt;
        switch (event.getEventID()) {
            case IMAGE_LOADED:
                break;
            case IMAGE_REGISTERED:
                break;
            case IMAGE_REMOVED:
                //todo if only one image is in model, then this is effectively removing the view.
                clearLoadableImage(event.getLoadableImage());
            case IMAGE_UNLOADED:
                break;
        }
    }

    protected void clearLoadableImage(ILoadableImage limg) {
        Iterator<IImageDisplayModel> iter = activeProject.iterator();
        while (iter.hasNext()) {
            IImageDisplayModel dmodel = iter.next();
            List<Integer> idx = dmodel.indexOf(limg.getData());

            if (idx.size() > 0) {

                List<ImageLayer> removables = new ArrayList<ImageLayer>();

                for (int i : idx) {
                    removables.add(dmodel.getImageLayer(i));
                }

                for (ImageLayer layer : removables) {
                    dmodel.removeLayer(layer);
                }
            }
        }
    }


    public void modelAdded(BrainflowProjectEvent event) {
        EventBus.publish(new ImageDisplayModelEvent(event, ImageDisplayModelEvent.TYPE.MODEL_ADDED));
        
    }

    public void modelRemoved(BrainflowProjectEvent event) {
        EventBus.publish(new ImageDisplayModelEvent(event, ImageDisplayModelEvent.TYPE.MODEL_REMOVED));
    }

    public void intervalAdded(BrainflowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.MODEL_INTERVAL_ADDED));
    }

    public void intervalRemoved(BrainflowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.MODEL_INTERVAL_REMOVED));
    }

    public void contentsChanged(BrainflowProjectEvent e) {
        EventBus.publish(new ImageDisplayModelEvent(e, ImageDisplayModelEvent.TYPE.MODEL_CHANGED));
    }
}
