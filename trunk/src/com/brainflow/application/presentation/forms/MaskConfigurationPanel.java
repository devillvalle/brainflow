package com.brainflow.application.presentation.forms;

import com.brainflow.application.presentation.BindingUtils;
import com.brainflow.core.*;
import com.brainflow.display.ThresholdRange;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.io.analyze.AnalyzeIO;
import com.brainflow.image.operations.BinaryOperation;
import com.brainflow.utils.Range;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.BoundedRangeAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.text.NumberFormat;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Sep 1, 2007
 * Time: 8:25:08 AM
 */
public class MaskConfigurationPanel extends JPanel {

    private IImageDisplayModel model;

    private IMaskItem item;

    private FormLayout layout;

    private JComboBox sourceSelection;

    private JComboBox operationSelection;

    private JComboBox groupSelection;

    private JCheckBox enabledCheckBox;

    private JSlider highSlider;

    private JFormattedTextField highField;

    private JSlider lowSlider;

    private JFormattedTextField lowField;

    private BeanAdapter thresholdAdapter;

    public MaskConfigurationPanel(IImageDisplayModel _model, IMaskItem _item) {
        model = _model;
        item = _item;
        buildGUI();

        initBinding();

    }

    public IMaskItem getMaskItem() {
        return item;
    }

    private void initBinding() {
        thresholdAdapter = new BeanAdapter(item.getPredicate(), true);

        ValueModel lowVal = thresholdAdapter.getValueModel(ThresholdRange.MIN_PROPERTY);
        ValueModel highVal = thresholdAdapter.getValueModel(ThresholdRange.MAX_PROPERTY);


        BoundedRangeAdapter lowAdapter = BindingUtils.createPercentageBasedRangeModel(lowVal,
                item.getSource().getMinValue(), item.getSource().getMaxValue(), 100);
        BoundedRangeAdapter highAdapter = BindingUtils.createPercentageBasedRangeModel(highVal,
                item.getSource().getMinValue(), item.getSource().getMaxValue(), 100);


        lowSlider.setModel(lowAdapter);
        highSlider.setModel(highAdapter);


        Bindings.bind(lowField, lowVal);
        Bindings.bind(highField, highVal);


    }


    private void buildGUI() {

        CellConstraints cc = new CellConstraints();

        layout = new FormLayout("8dlu, p, 4dlu, l:max(25dlu;p):g, 1dlu, 8dlu", "12dlu, p, 10dlu, p, 10dlu, p, 22dlu, p, 10dlu, p, 10dlu, p, 12dlu, p, 12dlu");
        layout.addGroupedColumn(4);

        setLayout(layout);

        JLabel sourceLabel = new JLabel("Source Image: ");
        sourceSelection = BasicComponentFactory.createComboBox(model.getLayerSelection());
        add(sourceLabel, cc.xy(2, 2));
        add(sourceSelection, cc.xyw(4, 2, 2));

        JLabel operationLabel = new JLabel("Operation: ");
        SelectionInList opList = new SelectionInList(new BinaryOperation[]{BinaryOperation.AND, BinaryOperation.OR});
        operationSelection = BasicComponentFactory.createComboBox(opList);
        operationSelection.setSelectedItem(item.getOperation());

        add(operationLabel, cc.xy(2, 4));
        add(operationSelection, cc.xyw(4, 4, 2));

        JLabel groupLabel = new JLabel("Group: ");
        SelectionInList groupList = new SelectionInList(new Integer[]{item.getGroup()});
        groupSelection = BasicComponentFactory.createComboBox(groupList);
        groupSelection.setSelectedItem(item.getGroup());
        add(groupLabel, cc.xy(2, 6));
        add(groupSelection, cc.xyw(4, 6, 2));

        JLabel highSliderLabel = new JLabel("High Thresh: ");
        add(highSliderLabel, cc.xy(2, 8));
        highSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        add(highSlider, cc.xyw(4, 8, 2));


        JLabel lowSliderLabel = new JLabel("Low Thresh: ");
        add(lowSliderLabel, cc.xy(2, 10));
        lowSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
        add(lowSlider, cc.xyw(4, 10, 2));

        FormLayout textLayout = new FormLayout("6dlu, p:g, 20dlu, p:g, 6dlu", "max(25dlu;p)");
        JPanel textPanel = new JPanel();

        NumberFormat format = NumberFormat.getInstance();

        format.setMaximumFractionDigits(2);
        format.setMinimumIntegerDigits(1);


        lowField = new JFormattedTextField(format);
        lowField.setEditable(false);
        highField = new JFormattedTextField(format);
        highField.setEditable(false);
        textPanel.setLayout(textLayout);
        textPanel.add(lowField, cc.xy(2, 1));
        textPanel.add(highField, cc.xy(4, 1));

        //add(new JLabel("Range: "), cc.xy(2,12));
        add(textPanel, cc.xyw(4, 12, 2));

        //PropertyAdapter adapter = new PropertyAdapter(item, IMaskItem.ACTIVE_PROPERTY);
        //enabledCheckBox = BasicComponentFactory.createCheckBox(adapter, "Mask Active ");
        //add(enabledCheckBox, cc.xyw(2, 12, 5));


    }

    public static void main(String[] args) {

        try {

            JFrame frame = new JFrame();
            URL url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_gray.hdr");
            IImageData data = AnalyzeIO.readAnalyzeImage(url);

            ImageLayer ilayer = new ImageLayer3D(data, new ImageLayerProperties(new Range(0, 256)));

            url = ClassLoader.getSystemResource("resources/data/icbm452_atlas_probability_white.hdr");
            data = AnalyzeIO.readAnalyzeImage(url);
            ImageLayer ilayer2 = new ImageLayer3D(data, new ImageLayerProperties(new Range(0, 256)));


            IImageDisplayModel model = new ImageDisplayModel("model");
            model.addLayer(ilayer);
            model.addLayer(ilayer2);

            MaskConfigurationPanel mpanel = new MaskConfigurationPanel(model, ilayer.getMaskList().getMaskItem(0));

            frame.add(mpanel, BorderLayout.CENTER);
            frame.pack();
            frame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
