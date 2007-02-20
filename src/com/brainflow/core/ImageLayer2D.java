package com.brainflow.core;

import com.brainflow.colormap.IColorMap;
import com.brainflow.display.Property;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.display.ImageOp;
import com.brainflow.display.ImageOpListProperty;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;

import java.awt.image.BufferedImage;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayer2D extends ImageLayer {


    private BufferedImage rimg;


    public ImageLayer2D(IImageData2D _data, ImageLayerParameters _params) {
        super(_data, _params);
    }


    public void clearCache() {
        rimg = null;
    }

    public BufferedImage getBufferedImage() {
        if (rimg == null) {
            rimg = createBufferedImage();
        }

        return rimg;
    }

    public BufferedImage createBufferedImage() {
        IColorMap imap = getImageLayerParameters().getColorMap().getProperty();

        IImageSpace ispace = getImageData().getImageSpace();

        byte[] rgba = imap.getInterleavedRGBAComponents(getImageData());

        rimg = RenderUtils.createRGBAImage(rgba, ispace.getDimension(Axis.X_AXIS),
                ispace.getDimension(Axis.Y_AXIS));


        Property<ImageOpListProperty> prop = getImageLayerParameters().getImageOpList();

        Collection<ImageOp> oplist = prop.getProperty().getImageOpList();
        for (ImageOp op : oplist) {
            rimg = op.filter(rimg);
        }

        return rimg;
    }


}
