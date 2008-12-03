package com.brainflow.image.io;

import com.brainflow.image.data.IImageData;
import com.brainflow.utils.ProgressListener;
import com.brainflow.application.BrainFlowException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Dec 2, 2008
 * Time: 2:03:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMultiImageDataSource extends IImageDataSource {


    public int indexOf(String imageLabel);

    public IImageData load(int index, ProgressListener plistener) throws BrainFlowException;

    public IImageData load(int index) throws BrainFlowException;

    public IImageDataSource getDataSource(String label);

    public IImageDataSource getDataSource(int index);

    public int size();

    




}
