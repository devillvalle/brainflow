package com.brainflow.application;

import com.brainflow.core.ImageView;
import com.brainflow.core.SimpleImageView;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.display.ICrosshair;
import com.brainflow.image.anatomy.AnatomicalPoint3D;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.WeakHashMap;
import java.util.logging.Logger;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2007
 * Time: 2:48:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class YokeHandler {

    private static final Logger log = Logger.getLogger(YokeHandler.class.getName());

    private WeakReference<ImageView> target;

    private WeakHashMap<ImageView, Long> sources = new WeakHashMap<ImageView, Long>();

    private CrosshairListener crossHandler = new CrosshairListener();


    public YokeHandler(ImageView _target) {
        target = new WeakReference<ImageView>(_target);
    }

    public void setTarget(ImageView _target) {
        target = new WeakReference<ImageView>(_target);
    }

    public WeakReference<ImageView> getTarget() {
        return target;
    }

    public void clearSources() {
        sources.clear();
    }

    public void removeSource(ImageView view) {
        sources.remove(view);
      
        view.getCrosshair().removePropertyChangeListener(crossHandler);
    }

    public void addSource(ImageView view) {
        if (view == target.get()) {
            throw new IllegalArgumentException("Source cannot be same ImageView as target!");
        }
        if (sources.containsKey(view)) {
            log.warning("YokeHandler already contains view argument : " + view);
        } else {
            sources.put(view, System.currentTimeMillis());
            view.getCrosshair().addPropertyChangeListener(crossHandler);
        }
    }

    public boolean isStale() {
        return target.get() == null;
    }

    public boolean containsSource(ImageView view) {
        return sources.containsKey((view));
    }


    class CrosshairListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {

            ICrosshair cross = (ICrosshair) evt.getSource();
            ImageView view = target.get();
            if (view != null) {
                view.getCrosshair().getProperty().setLocation(cross.getLocation());
            } else {
                log.fine("Target view of YokeHandler has been garbage collected");
            }
        }
    }

    public static void main(String[] args) {
        SimpleImageView target = new SimpleImageView(new ImageDisplayModel("1"));
        SimpleImageView s1 = new SimpleImageView(new ImageDisplayModel("s1"));
        SimpleImageView s2 = new SimpleImageView(new ImageDisplayModel("s2"));

        YokeHandler handler = new YokeHandler(target);
        handler.addSource(s1);
        handler.addSource(s2);

        System.out.println("does handler contain s1 ?" + handler.containsSource(s1));
        System.out.println("does handler contain target ?" + handler.containsSource(target));
        System.out.println("does handler contain s2 ?" + handler.containsSource(s2));


    }


}
