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

    public static final String COLOR_MAP_PARAMETER = "colormap";


    public static final String RESAMPLE_PROPERTY = "resampleInterpolation";
    public static final String COLORMAP_PROPERTY = "colorMap";
    public static final String VISIBLE_PROPERTY  = "visible";
    public static final String IMAGEOP_PROPERTY  = "imageOpList";

    
    private Property<IColorMap> colorMap;

    private Property<InterpolationProperty> resampleInterpolation;

    private Property<VisibleProperty> visible;

    private Property<ImageOpListProperty> imageOpList;

    private SelectionInList resampleSelection;
    

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
        resampleInterpolation = new Property<InterpolationProperty>(new InterpolationProperty(InterpolationHint.CUBIC));
        visible = new Property<VisibleProperty>(new VisibleProperty(this, true));
        imageOpList = new Property<ImageOpListProperty>(new ImageOpListProperty());
        resampleSelection = new SelectionInList(InterpolationHint.values(), resampleInterpolation.getModel(InterpolationProperty.INTERPOLATION_PROPERTY));



    }

    public void addImageOp(String filterName, ImageOp op) {
        imageOpList.getProperty().addImageOp(filterName, op);

    }


    public Property<VisibleProperty> getVisible() {
        return visible;
    }

    public SelectionInList getResampleSelection() {
        return resampleSelection;
    }


    public Property<IColorMap> getColorMap() {
        return colorMap;
    }


    public Property<InterpolationProperty> getResampleInterpolation() {
        return resampleInterpolation;
    }

    public Property<ImageOpListProperty> getImageOpList() {
        return imageOpList;
    }





}