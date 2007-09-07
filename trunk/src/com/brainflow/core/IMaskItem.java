package com.brainflow.core;

import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.display.ThresholdRange;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 16, 2007
 * Time: 2:52:14 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IMaskItem {

    public static final String SOURCE_IMAGE_PROPERTY = "source";

    public static final String THRESHOLD_PREDICATE_PROPERTY = "predicate";

    public static final String BINARY_OPERATION_PROPERTY = "operation";

    public static final String GROUP_PROPERTY = "group";
    
    public static final String ACTIVE_PROPERTY = "active";

    public BinaryOperation getOperation();

    public void setOperation(BinaryOperation operation);

    public AbstractLayer getSource();

    public void setSource(AbstractLayer source);

    public ThresholdRange getPredicate();

    public void setPredicate(ThresholdRange predicate);

    public boolean isActive();

    public void setActive(boolean active);

    public int getGroup();

    public void setGroup(int group);
}
