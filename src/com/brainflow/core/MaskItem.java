package com.brainflow.core;

import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.display.ThresholdRange;
import com.jgoodies.binding.beans.Model;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: May 9, 2007
 * Time: 10:45:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskItem<T extends AbstractLayer> extends Model {

    public static final String SOURCE_IMAGE_PROPERTY = "source";

    public static final String THRESHOLD_PREDICATE_PROPERTY = "predicate";

    public static final String BINARY_OPERATION_PROPERTY = "operation";

    public static final String GROUP_PROPERTY = "group";

    public static final String ACTIVE_PROPERTY = "active";
   
    private T source;

    private ThresholdRange predicate;

    private boolean active = true;

    private int group = 1;

    private BinaryOperation operation;


    
    public MaskItem(T source, ThresholdRange predicate, int group) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        operation = BinaryOperation.AND;
    }

    public MaskItem(T source, ThresholdRange predicate, int group, BinaryOperation operation) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        this.operation = operation;
    }

    public BinaryOperation getOperation() {
        return operation;
    }

    public void setOperation(BinaryOperation operation) {
        BinaryOperation old = getOperation();
        this.operation = operation;
        firePropertyChange(MaskItem.BINARY_OPERATION_PROPERTY, old, getOperation());
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        T old = getSource();
        this.source = source;
        firePropertyChange(MaskItem.SOURCE_IMAGE_PROPERTY, old, getSource());

    }

    public ThresholdRange getPredicate() {
        return predicate;
    }

    public void setPredicate(ThresholdRange predicate) {
        
        ThresholdRange old = getPredicate();
        this.predicate = predicate;
        firePropertyChange(MaskItem.THRESHOLD_PREDICATE_PROPERTY, old, getPredicate());
    }

  
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = isActive();
        this.active = active;
        firePropertyChange(MaskItem.ACTIVE_PROPERTY, old, isActive());
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        int old = getGroup();
        this.group = group;
        firePropertyChange(MaskItem.GROUP_PROPERTY, old, getGroup());
    }
}
