package com.brainflow.application;

import com.brainflow.image.data.IImageData;
import com.brainflow.utils.ProgressListener;
import com.brainflow.application.services.LoadableImageProgressEvent;

import javax.swing.*;
import java.util.List;

import org.bushe.swing.event.EventBus;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:44:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLoader<T extends IImageData> extends SwingWorker implements ProgressListener {


    private int min;
    private int max;

    private int value;

    private String message = "";

    private ILoadableImage loadable;

    public ImageLoader(ILoadableImage _loadable) {
        loadable = _loadable;
    }


    protected void done() {

        if (isCancelled()) {
            EventBus.publish(new LoadableImageProgressEvent(loadable,
                                0,
                                "done.", LoadableImageProgressEvent.State.CANCELLED));

        } else {
            System.out.println("publishing done event");
            EventBus.publish(new LoadableImageProgressEvent(loadable,
                                max,
                                "done.", LoadableImageProgressEvent.State.DONE));
        }


    }

    protected T doInBackground() throws Exception {
        System.out.println("publishing start event");
        EventBus.publish(new LoadableImageProgressEvent(loadable,
                        0,
                        message, LoadableImageProgressEvent.State.INITIATED));

        T ret = (T)loadable.load(this);

        return ret;

    }


    public void setValue(int val) {
        value = val;
        System.out.println("publishing value event");
        EventBus.publish(new LoadableImageProgressEvent(loadable,
                (int) ((float) (value - min) / (float) (max - min) * 100f),
                message, LoadableImageProgressEvent.State.LOADING));


    }

    public void setMinimum(int val) {
        min = val;
    }

    public void setMaximum(int val) {
        max = val;
    }

    public void setString(String _message) {
        message = _message;
        EventBus.publish(new LoadableImageProgressEvent(loadable,
                (int) ((float) (value - min) / (float) (max - min) * 100f),
                message, LoadableImageProgressEvent.State.LOADING));

    }

    public void setIndeterminate(boolean b) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void finished() {
       //
    }
}
