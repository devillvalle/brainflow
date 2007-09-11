package com.brainflow.application.actions;

import com.brainflow.core.IImagePlot;
import com.brainflow.core.ImageView;
import com.brainflow.display.ICrosshair;
import com.jidesoft.popup.JidePopup;
import org.bushe.swing.action.BasicAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Aug 1, 2006
 * Time: 1:46:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectInfoAction extends BasicAction {


    protected void execute(ActionEvent actionEvent) throws Exception {
        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);
        if (view == null) {
            return;
        }

        JidePopup popup = new JidePopup();
        popup.setMovable(true);
        popup.getContentPane().setLayout(new BorderLayout());
        JTextArea area = new JTextArea();
        area.setRows(10);
        area.setColumns(40);
        area.append(collectInfo(view));
        popup.getContentPane().add(new JScrollPane(area));
        popup.setOwner(view);
        popup.setResizable(true);
        popup.setMovable(true);
        popup.showPopup();


    }


    private String collectInfo(ImageView view) {
        StringBuffer sb = new StringBuffer();
        java.util.List<IImagePlot> plots = view.getPlots();
        IImagePlot iplot = plots.get(0);
        sb.append("X Axis: " + iplot.getXAxisRange().getAnatomicalAxis() + "\n");
        sb.append("Range " + iplot.getXAxisRange().getMinimum() + " to " + iplot.getXAxisRange().getMaximum() + "\n");
        sb.append("Y Axis: " + iplot.getYAxisRange().getAnatomicalAxis() + "\n");
        sb.append("Range " + iplot.getYAxisRange().getMinimum() + " to " + iplot.getYAxisRange().getMaximum() + "\n");


        ICrosshair cross = view.getCrosshair();
        sb.append("Crosshair: " + "\n");
        sb.append("zero loc " + cross.getValue(iplot.getXAxisRange().getAnatomicalAxis()) + "\n");
        sb.append("zero loc " + cross.getValue(iplot.getYAxisRange().getAnatomicalAxis()) + "\n");


        return sb.toString();
    }
}
