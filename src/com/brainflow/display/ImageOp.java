package com.brainflow.display;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 5:01:00 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ImageOp {


    public BufferedImage filter(BufferedImage input);
}
