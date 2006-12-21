package com.brainflow.application.managers;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.services.LoadableImageStatusEvent;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.data.BasicImageData3D;
import org.apache.commons.lang.math.DoubleRange;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;
import org.bushe.swing.event.EventSubscriber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 2, 2006
 * Time: 1:39:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DisplayManager implements EventSubscriber {

    private List<IImageDisplayModel> models = new ArrayList<IImageDisplayModel>();


    protected DisplayManager() {
        // Exists only to thwart instantiation.
        EventBus.subscribe(LoadableImageStatusEvent.class, this);
    }

    public static DisplayManager getInstance() {
        return (DisplayManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.managers.DisplayManager");
    }

    public IImageDisplayModel createDisplayModel(ILoadableImage limg) {

        boolean registered = LoadableImageManager.getInstance().isRegistered(limg);
        if (!registered) {
            LoadableImageManager.getInstance().registerLoadableImage(limg);
        }

        IImageDisplayModel displayModel = new ImageDisplayModel("Dataset #" + (models.size() + 1));

        models.add(displayModel);

        BasicImageData3D data = (BasicImageData3D) limg.getData();
        displayModel.addLayer(new ImageLayer3D(data,
                new ImageLayerParameters(ResourceManager.getInstance().getDefaultColorMap(),
                        new DoubleRange(limg.getData().getMinValue(), limg.getData().getMaxValue()))));


        return displayModel;

    }


    public void onEvent(EventServiceEvent evt) {
        LoadableImageStatusEvent event = (LoadableImageStatusEvent) evt;
        switch (event.getEventID()) {
            case IMAGE_LOADED:
                break;
            case IMAGE_REGISTERED:
                break;
            case IMAGE_REMOVED:
                removeLoadableImage(event.getLoadableImage());
            case IMAGE_UNLOADED:
                break;
        }
    }

    protected void removeLoadableImage(ILoadableImage limg) {
        System.out.println("querying for removables...");
        for (IImageDisplayModel dmodel : models) {
            List<Integer> idx = dmodel.indexOf(limg.getData());

            System.out.println("found " + idx.size() + " removable layers");

            if (idx.size() > 0) {
                System.out.println("gathering removables");

                List<ImageLayer> removables = new ArrayList<ImageLayer>();

                for (int i : idx) {
                    removables.add(dmodel.getImageLayer(i));
                }

                System.out.println("gathered" + removables.size() + " removables");

                for (ImageLayer layer : removables) {
                    System.out.println("removing " + layer);
                    dmodel.removeLayer(layer);
                }
            }
        }
    }
}
