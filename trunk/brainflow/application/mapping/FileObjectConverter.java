package com.brainflow.application.mapping;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;


/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 5:01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectConverter implements Converter {


    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            FileObject fobj = (FileObject) object;
            writer.setValue(fobj.getName().getBaseName());
            writer.addAttribute("uri", fobj.getName().getURI());
            writer.addAttribute("type", fobj.getType().getName());
        } catch (FileSystemException e) {
            throw new RuntimeException("error during xml serialization", e);
        }
    }

    public Object unmarshal(HierarchicalStreamReader hierarchicalStreamReader, UnmarshallingContext unmarshallingContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean canConvert(Class aClass) {
        if (FileObject.class.isAssignableFrom(aClass)) {
            return true;
        }

        return false;

    }
}
