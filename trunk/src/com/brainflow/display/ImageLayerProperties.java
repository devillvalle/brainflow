package com.brainflow.display;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.utils.Range;
import com.jgoodies.binding.list.SelectionInList;

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

public class ImageLayerProperties implements Serializable {

    public static final String THRESHOLD_PROPERTY = "threshold";

    public static final String RESAMPLE_PROPERTY = "resampleInterpolation";

    public static final String COLOR_MAP_PROPERTY = "colorMap";

    public static final String VISIBLE_PROPERTY  = "visible";

    public static final String IMAGEOP_PROPERTY  = "imageOpList";

    public static final String OPACITY_PROPERTY = "opacity";

    
    private Property<IColorMap> colorMap;

    private Property<InterpolationMethod> resampleInterpolation;

    private Property<Visibility> visible;

    private Property<ImageOpListProperty> imageOpList;

    private Property<Opacity> opacity;

    private Property<ThresholdRange> threshold;

    private SelectionInList interpolationMethod;
    

    public ImageLayerProperties() {
        IColorMap imap = new LinearColorMap(0, 255, ColorTable.GRAYSCALE);
        init(imap);
    }

    public ImageLayerProperties(IndexColorModel _icm, Range _dataRange) {
        IColorMap imap = new LinearColorMap(_dataRange.getMin(), _dataRange.getMax(), _icm);
        init(imap);

    }

    public ImageLayerProperties(IColorMap map) {
        init(map);
    }


  
    private void init(IColorMap map) {

        colorMap = new Property<IColorMap>(map);
        resampleInterpolation = new Property<InterpolationMethod>(new InterpolationMethod(InterpolationHint.CUBIC));
        visible = new Property<Visibility>(new Visibility(this, true));
        imageOpList = new Property<ImageOpListProperty>(new ImageOpListProperty());
        opacity = new Property<Opacity>(new Opacity(1f));
        interpolationMethod = new SelectionInList(InterpolationHint.values(), resampleInterpolation.getModel(InterpolationMethod.INTERPOLATION_PROPERTY));
        threshold = new Property<ThresholdRange>(new ThresholdRange());


    }

    public void addImageOp(String filterName, ImageOp op) {
        imageOpList.getProperty().addImageOp(filterName, op);
    }

    public Property<ThresholdRange> getThresholdRange() {
        return threshold;
    }


    public Property<Visibility> getVisible() {
        return visible;
    }

    public SelectionInList getInterpolationMethod() {
        return interpolationMethod;
    }


    public Property<Opacity> getOpacity() {
        return opacity;
    }

    public Property<IColorMap> getColorMap() {
        return colorMap;
    }


    public Property<InterpolationMethod> getResampleInterpolation() {
        return resampleInterpolation;
    }

    public Property<ImageOpListProperty> getImageOpList() {
        return imageOpList;
    }





}