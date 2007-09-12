package com.brainflow.application.mapping;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.brainflow.application.IImageDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 4:18:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class IImageDataSourceConverter implements Converter {
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        Integer dataIndex = (Integer)context.get("dataIndex");
        if (dataIndex != null) {
            dataIndex = dataIndex+1;
            System.out.println("data index is " + dataIndex);
            context.put("dataIndex", dataIndex+1);
        } else {
            dataIndex=1;
            System.out.println("putting dataIndex into context");
            context.put("dataIndex", dataIndex);
        }


        
        IImageDataSource dataSource = (IImageDataSource)object;
        
        writer.startNode("header");
        context.convertAnother(dataSource.getHeaderFile());
        writer.endNode();

        writer.startNode("data");
        context.convertAnother(dataSource.getDataFile());
        writer.endNode();

        writer.startNode("data-format");
        context.convertAnother(dataSource.getFileFormat());
        writer.endNode();

        writer.addAttribute("refid", "dataSource"+dataIndex);
    }

    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;
    }

    public boolean canConvert(Class aClass) {
       
        if (IImageDataSource.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
