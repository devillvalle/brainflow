package com.brainflow.image.io;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageReader;
import com.brainflow.utils.ProgressListener;
import com.brainflow.utils.ProgressAdapter;
import com.brainflow.image.io.AbstractImageDataSource;
import com.brainflow.application.ImageIODescriptor;
import com.brainflow.application.BrainflowException;
import org.apache.commons.vfs.FileObject;

import java.lang.ref.SoftReference;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 1:32:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class SoftImageDataSource extends AbstractImageDataSource {

    final static Logger log = Logger.getLogger(SoftImageDataSource.class.getCanonicalName());


    private SoftReference<IImageData> dataRef = new SoftReference<IImageData>(null);


    public SoftImageDataSource(ImageIODescriptor _descriptor, ImageInfo _info) {
        super(_descriptor, _info);
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        super(_descriptor, _header, _data);
    }

    public SoftImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        super(_descriptor, _header);

    }

    public void releaseData() {
        dataRef.enqueue();
    }

    public boolean isLoaded() {
        if (dataRef.get() == null) {
            return false;
        }

        return true;
    }



    //duplicate code (with ImageDataSource)


    public IImageData getData() {
        if (dataRef.get() == null) {
            try {
                load();
            } catch (BrainflowException e) {
                log.severe("failed to load " + getDataFile().getName().getPath());
                throw new RuntimeException(e);
            }
        }

        return dataRef.get();
    }



    public IImageData load(ProgressListener plistener) throws BrainflowException {
        try {


            ImageInfo imageInfo = getImageInfoList().get(getImageIndex());

            if (imageInfo.getDataFile() == null) {
                //todo hack alert
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();

            IImageData data = ireader.readImage(imageInfo, plistener);
            data.setIdentifier(getUniqueID());
            data.setImageLabel(getStem());
            dataRef = new SoftReference(data);
        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        }

        return dataRef.get();

    }


    public IImageData load() throws BrainflowException {
        try {

            ImageInfo imageInfo = getImageInfo();
            
            if (imageInfo.getDataFile() == null) {
                imageInfo.setDataFile(getDataFile());
            }

            ImageReader ireader = (ImageReader) getDescriptor().getDataReader().newInstance();
            IImageData data = ireader.readImage(imageInfo, new ProgressAdapter() {
                public void setString(String message) {
                    log.info(message);
                }
            });
            data.setIdentifier(getUniqueID());
            data.setImageLabel(getStem());
            dataRef = new SoftReference(data);

        } catch (IllegalAccessException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        } catch (InstantiationException e) {
            log.warning("Error caught in BasicImageData.load()");
            throw new BrainflowException(e);
        }

        return (IImageData) dataRef.get();
    }

    public int getUniqueID() {
        return hashCode();
    }



    


}
