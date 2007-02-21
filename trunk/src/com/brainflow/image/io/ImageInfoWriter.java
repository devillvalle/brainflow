/*
 * ImageInfoWriter.java
 *
 * Created on March 21, 2003, 1:27 PM
 */

package com.brainflow.image.io;

import com.brainflow.application.BrainflowException;

import java.io.File;

/**
 * @author Bradley
 */
public interface ImageInfoWriter {

    /**
     * Creates a new instance of ImageInfoWriter
     */
    public void writeInfo(File file, ImageInfo info) throws BrainflowException;

}
