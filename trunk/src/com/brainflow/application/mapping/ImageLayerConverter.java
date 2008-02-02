package com.brainflow.application.mapping;

import com.brainflow.image.io.IImageDataSource;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageLayer3D;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 11, 2007
 * Time: 7:25:26 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageLayerConverter implements Converter {

    
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        ImageLayer layer = (ImageLayer)o;
        //layer.getData().getImageInfo().
        //writer.startNode("header");
        IImageDataSource dataSource = layer.getDataSource();
        writer.startNode("dataSource");
        context.convertAnother(dataSource);
        writer.endNode();


       
    }

    @Testable
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        reader.moveDown();
        IImageDataSource dataSource = (IImageDataSource)context.convertAnother(new Object(), IImageDataSource.class);
        reader.moveUp();

        ImageLayer layer = new ImageLayer3D(dataSource);
        return layer;


        

    }

    @Testable
    public boolean canConvert(Class aClass) {
        if (ImageLayer.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
