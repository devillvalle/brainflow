/*
 * DataSourceManager.java
 *
 * Created on May 29, 2003, 9:09 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.ImageProgressDialog;
import com.brainflow.application.services.LoadableImageStatusEvent;
import org.bushe.swing.event.EventBus;

import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


/**
 * @author Bradley
 */
public class DataSourceManager {

    /**
     * Creates a new instance of DataSourceManager
     */

    private LinkedHashMap<Integer, IImageDataSource> imageMap = new LinkedHashMap<Integer, IImageDataSource>();

    private Logger log = Logger.getLogger(DataSourceManager.class.getName());


    protected DataSourceManager() {
        // Exists only to thwart instantiation.
    }

    public static DataSourceManager getInstance() {
        return (DataSourceManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.toplevel.DataSourceManager");
    }


    public boolean requestRemoval(IImageDataSource limg) {
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

    public boolean isRegistered(IImageDataSource limg) {
        return imageMap.containsKey(limg.getUniqueID());
    }

    public void register(IImageDataSource limg) {
        int uid = limg.getUniqueID();
        if (imageMap.containsKey(uid)) {
            log.warning("Attempt to load image already in memory: " + limg.getHeaderFile());
            throw new IllegalArgumentException("IImageDataSource " + limg.getStem() + " with uinique ID " + uid + " is already registered.");
        }

        imageMap.put(uid, limg);
        log.fine("registering image ..." + limg.getImageInfo().getImageLabel());
        EventBus.publish(new LoadableImageStatusEvent(limg, LoadableImageStatusEvent.EventID.IMAGE_REGISTERED));
    }


    public int getNumLoadableImages() {
        return imageMap.size();
    }

    public IImageDataSource lookup(int uid) {
        return imageMap.get(uid);
    }

    public ImageProgressDialog createProgressDialog(final IImageDataSource dataSource, final ActionListener listener) {
        final ImageProgressDialog id = new ImageProgressDialog(dataSource, DisplayManager.getInstance().getSelectedCanvas().getComponent()) {

            protected void done() {
                listener.actionPerformed(new ActionEvent(dataSource, 0, "LOADED"));
                getDialog().setVisible(false);
               
            }
        };

        return id;
    }


}
