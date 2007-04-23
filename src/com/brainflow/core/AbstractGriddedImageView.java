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
public abstract class AbstractGriddedImageView extends ImageView  {

    private ArrayListModel plots = new ArrayListModel();

    private SelectionInList plotSelection = new SelectionInList((ListModel) plots);

    private int NRows = 3;

    private int NCols = 3;

    private GridLayout layout;


    public AbstractGriddedImageView(IImageDisplayModel imodel) {
        super(imodel);
      
    }


    protected AbstractGriddedImageView(IImageDisplayModel imodel, int NRows, int NCols) {
        super(imodel);
        this.NRows = NRows;
        this.NCols = NCols;
    }

    protected void layoutGrid() {
        layout = new GridLayout(getNRows(), getNCols());
        setLayout(layout);

        int count = 0;
        for (int i=0; i<getNRows(); i++) {
            for (int j=0; j<getNCols(); j++) {
                IImagePlot plot = makePlot(count++, i,j);
                plots.add(plot);
                add(plot.getComponent());
            }

        }

        plotSelection.setSelection(plots.get(0));

    }

    public SelectionInList getPlotSelection() {
        return plotSelection;
    }

    protected abstract IImagePlot makePlot(int index, int row, int column);


    public int getNRows() {
        return NRows;
    }

   

    public int getNCols() {
        return NCols;
    }


    public int getNumPlots() {
        return plots.size();
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


   


}
