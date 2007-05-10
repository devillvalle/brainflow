package com.brainflow.core.pipeline;

import org.apache.commons.pipeline.StageException;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;

import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.RGBAImage;
import com.brainflow.image.data.UByteImageData2D;
import com.brainflow.image.data.MaskedData2D;
import com.brainflow.image.data.MaskPredicate;
import com.brainflow.image.iterators.ImageIterator;
import com.brainflow.core.AbstractLayer;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 28, 2007
 * Time: 10:19:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThresholdImagesStage extends ImageProcessingStage {

    private List<RGBAImage> images;

    private static final Logger log = Logger.getLogger(ThresholdImagesStage.class.getName());


    public void flush() {
        images = null;
    }

    public Object filter(Object input) throws StageException {
        List<RGBAImage> rgbaImages = (List<RGBAImage>) input;

        if (images == null || images.size() != getModel().getNumLayers()) {
            images = new ArrayList<RGBAImage>();
            for (int i = 0; i < getModel().getNumLayers(); i++) {
                RGBAImage rgba = rgbaImages.get(i);
                AbstractLayer layer = getModel().getLayer(i);
                if (rgba != null && layer.isVisible()) {
                    images.add(threshold(layer, rgba));
                } else {
                    images.add(null);
                }
            }
        }

        return images;


    }

    private RGBAImage threshold(AbstractLayer layer, RGBAImage rgba) {
        ThresholdRange trange = layer.getImageLayerProperties().getThresholdRange().getProperty();


        if (Double.compare(trange.getMin(), trange.getMax()) != 0) {
            UByteImageData2D alpha = rgba.getAlpha();
            UByteImageData2D out = new UByteImageData2D(alpha.getImageSpace());
            MaskedData2D mask = new MaskedData2D(rgba.getSource(), (MaskPredicate) trange);

            ImageIterator sourceIter = alpha.iterator();
            ImageIterator maskIter = mask.iterator();

            while (sourceIter.hasNext()) {
                int index = sourceIter.index();
                double a = sourceIter.next();
                double b = maskIter.next();

                byte val = (byte) (a * b);

                out.set(index, val);
            }

            RGBAImage ret = new RGBAImage(rgba.getSource(), rgba.getRed(), rgba.getGreen(), rgba.getBlue(), out);
            return ret;

        } else {
            return rgba;
        }


    }
}
