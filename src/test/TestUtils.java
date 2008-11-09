package test;

import com.brainflow.application.toplevel.ImageIOManager;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.ImageLayer3D;
import com.brainflow.image.io.IImageDataSource;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.junit.Assert;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 9:32:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestUtils {


    private static final String dataDir = "resources/data/";
    
    public static URL getDataURL(String fileName) {
        if (fileName.startsWith(dataDir)) {
            return ClassLoader.getSystemClassLoader().getResource(fileName);
        } else {
            return ClassLoader.getSystemClassLoader().getResource(dataDir + fileName);
        }
    }

    public static IImageDisplayModel quickModel(String headerName) {
        //todo hack cast
        ImageLayer3D layer = (ImageLayer3D)quickLayer(headerName);
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
            IImageDataSource dataSource = TestUtils.quickDataSource(headerName);
            dataSource.load();
            layer = new ImageLayer3D(dataSource);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return layer;


    }

    public static void assertArrayEquals(float[] f1, float[] f2, float tol) {
        for (int i = 0; i < f1.length; i++) {
            Assert.assertEquals(f1[i], f2[i], tol);
        }
    }


}
