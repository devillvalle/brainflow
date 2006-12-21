package com.brainflow.core;

import com.brainflow.colormap.IColorMap;
import com.brainflow.display.DisplayParameter;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.display.ImageOp;
import com.brainflow.display.ImageOpListProperty;
import com.brainflow.image.data.IImageData2D;
import com.brainflow.image.rendering.RenderUtils;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.IImageSpace;

import java.awt.image.RenderedImage;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Dec 17, 2006
 * Time: 6:10:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageLayer2D extends ImageLayer {


    private RenderedImage rimg;


    public ImageLayer2D(IImageData2D _data, ImageLayerParameters _params) {
        super(_data, _params);
    }


    public void clearCache() {
        rimg = null;
    }

    public RenderedImage getRenderedImage() {
        if (rimg == null) {
            rimg = createRenderedImage();
        }

        return rimg;
    }

    public RenderedImage createRenderedImage() {
        IColorMap imap = getImageLayerParameters().getColorMap().getParameter();

        IImageSpace ispace = getImageData().getImageSpace();

        byte[] rgba = imap.getInterleavedRGBAComponents(getImageData());

        rimg = RenderUtils.createRGBAImage(rgba, ispace.getDimension(Axis.X_AXIS),
                ispace.getDimension(Axis.Y_AXIS));


        DisplayParameter<ImageOpListProperty> prop = getImageLayerParameters().getImageOpList();

        Collection<ImageOp> oplist = prop.getParameter().getImageOpList();
        for (ImageOp op : oplist) {
            System.out.println("should be smoothing!");
            rimg = op.filter(rimg);
        }

        return rimg;
    }


}
