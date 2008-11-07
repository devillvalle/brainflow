package com.brainflow.application.presentation.forms;

import com.brainflow.application.MemoryImageDataSource;
import com.brainflow.core.*;
import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.MaskedData3D;
import com.brainflow.image.io.BrainIO;
import com.brainflow.utils.Range;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 6, 2007
 * Time: 11:20:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskViewPanel extends JPanel {

    private IImageDisplayModel model;

    private ImageView view;

    private MaskedData3D mask;

    public MaskViewPanel(ImageMaskItem item) {

        IImageData3D data = (IImageData3D) item.getSource().getData();
        mask = new MaskedData3D(data, item.getPredicate());
        model = new ImageDisplayModel("mask model");


      //  MaskLayer layer = new MaskLayer(mask, new ImageLayerProperties(new Range(0, 256)));

      //  model.addLayer(layer);

        view = new ImageView(model);
        
        add(view);
    }

    public MaskedData3D getMask() {
        return mask;
    }


    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame();
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            IImageData data = BrainIO.readAnalyzeImage(url);

            ImageLayer ilayer = new ImageLayer3D(new MemoryImageDataSource(data), new ImageLayerProperties(new Range(0, 256)));

            ThresholdRange trange = new ThresholdRange(1500, 25000);
            ImageMaskItem item = new ImageMaskItem(ilayer, trange, 0);
            MaskViewPanel mpanel = new MaskViewPanel(item);


            System.out.println("cardinality: " + mpanel.getMask().cardinality());
            System.out.println("mask maximum : " + mpanel.getMask().getMaxValue());
            frame.add(mpanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}