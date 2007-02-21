/*
 * LoadableImageManager.java
 *
 * Created on May 29, 2003, 9:09 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.services.LoadableImageStatusEvent;
import org.bushe.swing.event.EventBus;

import javax.swing.event.EventListenerList;
import java.util.LinkedHashMap;
import java.util.logging.Logger;


/**
 * @author Bradley
 */
public class LoadableImageManager {

    /**
     * Creates a new instance of LoadableImageManager
     */

    private LinkedHashMap<Integer, ILoadableImage> imageMap = new LinkedHashMap<Integer, ILoadableImage>();

    private Logger log = Logger.getLogger(LoadableImageManager.class.getName());

    private EventListenerList listeners = new EventListenerList();

    protected LoadableImageManager() {
        // Exists only to thwart instantiation.
    }

    public static LoadableImageManager getInstance() {
        return (LoadableImageManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.LoadableImageManager");
    }


    public boolean requestRemoval(ILoadableImage limg) {
        assert imageMap.containsKey(limg.getUniqueID());
        if (limg == null) {
            return false;
        }

        if (imageMap.containsKey(limg.getUniqueID())) {
            imageMap.remove(limg.getUniqueID());
            EventBus.publish(new LoadableImageStatusEvent(limg, LoadableImageStatusEvent.EventID.IMAGE_REMOVED));
        }

        return true;


    }

    public boolean isRegistered(ILoadableImage limg) {
        return imageMap.containsKey(limg.getUniqueID());
    }

    public void registerLoadableImage(ILoadableImage limg) {
        int uid = limg.getUniqueID();
        if (imageMap.containsKey(uid)) {
            log.warning("Attempt to load image already in memory: " + limg.getHeaderFile());
            throw new IllegalArgumentException("ILoadableImage " + limg.getStem() + " with uinique ID " + uid + " is already registered.");
        }

        imageMap.put(uid, limg);
        log.info("firing LoadableImageStatusEvent ...");
        EventBus.publish(new LoadableImageStatusEvent(limg, LoadableImageStatusEvent.EventID.IMAGE_REGISTERED));
    }


    public int getNumLoadableImages() {
        return imageMap.size();
    }

    public ILoadableImage lookup(int uid) {
        return imageMap.get(uid);
    }


}
