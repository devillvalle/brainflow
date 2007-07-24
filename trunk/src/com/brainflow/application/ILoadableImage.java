package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.ProgressListener;
import org.apache.commons.vfs.FileObject;

/**
 * Created by IntelliJ IDEA.
 * User: Owner
 * Date: Nov 24, 2004
 * Time: 2:49:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ILoadableImage {

    public ImageInfo getImageInfo();

    public boolean isLoaded();

    public String getStem();

    public FileObject getDataFile();

    public FileObject getHeaderFile();

    public String getFileFormat();

    public IImageData getData();

    public void releaseData();

    public IImageData load(ProgressListener plistener) throws BrainflowException;

    public IImageData load() throws BrainflowException;

    public int getUniqueID();

}
