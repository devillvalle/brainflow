package com.brainflow.core;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMapDeprecated;
import com.brainflow.display.*;
import com.brainflow.utils.IRange;
import com.jgoodies.binding.beans.PropertyAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;

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

    @XStreamAlias("interpolation")
    private InterpolationMethod interpolation;

    private Visibility visible;

    public final Property<Double> opacity = ObservableProperty.create(1.0);

    @XStreamAlias("smoothing-radius")
    private SmoothingRadius smoothingRadius;


    private ThresholdRange threshold;


    public final ObservableProperty<ClipRange> clipRange = ObservableProperty.create(new ClipRange(0,0,0,0));

    @XStreamOmitField
    private SelectionInList interpolationMethod;


    public ImageLayerProperties(IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMapDeprecated(_dataRange.getMin(), _dataRange.getMax(), ColorTable.GRAYSCALE);
        init(imap, _dataRange);
    }

    public ImageLayerProperties(IRange _dataRange, ThresholdRange _thresholdRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMapDeprecated(_dataRange.getMin(), _dataRange.getMax(), ColorTable.GRAYSCALE);
        init(imap, _dataRange);

        //temporary hack replace with builder
        threshold = _thresholdRange;
    }

    public ImageLayerProperties(IndexColorModel _icm, IRange _dataRange) {
        BeanContainer.bind(this);
        IColorMap imap = new LinearColorMapDeprecated(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap, _dataRange);
    }

    //public ImageLayerProperties(IColorMap map) {
    //    init(map);
    //}


    private void init(IColorMap map, IRange dataRange) {

        colorMap.set(map);

        interpolation = new InterpolationMethod(InterpolationType.CUBIC);

        visible = new Visibility(this, true);

        smoothingRadius = new SmoothingRadius(0);

        interpolationMethod = new SelectionInList<InterpolationType>(InterpolationType.values(), new PropertyAdapter<InterpolationMethod>(interpolation, InterpolationMethod.INTERPOLATION_PROPERTY));

        threshold = new ThresholdRange(map.getMinimumValue(), map.getMinimumValue(), dataRange);

        clipRange.get().maxValue.set(dataRange.getMax());
        clipRange.get().minValue.set(dataRange.getMin());

        clipRange.get().lowClip.set(map.getLowClip());
        clipRange.get().highClip.set(map.getHighClip());


   }

    public ThresholdRange getThresholdRange() {
        return threshold;
    }

    public SmoothingRadius getSmoothingRadius() {
        return smoothingRadius;
    }

    public Visibility getVisible() {
        return visible;
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

    public InterpolationMethod getInterpolation() {
        return interpolation;
    }



}