package com.brainflow.core;

import com.brainflow.image.data.IImageData3D;
import com.brainflow.display.ThresholdRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 10:45:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskItem {

    private IImageData3D source;

    private ThresholdRange predicate;

    private boolean active = true;

    private int group = 1;


    public MaskItem(IImageData3D source, ThresholdRange predicate, int group) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
    }

    public IImageData3D getSource() {
        return source;
    }

    public void setSource(IImageData3D source) {
        this.source = source;
    }

    public ThresholdRange getPredicate() {
        return predicate;
    }

    public void setPredicate(ThresholdRange predicate) {
        this.predicate = predicate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
