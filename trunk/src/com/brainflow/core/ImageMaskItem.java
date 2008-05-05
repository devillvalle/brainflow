package com.brainflow.core;

import com.brainflow.display.ThresholdRange;
import com.brainflow.image.operations.Operations;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.image.data.MaskPredicate;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jun 16, 2007
 * Time: 2:56:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMaskItem extends MaskItem {

    public ImageMaskItem(ImageLayer source, MaskPredicate predicate, int group) {
        super(source, predicate, group);
    }

    public ImageMaskItem(ImageLayer source, MaskPredicate predicate, int group, BinaryOperation operation) {
        super(source, predicate, group, operation);
    }

    public ImageMaskItem(ImageLayer source, MaskPredicate predicate, int group, BinaryOperation operation, boolean active) {
        super(source, predicate, group, operation, active);
    }


}
