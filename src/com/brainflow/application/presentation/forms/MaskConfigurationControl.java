package com.brainflow.application.presentation.forms;

import com.brainflow.core.*;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.utils.Range;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.dialog.ButtonPanel;
import org.jvnet.substance.skin.SubstanceSaharaLookAndFeel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Iterator;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Sep 1, 2007
 * Time: 9:23:01 AM
 */
public class MaskConfigurationControl extends JPanel {

    private IImageDisplayModel model;

    private JTabbedPane maskTabbedPanel;

    private JButton addMaskButton = new JButton("Add Mask");

    private JButton removeMaskButton = new JButton("Remove Mask");

    private ImageView viewPanel;


    public MaskConfigurationControl(IImageDisplayModel _model) {
        model = _model;
        buildGUI();

        addMaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int i = maskTabbedPanel.getSelectedIndex();
                System.out.println("selected index is : " + i);

                MaskConfigurationPanel panel = (MaskConfigurationPanel) maskTabbedPanel.getSelectedComponent();
                if (panel != null) {
                    IMaskItem item = panel.getMaskItem();
                    item.getSource().getMaskList().dupMask();
                }
            }
        });

        removeMaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });

        //init();


    }


    private void buildGUI() {


        JPanel topPanel = new SelectionPanel();
        //topPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Top Panel"));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(topPanel, BorderLayout.NORTH);


        ImageLayer layer = model.getLayer(model.getSelectedIndex());
        IMaskList maskList = layer.getMaskList();
        Iterator<? extends IMaskItem> iter = maskList.iterator();

        maskTabbedPanel = new JTabbedPane();

        int i = 0;

        while (iter.hasNext()) {
            JPanel panel = new MaskConfigurationPanel(model, iter.next());
            maskTabbedPanel.add("Mask " + (i + 1), panel);
            i++;

        }


        leftPanel.add(maskTabbedPanel, BorderLayout.CENTER);

        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addButton(addMaskButton);
        buttonPanel.addButton(removeMaskButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        buttonPanel.setAlignment(SwingConstants.LEFT);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        IImageDisplayModel viewModel = new ImageDisplayModel("mask view");
        viewModel.addLayer(layer);


        viewPanel = new SimpleImageView(viewModel);
        viewPanel.clearAnnotations();
        viewPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);


        JTabbedPane viewTab = new JTabbedPane();
        viewTab.addTab("View 1", viewPanel);
        add(viewTab, BorderLayout.CENTER);

        this.setMinimumSize(new Dimension(800, 300));


    }

    class SelectionPanel extends JPanel {
        private JList selectedLayerList;

        //private JButton addMaskButton = new JButton("Add Mask");
        //private JButton removeMaskButton = new JButton("Remove Mask");


        private FormLayout layout;

        public SelectionPanel() {
            layout = new FormLayout("8dlu, p, 8dlu, l:max(25dlu;p):g, 1dlu, 8dlu", "8dlu, p, 8dlu, p, 8dlu, p, 8dlu");
            CellConstraints cc = new CellConstraints();


            selectedLayerList = new JList(model.getLayerSelection().getListModel());
            setLayout(layout);

            add(new JLabel("Selected Layer: "), cc.xy(2, 2));
            add(new JScrollPane(selectedLayerList), cc.xyw(2, 4, 3));


        }
    }

    public static void main
            (String[] args) {

        try {

            UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());

            JFrame frame = new JFrame();
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            IImageData data = AnalyzeIO.readAnalyzeImage(url);

            ImageLayer ilayer = new ImageLayer3D(data, new ImageLayerProperties(new Range(data.getMinValue(), data.getMaxValue())));

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_white.hdr");
            data = AnalyzeIO.readAnalyzeImage(url);
            ImageLayer ilayer2 = new ImageLayer3D(data, new ImageLayerProperties(new Range(data.getMinValue(), data.getMaxValue())));


            IImageDisplayModel model = new ImageDisplayModel("model");
            model.addLayer(ilayer);
            model.addLayer(ilayer2);

            MaskConfigurationControl mpanel = new MaskConfigurationControl(model);
            frame.add(mpanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
