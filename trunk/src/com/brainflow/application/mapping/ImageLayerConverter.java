package com.brainflow.application.mapping;

import com.brainflow.application.IImageDataSource;
import com.brainflow.core.ImageLayer;
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
    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Testable
    public boolean canConvert(Class aClass) {
        if (ImageLayer.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
