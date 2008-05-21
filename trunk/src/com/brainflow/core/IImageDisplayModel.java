package com.brainflow.core;


import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.core.layer.ImageLayer;
import com.brainflow.core.layer.ImageLayerListener;
import com.brainflow.core.layer.ImageLayerProperties;

import java.util.List;

import net.java.dev.properties.Property;
import net.java.dev.properties.IndexedProperty;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 16, 2004
 * Time: 10:11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageDisplayModel {


    public Property<Integer> getListSelection();

    public IndexedProperty<ImageLayer> getListModel();
    
    public String getName();

    public void setSelectedIndex(int index);

    public int getSelectedIndex();

    public ImageLayer getSelectedLayer();

    public void addImageDisplayModelListener(ImageDisplayModelListener listener);

    public void removeImageDisplayModelListener(ImageDisplayModelListener listener);

    public void addImageLayerListener(ImageLayerListener listener);

    public void removeImageLayerListener(ImageLayerListener listener);

    public String getLayerName(int idx);

    public List<Integer> indexOf(IImageData data);

    public int indexOf(ImageLayer layer);

    public void addLayer(ImageLayer layer);

    public void swapLayers(int index0, int index1);

    public void rotateLayers();

    public void removeLayer(int layer);

    public void removeLayer(ImageLayer layer);

    public ImageLayerProperties getLayerParameters(int layer);

    public ImageLayer getLayer(int layer);

    public int getNumLayers();

    public IImageSpace getImageSpace();

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis);


}
