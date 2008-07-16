package com.brainflow.core.layer;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap2;
import com.brainflow.display.*;
import com.brainflow.utils.IRange;
import com.brainflow.core.ClipRange;
import com.jgoodies.binding.list.SelectionInList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.java.dev.properties.Property;
import net.java.dev.properties.IndexedProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.ObservableIndexed;
import net.java.dev.properties.container.ObservableWrapper;

import java.awt.image.IndexColorModel;
import java.io.Serializable;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

@XStreamAlias("layer-properties")
public class ImageLayerProperties implements Serializable {

    public static final String THRESHOLD_PROPERTY = "threshold";

    public static final String RESAMPLE_PROPERTY = "interpolation";

    public static final String COLOR_MAP_PROPERTY = "colorMap";

    public static final String VISIBLE_PROPERTY = "visible";

    public static final String IMAGEOP_PROPERTY = "imageOpList";

    public static final String OPACITY_PROPERTY = "opacity";

    public static final String SMOOTHING_PROPERTY = "smoothingRadius";

    public static final String CLIP_RANGE_PROPERTY = "clipRange";

    public final ObservableProperty<IColorMap> colorMap = ObservableProperty.create();

    //@XStreamAlias("interpolation")
    //private InterpolationMethod interpolation;

    public final IndexedProperty<InterpolationType> interpolationSet = new ObservableIndexed<InterpolationType>(
            InterpolationType.NEAREST_NEIGHBOR, InterpolationType.LINEAR, InterpolationType.CUBIC);


    public final Property<InterpolationType> interpolationType = ObservableProperty.create(InterpolationType.LINEAR);

    public final Property<Integer> interpolationSelection = new ObservableWrapper.ReadWrite<Integer>(interpolationType) {
        public Integer get() {
            return interpolationSet.get().indexOf(interpolationType.get());
        }

        public void set(Integer integer) {       
            InterpolationType itype = interpolationSet.get(integer);
            if (itype != null) {
                interpolationType.set(itype);
            } else {
                throw new IllegalArgumentException("Illegal index " + integer + " for interpolation type");
            }
        }
    };

    public final Property<Boolean> visible = new ObservableProperty<Boolean>(true) {
        public void set(Boolean aBoolean) {
           super.set(aBoolean);

        }
    };

    public final Property<Double> opacity = ObservableProperty.create(1.0);

    public final Property<Double> smoothingRadius = ObservableProperty.create(0.0);

    public final ObservableProperty<ClipRange> thresholdRange = ObservableProperty.create(new ClipRange(0, 0, 0, 0));

    public final ObservableProperty<ClipRange> clipRange = ObservableProperty.create(new ClipRange(0, 0, 0, 0));



    @XStreamOmitField
    private SelectionInList interpolationMethod;


    public ImageLayerProperties(IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMap2(_dataRange.getMin(), _dataRange.getMax(), ColorTable.GRAYSCALE);
        init(imap, _dataRange);
    }

    public ImageLayerProperties(IRange _dataRange, ThresholdRange _thresholdRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMap2(_dataRange.getMin(), _dataRange.getMax(), ColorTable.GRAYSCALE);
        init(imap, _dataRange);

        //temporary hack replace with builder
        //thresholdRange.get().setHighClip();
    }

    public ImageLayerProperties(IndexColorModel _icm, IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMap2(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap, _dataRange);
    }

    //public ImageLayerProperties(IColorMap map) {
    //    init(map);
    //}


    private void init(IColorMap map, IRange dataRange) {

        colorMap.set(map);
       
        clipRange.get().maxValue.set(dataRange.getMax());
        clipRange.get().minValue.set(dataRange.getMin());

        clipRange.get().lowClip.set(map.getLowClip());
        clipRange.get().highClip.set(map.getHighClip());

        thresholdRange.get().maxValue.set(dataRange.getMax());
        thresholdRange.get().minValue.set(dataRange.getMin());


    }

    public ThresholdRange getThresholdRange() {
        return null;
    }

    public double getSmoothingRadius() {
        return smoothingRadius.get();
    }

    public boolean isVisible() {
        return visible.get();
    }

    public ClipRange getClipRange() {
        return clipRange.get();
    }

    public SelectionInList getInterpolationMethod() {
        return interpolationMethod;
    }


    public float getOpacity() {
        return opacity.get().floatValue();
    }

    public IColorMap getColorMap() {
        return colorMap.get();
    }

    public InterpolationType getInterpolation() {
        return interpolationType.get();
    }


}