package com.brainflow.core;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 10, 2007
 * Time: 3:02:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskList {

    private List<MaskItem> maskItems = new ArrayList<MaskItem>();


    public MaskList(MaskItem root) {
        maskItems.add(root);
    }
     
    public MaskItem getLastItem() {
        return maskItems.get(maskItems.size() - 1);
    }

    public MaskItem getFirstItem() {
        return maskItems.get(0);
    }

    public MaskItem getMaskItem(int index) {
        return maskItems.get(index);
    }
    
    public void addMask(MaskItem item) {
        if (item.getGroup() > (getLastItem().getGroup() + 1) ) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup() );
        }

        if (item.getGroup() < (getLastItem().getGroup()) ) {
            throw new IllegalArgumentException("Illegal Group number for MaskItem : " + item + " " + item.getGroup() );
        }

        maskItems.add(item);

    }



}
