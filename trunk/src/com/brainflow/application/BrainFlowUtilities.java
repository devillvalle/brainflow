package com.brainflow.application;

import com.brainflow.application.toplevel.ImageIOManager;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 9:32:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrainFlowUtilities {


    private static final String dataDir = "resources/data/";
    
    public static URL getDataURL(String fileName) {
        return ClassLoader.getSystemClassLoader().getResource(dataDir + fileName);
    }

    public static IImageDisplayModel quickModel(String headerName) {
        ImageLayer layer = quickLayer(headerName);
        IImageDisplayModel model = new ImageDisplayModel("model");
        model.addLayer(layer);
        return model;
    }

    public static IImageDataSource quickDataSource(String headerName) {
          try {
            URL url = getDataURL(headerName);
            ImageIOManager.getInstance().initialize();

            FileObject fobj = VFS.getManager().resolveFile(url.getPath());
            ImageIODescriptor descriptor = ImageIOManager.getInstance().getDescriptor(fobj);
            IImageDataSource dataSource =  descriptor.createLoadableImage(fobj);

            return dataSource;


        } catch(Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static ImageLayer quickLayer(String headerName) {
        ImageLayer layer;

        try {
            IImageDataSource dataSource = BrainFlowUtilities.quickDataSource(headerName);
            layer = new ImageLayer3D(dataSource);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return layer;


    }


}
