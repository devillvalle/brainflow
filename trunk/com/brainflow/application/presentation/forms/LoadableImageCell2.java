package com.brainflow.application.presentation.forms;

import com.brainflow.application.ILoadableImage;
import com.brainflow.application.MemoryImage;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.SnapShooter;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.io.AnalyzeIO;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.swing.JideButton;
import com.jidesoft.swing.StyledLabel;

import javax.media.jai.JAI;
import javax.media.jai.RenderedImageAdapter;
import javax.swing.*;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.ParameterBlock;

/**
 * @author Brad Buchsbaum
 */
public class LoadableImageCell2 extends javax.swing.JPanel {


    private JLabel filenameLabel;
    private JLabel iconLabel;
    private JLabel infoLabel;

    private JideButton openButton = new JideButton("Open");
    private JideButton removeButton = new JideButton("Remove");

    private FormLayout layout;


    /**
     * Creates new form LoadableImageCell
     */
    public LoadableImageCell2() {
        initComponents();
    }


    public JLabel getFilenameLabel() {
        return filenameLabel;
    }

    public void setFilenameLabel(JLabel filenameLabel) {
        this.filenameLabel = filenameLabel;
    }

    public JLabel getIconLabel() {
        return iconLabel;
    }

    public void setIconLabel(JLabel iconLabel) {
        this.iconLabel = iconLabel;
    }

    public JLabel getInfoLabel() {
        return infoLabel;
    }

    public void setInfoLabel(JLabel infoLabel) {
        this.infoLabel = infoLabel;
    }


    private void initComponents() {

        layout = new FormLayout("3dlu, l:p, 6dlu, l:p, 3dlu:g, l:p, 3dlu",
                "3dlu, 1dlu, p, p, 1dlu, 3dlu");

        setLayout(layout);
        CellConstraints cc = new CellConstraints();


        iconLabel = new JLabel();
        filenameLabel = new JLabel();
        infoLabel = new StyledLabel("test");

        openButton.setButtonStyle(JideButton.HYPERLINK_STYLE);
        openButton.setAlwaysShowHyperlink(true);
        openButton.setForeground(Color.BLUE);
        removeButton.setButtonStyle(JideButton.HYPERLINK_STYLE);
        removeButton.setAlwaysShowHyperlink(true);

        add(iconLabel, cc.xywh(2, 1, 1, 5));
        add(filenameLabel, cc.xy(4, 3));
        add(infoLabel, cc.xy(4, 4));
        add(openButton, cc.xy(6, 3));
        add(removeButton, cc.xy(6, 4));


    }

    public static void main(String[] args) {
        ImageDisplayModel dset = null;
        try {

            com.jidesoft.utils.Lm.verifyLicense("UIN", "Brainflow", "S5XiLlHH0VReaWDo84sDmzPxpMJvjP3");


            LookAndFeelFactory.installJideExtension(LookAndFeelFactory.OFFICE2003_STYLE);


            dset = new ImageDisplayModel("snapper");

            ILoadableImage il1 = new MemoryImage(AnalyzeIO.readAnalyzeImage("c:/code/icbm/icbm452_atlas_probability_white.img"));
            LinearColorMap lmap = new LinearColorMap(0, 24000, ColorTable.GRAYSCALE);
            dset.addLayer(new ImageLayer(il1.getData(), new ImageLayerParameters(lmap)));
            AnatomicalPoint1D point = dset.getImageAxis(AnatomicalVolume.getCanonicalAxial().ZAXIS).getRange().getCenter();
            SnapShooter shooter = new SnapShooter(dset, AnatomicalVolume.getCanonicalAxial());
            RenderedImage rimg = shooter.shoot(point.getX());
            float sx = (float) 48 / (float) rimg.getWidth();
            float sy = (float) 48 / (float) rimg.getHeight();
            ParameterBlock pb = new ParameterBlock();

            pb.add(sx);
            pb.add(sy);
            pb.addSource(rimg);

            rimg = JAI.create("scale", pb);

            RenderedImageAdapter rapter = new RenderedImageAdapter(rimg);
            ImageIcon icon = new ImageIcon(rapter.getAsBufferedImage());

            LoadableImageCell2 cell = new LoadableImageCell2();
            cell.getIconLabel().setIcon(icon);
            cell.getFilenameLabel().setText("AVG152MNI.img");
            cell.getInfoLabel().setText("91 X 91 X 91  180,152 bytes");
            cell.setBackground(Color.WHITE);
            cell.setForeground(Color.WHITE);
            cell.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
            JFrame jf = new JFrame();
            jf.add(cell);
            jf.setVisible(true);
            jf.pack();


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
