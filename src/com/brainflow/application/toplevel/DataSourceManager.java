/*
 * DataSourceManager.java
 *
 * Created on May 29, 2003, 9:09 AM
 */

package com.brainflow.application.toplevel;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.SoftImageDataSource;
import com.brainflow.image.io.ImageDataSource;
import com.brainflow.image.data.IImageData;
import com.brainflow.application.ImageProgressDialog;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.application.services.DataSourceStatusEvent;
import org.bushe.swing.event.EventBus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
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
            EventBus.publish(new DataSourceStatusEvent(limg, DataSourceStatusEvent.EventID.IMAGE_REMOVED));
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

        //limg.getDataFile().getFileSystem().addListener();

        imageMap.put(uid, limg);
        log.fine("registering image ..." + limg.getImageInfo().getImageLabel());
        EventBus.publish(new DataSourceStatusEvent(limg, DataSourceStatusEvent.EventID.IMAGE_REGISTERED));
    }

    public IImageDataSource createDataSource(ImageIODescriptor descriptor, ImageInfo info, boolean register) {

        IImageDataSource source = new SoftImageDataSource(descriptor, info);
        if (register) {
            register(source);
        }

        return source;
    }

    public IImageDataSource createDataSource(ImageIODescriptor descriptor, List<ImageInfo> infoList, int index, boolean register) {

        IImageDataSource source = new ImageDataSource(descriptor, infoList, index);
        if (register) {
            if (!isRegistered(source))
                register(source);
        }

        return source;
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
                try {
                    IImageData data = get();
                } catch(ExecutionException e1) {
                    throw new RuntimeException(e1);
                } catch(InterruptedException e2) {
                    throw new RuntimeException(e2);
                }


                listener.actionPerformed(new ActionEvent(dataSource, 0, "LOADED"));
                getDialog().setVisible(false);
               
            }

            
        };

        return id;
    }


}
