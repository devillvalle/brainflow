package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.DoubleSliderForm;
import com.brainflow.colormap.AbstractColorBar;
import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.IColorMap;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.colormap.forms.SimpleColorEditor;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel;
import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 11, 2007
 * Time: 7:21:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColorMapTweaker {

    private IColorMap colorMap;
    private JPanel mainPanel;
    private JPanel controlPanel;

    private SimpleColorEditor colorEditor1 = new SimpleColorEditor();
    private SimpleColorEditor colorEditor2 = new SimpleColorEditor();
    private AbstractColorBar colorBar;

    public ColorMapTweaker(IColorMap _colorMap) {
        colorMap = _colorMap;
        colorBar = colorMap.createColorBar();
        colorBar.setOrientation(SwingConstants.VERTICAL);

        buildGUI();
    }

    private void buildGUI() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        colorBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5),
                BorderFactory.createEtchedBorder()));


        mainPanel.add(colorBar, BorderLayout.WEST);

        controlPanel = new JPanel();
        BoxLayout layout = new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS);
        controlPanel.setLayout(layout);

        JPanel topPanel = buildTopPanel();
        topPanel.setBorder(BorderFactory.createTitledBorder("Color Map"));
        controlPanel.add(topPanel);

        colorEditor1.setBorder(BorderFactory.createTitledBorder("First Selected Color"));
        controlPanel.add(colorEditor1);

        colorEditor2.setBorder(BorderFactory.createTitledBorder("Second Selected Color"));
        controlPanel.add(colorEditor2);

        JPanel c = buildRangePanel2();
        c.setBorder(BorderFactory.createTitledBorder("Interval Range"));
        controlPanel.add(c);

        mainPanel.add(controlPanel, BorderLayout.CENTER);
    }

    private JPanel buildTopPanel() {
        FormLayout layout = new FormLayout("6dlu, l:p, 3dlu, p, 8dlu, l:p, 3dlu, p, 3dlu", "6dlu, p, 10dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu");
        CellConstraints cc = new CellConstraints();
        JPanel panel = new JPanel();
        panel.setLayout(layout);

        JSpinner mapSizeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JSpinner indexSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        panel.add(new JLabel("Index One: "), cc.xy(2, 2));
        panel.add(new JLabel("Index Two: "), cc.xy(6, 2));
        panel.add(mapSizeSpinner, cc.xy(4, 2));
        panel.add(indexSpinner, cc.xy(8, 2));

        JButton equalizeButton = new JButton("Equalize Bins");
        JToggleButton discretizeButton = new JToggleButton("Discretize Map");
        JButton chooseMapButton = new JButton("Choose Map");
        JButton applyGradientButton = new JButton("Apply Gradient");
        JButton fillFirstButton = new JButton("Fill First");
        JButton fillSecondButton = new JButton("Fill Second");
        JButton showTableButton = new JButton("Show Table");
        JButton showChartButton = new JButton("Show Chart");

        panel.add(equalizeButton, cc.xywh(2, 4, 3, 1));
        panel.add(discretizeButton, cc.xywh(6, 4, 3, 1));
        panel.add(chooseMapButton, cc.xywh(2, 6, 3, 1));
        panel.add(applyGradientButton, cc.xywh(6, 6, 3, 1));
        panel.add(fillFirstButton, cc.xywh(2, 8, 3, 1));
        panel.add(fillSecondButton, cc.xywh(6, 8, 3, 1));
        panel.add(showTableButton, cc.xywh(2, 10, 3, 1));
        panel.add(showChartButton, cc.xywh(6, 10, 3, 1));

        return panel;


    }

    private JPanel buildRangePanel2() {
        DoubleSliderForm form = new DoubleSliderForm();
        form.getSliderLabel1().setText("Min:");
        form.getSliderLabel2().setText("Max:");

        return form;


    }

    private JPanel buildRangePanel() {
        FormLayout layout = new FormLayout("6dlu, l:p, 3dlu, p, 10dlu, l:p, 3dlu, p, 3dlu", "6dlu, p, 15dlu, p, 6dlu");
        CellConstraints cc = new CellConstraints();

        JSlider indexSlider = new JSlider(0, 100, 50);
        indexSlider.setPaintTicks(true);
        indexSlider.setPaintLabels(true);
        indexSlider.setPaintTrack(true);
        indexSlider.setMajorTickSpacing(25);
        JSpinner maxSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
        JSpinner minSpinner = new JSpinner(new SpinnerNumberModel(50, 0, 100, 1));
        JPanel panel = new JPanel();
        panel.setLayout(layout);

        panel.add(new JLabel("Min:"), cc.xy(2, 2));
        panel.add(minSpinner, cc.xy(4, 2));
        panel.add(new JLabel("Max:"), cc.xy(6, 2));
        panel.add(maxSpinner, cc.xy(8, 2));
        panel.add(indexSlider, cc.xywh(2, 4, 8, 1));

        return panel;


    }

    public Component getComponent() {
        return mainPanel;
    }

    public static void main(String[] args) throws Exception {
        SyntheticaLookAndFeel lf = new SyntheticaStandardLookAndFeel();

        javax.swing.UIManager.setLookAndFeel(lf);
        JFrame jf = new JFrame();
        ColorMapTweaker tweaker = new ColorMapTweaker(new LinearColorMap(0, 255, ColorTable.SPECTRUM));
        jf.add(tweaker.getComponent());
        jf.pack();
        jf.setVisible(true);
    }
}
