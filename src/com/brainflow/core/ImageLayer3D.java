package com.brainflow.core;

import com.brainflow.application.ILoadableImage;
import com.brainflow.display.ImageLayerProperties;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.operations.ImageSlicer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:05:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayer3D extends ImageLayer {


    private static final int MAX_ENTRIES = 50;


    private ImageSlicer slicer;

    private Map<String, ImageLayer2D> cache = new LinkedHashMap<String, ImageLayer2D>(MAX_ENTRIES, .75F, true) {
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > MAX_ENTRIES;
        }
    };


    public ImageLayer3D(ILoadableImage limg, ImageLayerProperties _params) {
        super(limg, _params);
        slicer = new ImageSlicer((IImageData3D) getImageData());
    }


    public ImageLayer2D getSlice(AnatomicalVolume displayAnatomy, AnatomicalPoint1D _displaySlice) {
        //String key = _displaySlice.getAnatomy().toString() + _displaySlice.getX();
        //ImageLayer2D layer = cache.get(key);

        //if (layer == null) {
        IImageData2D data2d = slicer.getSlice(displayAnatomy, _displaySlice);
        ImageLayer2D layer = new ImageLayer2D(data2d, getImageLayerParameters());
        cache.put(_displaySlice.getAnatomy().toString() + _displaySlice.getX(), layer);

        //} else {
        //    System.out.println("RETURNING CACHED IMAGE");

        //}

        return layer;
    }


}
