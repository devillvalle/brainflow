package com.brainflow.display;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 5:00:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOpList {

    private LinkedHashMap<String, ImageOp> filterMap = new LinkedHashMap<String, ImageOp>();


    public ImageOpList() {
    }

    public ImageOpList(ImageOpList oplist) {
        Iterator<String> iter = oplist.keyIterator();
        while (iter.hasNext()) {
            String key = iter.next();
            ImageOp op = oplist.getOperation(key);
            addOperation(key, op);
        }
    }

    public void addOperation(String name, ImageOp op) {
        filterMap.put(name, op);
    }

    public ImageOp getOperation(String name) {
        return filterMap.get(name);

    }

    public Iterator<String> keyIterator() {
        return filterMap.keySet().iterator();
    }

    public void remove(String name) {
        filterMap.remove(name);
    }

    public void removeAll() {
        filterMap.clear();
    }

    public boolean isEmpty() {
        return filterMap.isEmpty();
    }


}
