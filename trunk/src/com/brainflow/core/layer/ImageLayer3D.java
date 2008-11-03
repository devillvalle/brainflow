package com.brainflow.core.layer;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.application.MemoryImageDataSource;
import com.brainflow.core.rendering.BasicImageSliceRenderer;
import com.brainflow.core.layer.ImageLayerProperties;
import com.brainflow.core.SliceRenderer;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.*;
import com.brainflow.image.space.IImageSpace;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.image.space.IImageSpace3D;
import com.brainflow.image.space.ICoordinateSpace3D;
import com.brainflow.image.interpolation.NearestNeighborInterpolator;

import java.util.Map;
import java.util.HashMap;

import net.java.dev.properties.Property;
import net.java.dev.properties.container.ObservableProperty;
import net.java.dev.properties.container.BeanContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:05:46 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayer3D extends ImageLayer<IImageSpace3D> {


    public final Property<MaskProperty3D> maskProperty = ObservableProperty.create();


    public ImageLayer3D(ImageLayer3D layer) {
        super(layer.getDataSource(), new ImageLayerProperties(layer.getImageLayerProperties()));
        init();
    }

    public ImageLayer3D(IImageData data) {
        super(new MemoryImageDataSource(data));
        init();
    }

    public ImageLayer3D(IImageData data, ImageLayerProperties _params) {
        super(new MemoryImageDataSource(data), _params);
        init();
    }


    public ImageLayer3D(IImageDataSource dataSource) {
        super(dataSource);
        init();
    }

    public ImageLayer3D(IImageDataSource dataSource, ImageLayerProperties _params) {
        super(dataSource, _params);
        init();
    }

    private void init() {
        BeanContainer.bind(this);
        maskProperty.set(new MaskProperty3D(this));

    }



    public IImageData3D getData() {
        return (IImageData3D) super.getData();
    }

    public double getValue(AnatomicalPoint3D pt) {
        IImageSpace space = getCoordinateSpace();

        AnatomicalPoint3D apt = pt.convertTo((ICoordinateSpace3D) space);

        return getData().worldValue((float) apt.getX(), (float) apt.getY(), (float) apt.getZ(), new NearestNeighborInterpolator());


    }

    public MaskProperty3D getMaskProperty() {
        return maskProperty.get();
    }

    public void setMaskProperty(MaskProperty3D mask) {
        maskProperty.set(mask);
    }

    private Map<Anatomy3D, BasicImageSliceRenderer> rendererMap = new HashMap<Anatomy3D, BasicImageSliceRenderer>();


    private SliceRenderer getSliceRenderer(IImageSpace3D refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
        BasicImageSliceRenderer renderer = rendererMap.get(displayAnatomy);
        if (renderer != null) {
            renderer = new BasicImageSliceRenderer(renderer, slice, true);
            rendererMap.put(displayAnatomy, renderer);

        } else {
            renderer = new BasicImageSliceRenderer(refspace, this, slice, displayAnatomy);
            rendererMap.put(displayAnatomy, renderer);

        }

        return renderer;
    }

    public SliceRenderer getSliceRenderer(IImageSpace refspace, AnatomicalPoint3D slice, Anatomy3D displayAnatomy) {
        return getSliceRenderer((IImageSpace3D) refspace, slice, displayAnatomy);
    }


}
