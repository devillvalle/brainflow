package com.brainflow.application.toplevel;

import com.brainflow.application.BrainFrame;
import com.brainflow.application.BrainflowException;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.*;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.jidesoft.docking.DefaultDockingManager;
import com.jidesoft.document.DocumentComponent;
import com.jidesoft.document.DocumentPane;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 5, 2007
 * Time: 10:28:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class BrainTest {


    BrainFrame frame;
    JDesktopPane desktop;
    DocumentPane docPane;

    ImageView view;


    public BrainTest() {
    }

    public void start() {
        ImageView view = createView();
        frame = new BrainFrame();

        desktop = new JDesktopPane();

        docPane = new DocumentPane();
        frame.getDockingManager().getWorkspace().setLayout(new BorderLayout());
        frame.getDockingManager().getWorkspace().add(docPane, "Center");
        docPane.setTabPlacement(DocumentPane.BOTTOM);
        docPane.openDocument(new DocumentComponent(new JScrollPane(desktop), "Desktop-1"));
        docPane.setActiveDocument("Desktop-1");

        frame.getDockingManager().beginLoadLayoutData();
        frame.getDockingManager().setInitSplitPriority(DefaultDockingManager.SPLIT_EAST_WEST_SOUTH_NORTH);
        frame.getDockingManager().loadLayoutData();

        frame.toFront();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 50);
        frame.setVisible(true);


        JInternalFrame f1 = new JInternalFrame("frame 1", true, true, false);
        f1.setContentPane(view);
        f1.setLocation(10, 10);
        f1.setVisible(true);
        f1.setMaximizable(true);
        f1.setIconifiable(true);

        f1.pack();
        desktop.add(f1);

        //frame.pack();
        frame.setVisible(true);


    }

    public ImageView createView() {
        try {
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            IImageData data1 = AnalyzeIO.readAnalyzeImage(url);

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_white.hdr");
            IImageData data2 = AnalyzeIO.readAnalyzeImage(url);


            IImageDisplayModel model = new ImageDisplayModel("model");
            ImageLayer layer = new ImageLayer3D(new MemoryImage(data1),
                    new ImageLayerProperties(new LinearColorMap(data1.getMinValue(), data1.getMaxValue(), ColorTable.GRAYSCALE)));
            model.addLayer(layer);

            layer = new ImageLayer3D(new MemoryImage(data2),
                    new ImageLayerProperties(new LinearColorMap(data2.getMinValue(), data2.getMaxValue(), ColorTable.GRAYSCALE)));


            model.addLayer(layer);
            view = new SimpleImageView(model);
            return view;

        } catch (BrainflowException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    public static void main(String[] args) {
        com.jidesoft.utils.Lm.verifyLicense("UIN", "BrainFlow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");

        try {


            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.XERTO_STYLE);
            BrainTest test = new BrainTest();
            test.start();


        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Could not load Look and Feel, aborting");
            e.printStackTrace();
            System.exit(-1);
        }

    }
}
