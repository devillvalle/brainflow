package com.brainflow.core.layer;

import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.space.ICoordinateSpace;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.data.CoordinateSet3D;
import com.brainflow.core.rendering.BasicCoordinateSliceRenderer;
import com.brainflow.core.layer.AbstractLayer;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.SliceRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 17, 2007
 * Time: 12:44:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateLayer extends AbstractLayer {

    private CoordinateSet3D coordinates;

    public CoordinateLayer(ImageLayerProperties properties, CoordinateSet3D coords) {
        super(properties);
        coordinates = coords;
    }

    public double getValue(AnatomicalPoint3D pt) {
        return 0;
    }

    public CoordinateSet3D getDataSource() {
        return coordinates;
    }

    public SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
       return new BasicCoordinateSliceRenderer(this, slice, displayAnatomy);
    }

    public IMaskProperty getMaskProperty() {
        //todo implement please
        throw new UnsupportedOperationException();
       
    }

    public ICoordinateSpace getCoordinateSpace() {
        return coordinates.getSpace();
    }

    public double getMinValue() {
        return coordinates.getMinValue();
    }

    public double getMaxValue() {
        return coordinates.getMaxValue();
    }

    public String getLabel() {
        return "coordinates";
    }


}
