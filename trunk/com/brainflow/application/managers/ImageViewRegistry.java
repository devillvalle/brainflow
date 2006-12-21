package com.brainflow.application.managers;

import com.brainflow.core.ImageView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 1, 2006
 * Time: 11:04:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewRegistry {


    private static List<WeakReference> registeredViews = new ArrayList<WeakReference>();

    private static final String[] ids = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    protected ImageViewRegistry() {
        // Exists only to thwart instantiation.
    }

    public static ImageViewRegistry getInstance() {
        return (ImageViewRegistry) SingletonRegistry.REGISTRY.getInstance("com.brainflow.application.managers.ImageViewRegistry");
    }


    public String register(ImageView view) {
        if (registeredViews.contains(view)) {
            throw new IllegalArgumentException("Image View: " + view + " has already been registered!");
        }

        registeredViews.add(new WeakReference(view));

        if (registeredViews.size() >= ids.length) {
            return String.valueOf(registeredViews.size());
        }


        return ids[registeredViews.size() - 1];
    }


}
