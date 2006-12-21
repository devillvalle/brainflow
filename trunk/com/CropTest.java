package com;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.*;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.io.AnalyzeIO;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 25, 2006
 * Time: 12:44:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class CropTest {


    public static void main(String[] args) {
        try {

            ImageDisplayModel dset = new ImageDisplayModel("snapper");
            ILoadableImage il1 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/code/icbm/icbm452_atlas_probability_white.img"));
            LinearColorMap lmap = new LinearColorMap(0, 18000, ColorTable.GRAYSCALE);
            dset.addLayer(new ImageLayer(il1.getData(), new ImageLayerParameters(lmap)));

            SimpleImageView view1 = new SimpleImageView(dset);
            IImagePlot plot = view1.getPlots().get(0);
            plot.setXAxisRange(new AxisRange(plot.getXAxisRange().getAnatomicalAxis(), -50, 50));
            plot.setYAxisRange(new AxisRange(plot.getYAxisRange().getAnatomicalAxis(), -50, 50));


            JFrame jf = new JFrame();
            jf.add(view1);
            jf.pack();
            jf.setVisible(true);


        } catch (BrainflowException e) {
            e.printStackTrace();
        }
    }
}
