package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.ImageInfoReader;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;
import java.lang.ref.SoftReference;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 4:47:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataSource implements IImageDataSource {

    final static Logger log = Logger.getLogger(SoftImageDataSource.class.getCanonicalName());

    private ImageIODescriptor descriptor;

    private FileObject header;

    private FileObject dataFile;

    private IImageData dataRef = null;

    private BufferedImage previewImage;


    private ImageInfo imageInfo = null;

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header, FileObject _data) {
        descriptor = _descriptor;
        //assert descriptor.getHeaderName(_data) == _header.getName().getBaseName();
        dataFile = _data;
        header = _header;
    }

    public ImageDataSource(ImageIODescriptor _descriptor, FileObject _header) {
        descriptor = _descriptor;
        header = _header;

    }


    public IImageData getData() {
        return dataRef;
    }

    public FileObject getDataFile() {
        return dataFile;
    }

    public String getFileFormat() {
        return descriptor.getFormatName();
    }

    public FileObject getHeaderFile() {
        return header;
    }

    public ImageInfo getImageInfo() {
        if (imageInfo == null) {
            try {
                ImageInfoReader reader = (ImageInfoReader) descriptor.getHeaderReader().newInstance();
                imageInfo = reader.readInfo(getHeaderFile());


            } catch (BrainflowException e) {
                //e.printStackTrace();
                throw new RuntimeException(e);
            } catch (InstantiationException e2) {
                //e2.printStackTrace();
                throw new RuntimeException(e2);
            } catch (IllegalAccessException e3) {
                //e3.printStackTrace();
                throw new RuntimeException(e3);
            }
        }

        return imageInfo;
    }

    public BufferedImage getPreview() {
        return previewImage;
    }

    public String getStem() {
        return descriptor.getStem(header.getName().getBaseName());
    }

    public int getUniqueID() {
        return hashCode();
    }

    public boolean isLoaded() {
        return dataRef == null;
    }

    public IImageData load() throws BrainflowException {
        return null;
    }

    public IImageData load(ProgressListener plistener) throws BrainflowException {
        return null;
    }

    public void releaseData() {
        dataRef = null;
    }
}
