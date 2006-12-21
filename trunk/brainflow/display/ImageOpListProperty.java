package com.brainflow.display;

import com.jgoodies.binding.beans.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 5:07:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageOpListProperty extends Model {

    private ImageOpList oplist;

    public static final String IMAGE_OP_LIST_PROPERTY = "oplist";


    public ImageOpListProperty() {
        oplist = new ImageOpList();
    }

    public void addImageOp(String filter, ImageOp op) {
        ImageOpList old = new ImageOpList(oplist);
        oplist.addOperation(filter, op);
        this.firePropertyChange(IMAGE_OP_LIST_PROPERTY, old, oplist);
    }

    public void removeImageOp(String name) {
        ImageOpList old = new ImageOpList(oplist);
        oplist.remove(name);
        this.firePropertyChange(IMAGE_OP_LIST_PROPERTY, old, oplist);
    }


    public Collection<ImageOp> getImageOpList() {
        List<ImageOp> list = new ArrayList<ImageOp>();
        Iterator<String> iter = oplist.keyIterator();

        while (iter.hasNext()) {
            list.add(oplist.getOperation(iter.next()));
        }

        return list;

    }


}
