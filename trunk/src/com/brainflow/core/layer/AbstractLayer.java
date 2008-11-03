package com.brainflow.core.layer;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.core.layer.IMaskList;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.SliceRenderer;
import com.brainflow.core.ClipRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 15, 2007
 * Time: 9:51:11 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractLayer {
    
    //private PropertyChangeSupport support = new PropertyChangeSupport(this);

    // instantiate to a NullMaskList or something to that effect.
    //private IMaskProperty maskProperty;

    private ImageLayerProperties properties;

    protected AbstractLayer(ImageLayerProperties properties) {
        this.properties = properties;
        init();
    }

    public ImageLayerProperties getImageLayerProperties() {
        return properties;
    }



    public abstract double getValue(AnatomicalPoint3D pt);

    public abstract SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy);


    public boolean isVisible() {
        return properties.isVisible();
    }

    public double getOpacity() {
        return properties.opacity.get();
    }

    public ClipRange getThreshold() {
        return properties.getThresholdRange();
    }

    public ClipRange getClipRange() {
        return properties.getClipRange();
    }

    public abstract IMaskProperty getMaskProperty();



    public abstract Object getDataSource();

    private void init() {
        // todo move to ImageLayerProperties class (or rethink entirely?)
        // this whole thing is obscene, avert your eyes
        /*properties.getColorMap().addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {

                support.firePropertyChange(new PropertyChangeEvent(AbstractLayer.this, ImageLayerProperties.COLOR_MAP_PROPERTY,
                        evt.getOldValue(), evt.getNewValue()));
            }
        });*/






    }


    public abstract ICoordinateSpace getCoordinateSpace();

    public abstract double getMinValue();

    public abstract double getMaxValue();

    public abstract String getLabel();


}
