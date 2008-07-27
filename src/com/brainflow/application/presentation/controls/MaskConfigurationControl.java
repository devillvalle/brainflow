package com.brainflow.application.presentation.forms;

import com.brainflow.core.*;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.BrainIO;
import com.brainflow.utils.Range;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.dialog.ButtonPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Sep 1, 2007
 * Time: 9:23:01 AM
 */
public class MaskConfigurationControl extends JPanel implements ImageViewClient {

    private ImageViewClientSupport modelSupport;

    private JTabbedPane maskTabbedPanel;

    private JButton addMaskButton = new JButton("Add Mask");

    private JButton removeMaskButton = new JButton("Remove Mask");

    private MaskLayerPanel maskViewPanel;

    private MaskListListener maskListener = null;



 //   "#7FC97F" "#BEAED4" "#FDC086" "#FFFF99" "#386CB0" "#F0027F" "#BF5B17"
 //  [8] "#666666"


    private static final List<Color> MASK_FILL_COLORS = Arrays.asList(new Color(0x7FC97F), new Color(0xBEAED4), new Color(0xFDC086),
                                                      new Color(0xFFFF99), new Color(0x386CB0), new Color(0xF0027F),
                                                      new Color(0xBF5B17), new Color(0x666666));



    public MaskConfigurationControl(ImageView view) {
        modelSupport = new ImageViewClientSupport(view, this);

        buildGUI();

        maskListener = new MaskListListener();
        modelSupport.getModel().getSelectedLayer().getMaskList().addListDataListener(maskListener);
        //init();

    }

    private void buildGUI() {

        maskTabbedPanel = new JTabbedPane();

        JPanel topPanel = new SelectionPanel();
        //topPanel.setBorder(new JideTitledBorder(new PartialEtchedBorder(PartialEtchedBorder.LOWERED, PartialSide.NORTH), "Top Panel"));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.add(topPanel, BorderLayout.NORTH);
        leftPanel.add(maskTabbedPanel, BorderLayout.CENTER);


        leftPanel.add(buildButtonPanel(), BorderLayout.SOUTH);


        populateMaskConfigurationPanel();

        setLayout(new BorderLayout());
        add(leftPanel, BorderLayout.WEST);
        add(buildMaskLayerPanel(), BorderLayout.CENTER);

        setMinimumSize(new Dimension(800, 300));

        maskTabbedPanel.getModel().addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                IMaskItem item = getSelectedMaskItem();
                maskViewPanel.setMaskItem(item);
            }
        });
    }


    private ButtonPanel buildButtonPanel() {

        ButtonPanel buttonPanel = new ButtonPanel();
        buttonPanel.addButton(addMaskButton);
        buttonPanel.addButton(removeMaskButton);


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
        buttonPanel.setAlignment(SwingConstants.LEFT);

        addMaskButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
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

        return buttonPanel;

    }

    private void populateMaskConfigurationPanel() {
        ImageLayer layer = getModel().getSelectedLayer();
        final IMaskList maskList = layer.getMaskList();
        Iterator<? extends IMaskItem> iter = maskList.iterator();

        maskTabbedPanel.removeAll();

        int i = 0;
        while (iter.hasNext()) {
            MaskConfigurationPanel panel = new MaskConfigurationPanel(getModel(), iter.next());
            addMaskTab(panel, i);
            i++;
        }


    }


    private IMaskItem getSelectedMaskItem() {
        MaskConfigurationPanel panel = (MaskConfigurationPanel) maskTabbedPanel.getSelectedComponent();
        return panel.getMaskItem();

    }

    private IImageDisplayModel getModel() {
        return modelSupport.getModel();
    }


    private void addMaskTab(MaskConfigurationPanel panel, int i) {
        maskTabbedPanel.addTab("Mask " + (i + 1), /*ColorTable.createImageIcon(MASK_FILL_COLORS[i], 20,14),*/ panel);
        maskTabbedPanel.setBackgroundAt(i, MASK_FILL_COLORS.get(i));
    }

    private MaskLayerPanel buildMaskLayerPanel() {
        IImageDisplayModel viewModel = new ImageDisplayModel("mask view");
        ImageLayer layer = modelSupport.getSelectedLayer();
        viewModel.addLayer(layer);
        maskViewPanel = new MaskLayerPanel(getSelectedMaskItem());

        return maskViewPanel;

    }


    public void layerContentsChanged(ListDataEvent event) {
        populateMaskConfigurationPanel();
    }

    public void selectedLayerChanged(ImageLayer layer) {

        maskViewPanel.setMaskItem(layer.getMaskList().getMaskItem(0));
        populateMaskConfigurationPanel();
    }

    public void modelChanged(IImageDisplayModel model) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    class MaskListListener implements ListDataListener {


        public void contentsChanged(ListDataEvent e) {
            System.out.println("mask list content changed");
        }

        public void intervalAdded(ListDataEvent e) {
            System.out.println("mask interval added");
            int i = e.getIndex0();
            IMaskList maskList = (IMaskList) e.getSource();

            IMaskItem item = maskList.getMaskItem(i);
            MaskConfigurationPanel panel = new MaskConfigurationPanel(getModel(), item);
            addMaskTab(panel, i);
        }

        public void intervalRemoved(ListDataEvent e) {
            System.out.println("mask list interval removed");
        }

    }

    class SelectionPanel extends JPanel {
        private JList selectedLayerList;

        //private JButton addMaskButton = new JButton("Add Mask");
        //private JButton removeMaskButton = new JButton("Remove Mask");


        private FormLayout layout;

        public SelectionPanel() {
            layout = new FormLayout("8dlu, p, 8dlu, l:max(25dlu;p):g, 1dlu, 8dlu", "8dlu, p, 8dlu, p, 8dlu, p, 8dlu");
            CellConstraints cc = new CellConstraints();

            selectedLayerList = BasicComponentFactory.createList(new SelectionInList<ImageLayer>(modelSupport.getModel().getListModel().get()));
             setLayout(layout);

            add(new JLabel("Selected Layer: "), cc.xy(2, 2));
            add(new JScrollPane(selectedLayerList), cc.xyw(2, 4, 3));




        }
    }

    public static void main(String[] args) {

        try {


            UIManager.setLookAndFeel(new org.jvnet.substance.skin.SubstanceSaharaLookAndFeel());

            JFrame frame = new JFrame();
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            IImageData data = BrainIO.readAnalyzeImage(url);

            ImageLayer ilayer = new ImageLayer3D(data, new ImageLayerProperties(new Range(data.getMinValue(), data.getMaxValue())));

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_white.hdr");
            data = BrainIO.readAnalyzeImage(url);
            ImageLayer ilayer2 = new ImageLayer3D(data, new ImageLayerProperties(new Range(data.getMinValue(), data.getMaxValue())));


            IImageDisplayModel model = new ImageDisplayModel("model");
            model.addLayer(ilayer);
            model.addLayer(ilayer2);

            MaskConfigurationControl mpanel = new MaskConfigurationControl(new ImageView(model));
            frame.add(mpanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
