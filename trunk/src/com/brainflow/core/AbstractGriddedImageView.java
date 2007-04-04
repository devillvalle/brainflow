package com.brainflow.core;

import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.list.ArrayListModel;
import com.brainflow.core.annotations.SelectedPlotAnnotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.RenderedImage;
import java.util.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Apr 1, 2007
 * Time: 11:50:38 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractGriddedImageView extends ImageView implements MouseListener {

    private ArrayListModel plots = new ArrayListModel();

    private SelectionInList plotSelection = new SelectionInList((ListModel) plots);

    private int NRows = 3;

    private int NCols = 3;

    private GridLayout layout;


    public AbstractGriddedImageView(IImageDisplayModel imodel) {
        super(imodel);
        //addMouseListener(this);


    }

    protected void layoutGrid() {
        layout = new GridLayout(getNRows(), getNCols());
        setLayout(layout);
        for (int i=0; i<getNRows(); i++) {
            for (int j=0; j<getNCols(); j++) {
                IImagePlot plot = makePlot(i,j);
                plots.add(plot);
                add(plot.getComponent());
            }

        }

    }

    public SelectionInList getPlotSelection() {
        return plotSelection;
    }

    public abstract IImagePlot makePlot(int row, int column);


    public int getNRows() {
        return NRows;
    }

    public void setNRows(int NRows) {
        this.NRows = NRows;
    }

    public int getNCols() {
        return NCols;
    }

    public void setNCols(int NCols) {
        this.NCols = NCols;
    }

    public int getSelectedPlotIndex() {
        return plotSelection.getSelectionIndex();
    }

    public IImagePlot whichPlot(Point p) {
        for (int i = 0; i < plots.size(); i++) {
            IImagePlot plot = (IImagePlot) plots.get(i);

            Point plotPoint = SwingUtilities.convertPoint(this, p, plot.getComponent());
            if (plot.getComponent().contains(plotPoint)) {
                return plot;
            }

        }

        return null;
    }

    public List<IImagePlot> getPlots() {
        List<IImagePlot> ret = new ArrayList();
        for (int i = 0; i < plots.size(); i++) {
            ret.add((IImagePlot) plots.get(i));
        }

        return ret;

    }

    public RenderedImage captureImage() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public void mouseClicked(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mousePressed(MouseEvent e) {
        IImagePlot plot = whichPlot(e.getPoint());
        if (plot != null) {
            SelectedPlotAnnotation annotation = (SelectedPlotAnnotation)getAnnotation(getSelectedPlot(), SelectedPlotAnnotation.ID);

            if (annotation != null) {
                removeAnnotation(getSelectedPlot(), SelectedPlotAnnotation.ID);
                annotation.setSelectedPlot(plot);
            }
            getPlotSelection().setSelection(plot);
            repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseEntered(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void mouseExited(MouseEvent e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
