package com.brainflow.display;

import com.brainflow.image.data.BasicImageData2D;
import com.brainflow.image.space.Axis;
import com.brainflow.image.space.ImageSpace2D;

import javax.media.jai.JAI;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class DisplayableImageStack {

    private ArrayList stack = new ArrayList();
    private RenderedImage[] cachedRaw;
    private RenderedImage[] cachedResampled;

    Logger log = Logger.getLogger(DisplayableImageStack.class.getName());

    public DisplayableImageStack() {
    }

    public DisplayableImageStack(DisplayableImage[] dimages) {
        for (int i = 0; i < dimages.length; i++) {
            add(dimages[i]);
        }
    }

    //public static DisplayableImageStack

    public void add(DisplayableImage image) {
        stack.add(image);

    }

    public void remove(int index) {
        if ((index > 0) && (index < stack.size()))
            stack.remove(index);
    }

    public DisplayableImage get(int index) {
        assert!stack.isEmpty();
        return (DisplayableImage) stack.get(index);
    }


    public void add(int i, DisplayableImage image) {
        stack.add(i, image);
    }

    public Iterator iterator() {
        return stack.iterator();
    }

    public int size() {
        return stack.size();
    }

    public RenderedImage[] createResampledImages() {
        if (cachedResampled != null) {
            return cachedResampled;
        }

        RenderedImage[] raw = createRawImages();
        cachedResampled = new RenderedImage[raw.length];
        ParameterBlock pb = null;
        for (int i = 0; i < raw.length; i++) {
            BasicImageData2D data = get(i).getImageData();
            ImageSpace2D ispace = (ImageSpace2D) data.getImageSpace();
            ImageLayerParameters dprops = get(i).getDisplayProperties();


            double sx = ispace.getImageAxis(Axis.X_AXIS).getRange().getInterval() / ispace.getDimension(Axis.X_AXIS);
            double sy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getInterval() / ispace.getDimension(Axis.Y_AXIS);

            double ox = ispace.getImageAxis(Axis.X_AXIS).getRange().getMinimum();
            double oy = ispace.getImageAxis(Axis.Y_AXIS).getRange().getMinimum();


            RenderingHints hints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL,
                    Boolean.TRUE);


            pb = new ParameterBlock();
            pb.addSource(raw[i]);
            pb.add((float) sx);
            pb.add((float) sy);
            pb.add((float) ox);
            pb.add((float) oy);
            //pb.add(interp);

            cachedResampled[i] = JAI.create("scale", pb, hints);


        }

        return cachedResampled;
    }

    public RenderedImage[] createRawImages() {
        if (cachedRaw != null) return cachedRaw;
        cachedRaw = new RenderedImage[size()];

        for (int i = 0; i < size(); i++) {

            BasicImageData2D data = get(i).getImageData();
            ImageSpace2D ispace = (ImageSpace2D) data.getImageSpace();

            ImageLayerParameters dprops = get(i).getDisplayProperties();
            //IColorMap imap = dprops.getColorModel();

            //DataBuffer buffer = data.getDataBuffer();
            //byte[] rgba = imap.getInterleavedRGBAComponents(buffer);

            //cachedRaw[i] = RenderUtils.createRGBAImage(rgba, ispace.getDimension(Axis.X_AXIS),
            //       ispace.getDimension(Axis.Y_AXIS));

            //cachedRaw[i] = PlanarImage.wrapRenderedImage(cachedRaw[i]);
            // assert cachedRaw[i] != null;

        }

        return cachedRaw;
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof DisplayableImageStack)) return false;

        DisplayableImageStack dis = (DisplayableImageStack) other;
        if (dis.size() != size()) return false;
        for (int i = 0; i < size(); i++) {
            DisplayableImage tmp = get(i);
            DisplayableImage tmp2 = dis.get(i);
            if (tmp != tmp2) return false;
        }

        return true;

    }


}