package com.brainflow.application.managers;


import com.brainflow.application.ImageIODescriptor;
import com.brainflow.application.SoftLoadableImage;
import com.brainflow.core.BrainflowException;
import org.apache.commons.vfs.FileObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 11:27:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageIOManager {

    private static Logger log = Logger.getLogger(ImageIOManager.class.getName());

    private static java.util.List<ImageIODescriptor> descriptorList = new java.util.ArrayList<ImageIODescriptor>();

    protected ImageIOManager() {
        // Exists only to thwart instantiation.
    }


    public static ImageIOManager getInstance() {
        return (ImageIOManager) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.managers.ImageIOManager");
    }

    public ImageIODescriptor[] descriptorArray() {
        ImageIODescriptor[] desc = new ImageIODescriptor[descriptorList.size()];
        descriptorList.toArray(desc);
        return desc;
    }

    public ImageIODescriptor getDescriptor(FileObject fobj) throws BrainflowException {
        for (Iterator<ImageIODescriptor> iter = descriptorList.iterator(); iter.hasNext();) {
            ImageIODescriptor desc = iter.next();

            if (desc.isHeaderMatch(fobj)) return desc;
            if (desc.isDataMatch(fobj)) return desc;
        }

        throw new BrainflowException("Colud not find ImageIODescriptor for supplied File " + fobj);

    }

    public SoftLoadableImage[] findLoadableImages(FileObject[] fobjs) {
        assert descriptorList.size() > 0 : "ImageIODescriptors not available";
        List<SoftLoadableImage> limglist = new ArrayList<SoftLoadableImage>();

        for (Iterator<ImageIODescriptor> iter = descriptorList.iterator(); iter.hasNext();) {
            ImageIODescriptor desc = iter.next();
            SoftLoadableImage[] limg = desc.findLoadableImages(fobjs);
            Collections.addAll(limglist, limg);
        }

        SoftLoadableImage[] ret = new SoftLoadableImage[limglist.size()];
        limglist.toArray(ret);

        return ret;

    }


    public void initialize() throws BrainflowException, IOException {
        InputStream istream = getClass().getClassLoader().getResourceAsStream("resources/config/imageio-config.xml");
        try {

            SAXBuilder parser = new SAXBuilder();
            Document doc = parser.build(istream);
            Element root = doc.getRootElement();
            java.util.List l1 = root.getChildren("ImageFormat");
            log.info("Number of image formats: " + l1.size());
            for (Iterator iter = l1.iterator(); iter.hasNext();) {
                try {
                    ImageIODescriptor desc = ImageIODescriptor.loadFromXML((Element) iter.next());
                    log.info("Loading image format: " + desc);
                    descriptorList.add(desc);
                } catch (BrainflowException e) {
                    throw e;
                }
            }

            descriptorList = Collections.unmodifiableList(descriptorList);


        } catch (JDOMException e) {
            e.printStackTrace();
            throw new BrainflowException("ImageIOManager.intialize: Error reading XML configuration file imagio/config.xml", e);
        } catch (IOException e) {
            throw new BrainflowException("ImageIOManager.intialize: Error reading XML configuration file imagio/config.xml", e);

        }

    }


}
