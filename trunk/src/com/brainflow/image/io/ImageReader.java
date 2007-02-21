package com.brainflow.image.io;

import com.brainflow.application.BrainflowException;
import com.brainflow.image.data.IImageData;
import com.brainflow.utils.ProgressListener;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Nov 18, 2004
 * Time: 11:07:34 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageReader {


    public IImageData readImage(ImageInfo info) throws BrainflowException;

    public IImageData readImage(ImageInfo info, ProgressListener plistener) throws BrainflowException;


}
