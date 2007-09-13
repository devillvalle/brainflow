package com.brainflow.application.mapping;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.AbstractFileObject;
import test.Testable;


/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 11, 2006
 * Time: 5:01:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileObjectConverter implements Converter {

    //@Testable
    public void marshal(Object object, HierarchicalStreamWriter writer, MarshallingContext context) {
        try {
            AbstractFileObject fobj = (AbstractFileObject) object;
            writer.addAttribute("uri", fobj.getName().getURI());
            writer.addAttribute("type", fobj.getType().getName());
            writer.setValue(fobj.getName().getBaseName());

            
        } catch (FileSystemException e) {
            throw new RuntimeException("error during xml serialization", e);
        }
    }

    //@Testable
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        String baseName = reader.getValue();

        String uri = reader.getAttribute("uri");
        String id = reader.getAttribute("id");

        Object ret = context.get(id);
        FileSystemManager fsManager = VFS.getManager();
        

        return baseName;

    }

    public boolean canConvert(Class aClass) {
        if (AbstractFileObject.class.isAssignableFrom(aClass)) {
            System.out.println("super class of " + aClass + " is " + aClass.getSuperclass());
            return true;
            
        }
        return false;

    }


}
