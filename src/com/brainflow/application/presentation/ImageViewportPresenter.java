package com.brainflow.application.presentation;

import com.brainflow.core.ImageView;
import com.brainflow.core.IImagePlot;
import com.brainflow.core.SimpleImageView;
import com.brainflow.core.ImageDisplayModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2007
 * Time: 10:54:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewportPresenter extends ImageViewPresenter {

    private JComboBox plotSelector = new JComboBox();

    private JPanel form;

    private JPanel imageBox;
    private JLabel choiceLabel;

    private JSpinner xorigin;
    private JSpinner yorigin;

    private FormLayout layout;

    private SimpleImageView boxView;
    private JPanel viewPanel;

    public ImageViewportPresenter() {
        buildGUI();
    }

    private void buildGUI() {
        form = new JPanel();
        layout = new FormLayout("8dlu, p:g, 6dlu, p:g, 8dlu", "8dlu, p, 6dlu, p, 6dlu, p, 6dlu, p, 6dlu, max(125dlu;p), 1dlu, 6dlu");
        form.setLayout(layout);
        layout.addGroupedColumn(2);
        layout.addGroupedColumn(4);
        CellConstraints cc = new CellConstraints();

        choiceLabel = new JLabel("Selected Plot: ");
        form.add(choiceLabel, cc.xy(2, 2));
        form.add(plotSelector, cc.xywh(2, 4, 3, 1));


        xorigin = new JSpinner();
        xorigin.setEditor(new JSpinner.NumberEditor(xorigin, "###"));

        yorigin = new JSpinner();
        yorigin.setEditor(new JSpinner.NumberEditor(yorigin, "###"));

        form.add(new JLabel("Origin: "), cc.xy(2,6));

        JPanel xpan = new JPanel();
        xpan.setLayout(new BoxLayout(xpan, BoxLayout.X_AXIS));
        xpan.add(new JLabel("X: "));
        xpan.add(xorigin);

        JPanel ypan = new JPanel();
        ypan.setLayout(new BoxLayout(ypan, BoxLayout.X_AXIS));
        ypan.add(new JLabel("Y: "));
        ypan.add(yorigin);

        form.add(xpan, cc.xy(2,8));
        form.add(ypan, cc.xy(4,8));

        viewPanel = new JPanel();boxView = new SimpleImageView(new ImageDisplayModel("NULL"));
        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2,2,2,2));

        boxView.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        viewPanel.add(boxView);
        viewPanel.setBorder(BorderFactory.createEmptyBorder());
        ////imageBox = new JPanel();
        //imageBox.setOpaque(true);
        //imageBox.setBackground(Color.BLACK);
        form.add(viewPanel, cc.xywh(2,10,3,2));
        


    }

    public void viewSelected(ImageView view) {
        IImagePlot plot = view.getSelectedPlot();
        Bindings.bind(plotSelector, view.getPlotSelection());
        viewPanel.remove(boxView);
        
        boxView = new SimpleImageView(view.getModel());
        boxView.clearAnnotations();
        boxView.getSelectedPlot().setPlotInsets(new Insets(2,2,2,2));
        viewPanel.add(boxView);

        form.revalidate();
        
    }

    public void allViewsDeselected() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public JComponent getComponent() {
        return form;
    }
}
