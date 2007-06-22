package com.brainflow.core;

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
public class MaskItem extends Model implements IMaskItem {

    private AbstractLayer source;

    private ThresholdRange predicate;

    private boolean active = true;

    private int group = 1;

    private BinaryOperation operation;


    public MaskItem(AbstractLayer source, ThresholdRange predicate, int group) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        operation = BinaryOperation.AND;
    }

    public MaskItem(AbstractLayer source, ThresholdRange predicate, int group, BinaryOperation operation) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        this.operation = operation;
    }

    public MaskItem(AbstractLayer source, ThresholdRange predicate, int group, BinaryOperation operation, boolean active) {
        this.source = source;
        this.predicate = predicate;
        this.group = group;
        this.operation = operation;
        this.active = active;
    }


    public BinaryOperation getOperation() {
        return operation;
    }

    public void setOperation(BinaryOperation operation) {
        BinaryOperation old = getOperation();
        this.operation = operation;
        firePropertyChange(IMaskItem.BINARY_OPERATION_PROPERTY, old, getOperation());
    }

    public AbstractLayer getSource() {
        return source;
    }

    public void setSource(AbstractLayer source) {
        AbstractLayer old = getSource();
        this.source = source;
        firePropertyChange(IMaskItem.SOURCE_IMAGE_PROPERTY, old, getSource());

    }

    public ThresholdRange getPredicate() {
        return predicate;
    }

    public void setPredicate(ThresholdRange predicate) {

        ThresholdRange old = getPredicate();
        this.predicate = predicate;
        firePropertyChange(IMaskItem.THRESHOLD_PREDICATE_PROPERTY, old, getPredicate());
    }


    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        boolean old = isActive();
        this.active = active;
        firePropertyChange(IMaskItem.ACTIVE_PROPERTY, old, isActive());
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        int old = getGroup();
        this.group = group;
        firePropertyChange(IMaskItem.GROUP_PROPERTY, old, getGroup());
    }
}
