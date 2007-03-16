package com.brainflow.core;



import com.brainflow.display.ImageLayerProperties;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.axis.ImageAxis;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;
import com.jgoodies.binding.list.SelectionInList;

import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.beans.PropertyChangeListener;

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

    public void addListDataListener(ListDataListener listener);

    public void removeListDataListener(ListDataListener listener);

    public void addPropertyChangeListener(PropertyChangeListener listener);

    public void removePropertyChangeListener(PropertyChangeListener listener);

    public void addLayerChangeListener(LayerChangeListener listener);

    public void removeLayerChangeListener(LayerChangeListener listener);

    public String getLayerName(int idx);

    ImageLayer getLayer(ImageLayerProperties params);

    public List<Integer> indexOf(IImageData data);

    public int indexOf(ImageLayer layer);

    public void addLayer(ImageLayer layer);

    //public void setLayer(int index, ImageLayer layer);

    public void removeLayer(int layer);

    public void removeLayer(ImageLayer layer);

    //public ListModel getListModel();

    public ImageLayerProperties getLayerParameters(int layer);

    public ImageLayer getImageLayer(int layer);

    public int getNumLayers();

    public IImageSpace getImageSpace();

    public double getSpacing(Axis axis);

    public double getSpacing(AnatomicalAxis axis);

    public ImageAxis getImageAxis(Axis axis);

    public ImageAxis getImageAxis(AnatomicalAxis axis);


}
