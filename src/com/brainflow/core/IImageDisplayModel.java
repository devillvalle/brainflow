package com.brainflow.core;


import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: bradley
 * Date: Dec 16, 2004
 * Time: 10:11:55 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageDisplayModel {


    public String getName();

    public SelectionInList getSelection();

    public int getSelectedIndex();

    public void addImageDisplayModelListener(ImageDisplayModelListener listener);

    public void removeImageDisplayModelListener(ImageDisplayModelListener listener);

    public void addImageLayerListener(ImageLayerListener listener);

    public void removeImageLayerListener(ImageLayerListener listener);

    public String getLayerName(int idx);


    public List<Integer> indexOf(IImageData data);

    public int indexOf(AbstractLayer layer);

    public void addLayer(AbstractLayer layer);

    public void swapLayers(int index0, int index1);

    public void rotateLayers();

    public void removeLayer(int layer);

    public void removeLayer(AbstractLayer layer);

    public ImageLayerProperties getLayerParameters(int layer);

    public AbstractLayer getLayer(int layer);

    public int getNumLayers();

    public IImageSpace getImageSpace();

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis);


}
