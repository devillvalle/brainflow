package com.brainflow.core.mask;

import com.brainflow.image.operations.BinaryOperand;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Oct 14, 2008
 * Time: 6:17:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LeafVisitable {

    public void accept(LeafNode leaf, BinaryOperand op);

}
