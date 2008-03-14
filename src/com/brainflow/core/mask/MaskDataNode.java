package com.brainflow.core.mask;

import com.brainflow.image.data.IMaskedData3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 12, 2008
 * Time: 9:25:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskDataNode extends AbstractNode {


    private IMaskedData3D mask;

    public MaskDataNode(IMaskedData3D mask) {
        this.mask = mask;
    }

    public void apply(TreeWalker walker) {
        walker.caseMaskDataNode(this);
    }

    public IMaskedData3D getData() {
        return mask;
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String toString() {
        return "mask: " + mask.toString();
    }
}
