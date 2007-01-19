package com.brainflow.application.mapping;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.brainflow.application.ILoadableImage;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 4:18:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ILoadableImageConverter implements Converter {
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        ILoadableImage limg = (ILoadableImage)object;
        
        writer.startNode("header");
        context.convertAnother(limg.getHeaderFile());
        writer.endNode();

        writer.startNode("data");
        context.convertAnother(limg.getDataFile());
        writer.endNode();

        writer.startNode("data-format");
        context.convertAnother(limg.getFileFormat());
        writer.endNode();

        writer.addAttribute("refid", limg.getStem());
    }

    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    public boolean canConvert(Class aClass) {
       
        if (ILoadableImage.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
