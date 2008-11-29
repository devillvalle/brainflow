package com.brainflow.image.io;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageReader;
import com.brainflow.utils.ProgressListener;
import com.brainflow.image.io.AbstractImageDataSource;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.application.BrainFlowException;
import org.apache.commons.vfs.FileObject;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 4:47:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataSource extends AbstractImageDataSource {

    final static Logger log = Logger.getLogger(ImageDataSource.class.getCanonicalName());


    private IImageData dataRef = null;


   
    public ImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);
    }


    public IImageData getData() {
        
       //todo check if loaded?
        return dataRef;
    }


    public boolean isLoaded() {
        return dataRef != null;
    }

    public IImageData load() throws BrainFlowException {
        try {

            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());
            if (imageInfo.getDataFile() == null) {
                //todo this is truly horrid
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new ProgressListener() {

                public void setValue(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setMinimum(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setMaximum(int val) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void setString(String message) {
                    log.info(message);
                }

                public void setIndeterminate(boolean b) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }

                public void finished() {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

            //data.setImageLabel(getStem());
            dataRef = data;

        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainFlowException(e);
        }

        return dataRef;
    }

    public IImageData load(ProgressListener plistener) throws BrainFlowException {
        try {

            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());

            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();

            IImageData data = ireader.readImage(imageInfo, plistener);
            //data.setImageLabel(getStem());
            dataRef = data;
        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainFlowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainFlowException(e);
        }

        return dataRef;

    }

    public void releaseData() {
        dataRef = null;
    }
}
