package com.brainflow.application.presentation;

import com.brainflow.colormap.*;
import com.brainflow.display.DisplayParameter;
import com.brainflow.utils.ResourceLoader;
import com.jgoodies.binding.beans.DelayedPropertyChangeHandler;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.swing.JideBoxLayout;
import org.bushe.swing.action.BasicAction;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 13, 2006
 * Time: 5:29:18 PM
 * To change this template use File | Settings | File Templates.
 */


public class ColorMapTablePresenter extends AbstractColorMapPresenter {


    private ColorMapTable colorTable;
    private JScrollPane scrollPane;

    private ButtonPanel buttonPanel;

    private JButton applyButton;
    private JButton resetButton;

    private JSpinner segmentSpinner;

    private Box topPanel;

    private JPanel form;


    public ColorMapTablePresenter() {
        colorTable = new ColorMapTable(new LinearColorMap(0, 255, ColorTable.SPECTRUM));
        scrollPane = new JScrollPane(colorTable.getComponent());

        topPanel = Box.createHorizontalBox();
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));


        segmentSpinner = new JSpinner(new SpinnerNumberModel());

        segmentSpinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                Number n = (Number) segmentSpinner.getValue();
                colorTable.getModel().setTableSize(n.intValue());

            }
        });


        topPanel.add(new JLabel("Color Segments: "));
        topPanel.add(segmentSpinner);

        buttonPanel = new ButtonPanel();
        buttonPanel.setSizeContraint(ButtonPanel.NO_LESS_THAN);

        applyButton = new JButton("Apply");
        resetButton = new JButton("Cancel");

        buttonPanel.addButton(applyButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(resetButton, ButtonPanel.CANCEL_BUTTON);


        buttonPanel.setBorder(BorderFactory.createEmptyBorder(8, 2, 8, 2));

        form = new JPanel();
        form.setLayout(new JideBoxLayout(form, JideBoxLayout.Y_AXIS));
        form.add(createToolBar(), JideBoxLayout.FIX);
        form.add(topPanel, JideBoxLayout.FIX);
        form.add(scrollPane, JideBoxLayout.VARY);
        form.add(buttonPanel, JideBoxLayout.FLEXIBLE);

        applyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                // color map already be set and therefore property change event never fired.
                ColorMapTablePresenter.this.getColorMapParameter().setParameter(colorTable.getEditedColorMap());
            }
        });

        initSegmentSpinner();

    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.add(new SelectAllAction());
        toolBar.add(new EqualBinsAction());
        toolBar.add(new ColorGradientAction());
        return toolBar;
    }

    private void initSegmentSpinner() {
        DisplayParameter<IColorMap> param = getColorMapParameter();


        if (param != null) {
            segmentSpinner.setEnabled(true);
            IColorMap cmap = param.getParameter();
            segmentSpinner.setModel(new SpinnerNumberModel(cmap.getMapSize(), 1, 256, 1));


        } else {
            segmentSpinner.setEnabled(false);
        }


    }


    public void setColorMap(DisplayParameter<IColorMap> param) {

        initSegmentSpinner();
        colorTable.setColorMap(param.getParameter());
        System.out.println("setting color map in color table");

        param.addPropertyChangeListener(new DelayedPropertyChangeHandler(5000) {

            public void delayedPropertyChange(PropertyChangeEvent evt) {
                System.out.println("event dispatch thread? " + SwingUtilities.isEventDispatchThread());

                colorTable.setColorMap(ColorMapTablePresenter.this.getColorMapParameter().getParameter());

            }
        });


    }

    public JComponent getComponent() {
        return form;
    }


    class ColorGradientAction extends BasicAction {


        public ColorGradientAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("resources/icons/problemview.gif"))));
                putValue(Action.SHORT_DESCRIPTION, "Adds a color gradient to selected region.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void execute(ActionEvent actionEvent) throws Exception {
            IColorMap colorMap = colorTable.getEditedColorMap();
            if (colorMap instanceof RaggedColorMap) {
                JTable table = colorTable.getComponent();
                int[] rows = table.getSelectedRows();
                RaggedColorMap cmap = (RaggedColorMap) colorMap;
                if (rows.length == 0) {
                    // no selection
                    // equalize all
                    rows = new int[2];
                    rows[0] = 0;
                    rows[1] = cmap.getMapSize() - 1;
                }
                // assuming contiguous

                // assuming contiguous

                int r1 = rows[0];
                int r2 = rows[rows.length - 1];
                java.util.List<Color> clist = ColorTable.createColorGradient(cmap.getInterval(r1).getColor(), cmap.getInterval(r2).getColor(), r2 - r1 + 1);
                for (int i = r1; i < (r2 + 1); i++) {
                    cmap.setColor(i, clist.get(i - r1));
                }

                colorTable.getModel().fireTableDataChanged();
                DefaultListSelectionModel model = new DefaultListSelectionModel();
                model.setSelectionInterval(rows[0], rows[rows.length - 1]);
                table.setSelectionModel(model);

            } else {
                System.out.println("not a ragged color map");
            }

        }


        public boolean shouldBeEnabled() {
            System.out.println("somebody is calling shouldBeEnabled");
            return false;
        }
    }

    class SelectAllAction extends BasicAction {

        public SelectAllAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("resources/icons/lprio_tsk.gif"))));
                //putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("resources/icons/logical_package_obj.gif"))));

                putValue(Action.SHORT_DESCRIPTION, "Select All");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void execute(ActionEvent actionEvent) throws Exception {
            JTable table = colorTable.getComponent();
            DefaultListSelectionModel model = new DefaultListSelectionModel();
            model.setSelectionInterval(0, table.getRowCount() - 1);
            table.setSelectionModel(model);

        }


    }


    class EqualBinsAction extends BasicAction {


        public EqualBinsAction() {
            try {
                putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("resources/icons/stkfrm_obj.gif"))));
                //putValue(Action.SMALL_ICON, new ImageIcon(ImageIO.read(ResourceLoader.getResource("resources/icons/int_obj.gif"))));

                putValue(Action.SHORT_DESCRIPTION, "make the size of all bin intervals equal");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        protected void execute(ActionEvent actionEvent) throws Exception {
            IColorMap colorMap = colorTable.getEditedColorMap();
            if (colorMap instanceof RaggedColorMap) {
                JTable table = colorTable.getComponent();
                int[] rows = table.getSelectedRows();
                RaggedColorMap cmap = (RaggedColorMap) colorMap;
                if (rows.length == 0) {
                    // no selection
                    // equalize all
                    rows = new int[2];
                    rows[0] = 0;
                    rows[1] = cmap.getMapSize() - 1;
                }
                // assuming contiguous

                if (rows[rows.length - 1] > rows[0]) {
                    cmap.equalize(rows[0], rows[rows.length - 1]);
                    colorTable.getModel().fireTableDataChanged();
                    DefaultListSelectionModel model = new DefaultListSelectionModel();
                    model.setSelectionInterval(rows[0], rows[rows.length - 1]);
                    table.setSelectionModel(model);
                }

            } else {
                System.out.println("not a ragged color map");


            }

        }


        public boolean shouldBeEnabled() {
            System.out.println("somebody is calling shouldBeEnabled");
            return false;
        }
    }


}
