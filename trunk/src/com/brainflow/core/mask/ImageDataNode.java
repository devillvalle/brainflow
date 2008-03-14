package com.brainflow.core.mask;

import com.brainflow.image.data.IImageData;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 7:06:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageDataNode extends AbstractNode {

    private IImageData data;

    public ImageDataNode(IImageData data) {
        this.data = data;
    }

    public IImageData getData() {
        return data;
    }

    public int depth() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void apply(TreeWalker walker) {
        walker.caseImageDataNode(this);
    }


}
