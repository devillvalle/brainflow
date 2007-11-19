package com.brainflow.application;

import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

import java.util.HashMap;
import java.util.Set;
import java.util.logging.Logger;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 24, 2007
 * Time: 2:48:39 AM
 * To change this template use File | Settings | File Templates.
 */
public class YokeHandler {

    private static final Logger log = Logger.getLogger(YokeHandler.class.getName());

    private ImageView target;

    private HashMap<ImageView, Long> sources = new HashMap<ImageView, Long>();

    private PropertyListener crossHandler = new CrosshairListener();

    public YokeHandler(ImageView _target) {
        target = _target;
    }

    public YokeHandler(ImageView _target, PropertyListener crossHandler) {
        target = _target;
        this.crossHandler = crossHandler;
    }

    public void setTarget(ImageView _target) {
        target = _target;
    }

    public ImageView getTarget() {
        return target;
    }

    public void clearSources() {
        for (ImageView view : sources.keySet()) {
            BeanContainer.get().removeListener(view.cursorPos, crossHandler);

        }
        sources.clear();

    }

    public Set<ImageView> getSources() {
        return sources.keySet();

    }

    public void removeSource(ImageView view) {
        if (sources.containsKey(view)) {
            sources.remove(view);
            BeanContainer.get().removeListener(view.cursorPos, crossHandler);
        } else {
            log.warning("Failed removal request: YokeHandler does not contain the view " + view);
        }

    }

    public void addSource(ImageView view) {
        if (view == target) {
            throw new IllegalArgumentException("Source cannot be same ImageView as target!");
        }
        if (sources.containsKey(view)) {
            log.warning("YokeHandler already contains view argument : " + view);
        } else {
            log.fine("yoking view " + view + " to " + target);
            sources.put(view, System.currentTimeMillis());
            BeanContainer.get().addListener(view.cursorPos, crossHandler);
        }
    }


    public boolean containsSource(ImageView view) {
        return sources.containsKey((view));
    }

    public void setTargetLocation(AnatomicalPoint3D point) {
        target.cursorPos.set(point);

    }

    public AnatomicalPoint3D getTargetLocation() {
        return target.getCursorPos();
    }


    class CrosshairListener implements PropertyListener {

        public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
            AnatomicalPoint3D ap = (AnatomicalPoint3D)newValue;

            if (!ap.equals(getTargetLocation())) {
                log.info("forwarding cursor change to view : " + target);
                setTargetLocation(ap);              
            }

        }


    }

    public static void main(String[] args) {
        ImageView target = new ImageView(new ImageDisplayModel("1"));
        ImageView s1 = new ImageView(new ImageDisplayModel("s1"));
        ImageView s2 = new ImageView(new ImageDisplayModel("s2"));

        YokeHandler handler = new YokeHandler(target);
        handler.addSource(s1);
        handler.addSource(s2);

        System.out.println("does handler contain s1 ?" + handler.containsSource(s1));
        System.out.println("does handler contain target ?" + handler.containsSource(target));
        System.out.println("does handler contain s2 ?" + handler.containsSource(s2));


    }


}
