package com.brainflow.application.presentation;

import com.brainflow.application.presentation.forms.TitledPanel;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.IconImageView;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Feb 20, 2005
 * Time: 1:31:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageIconPanel extends JPanel {

    private IImageDisplayModel displayModel;
    private List<IconImageView> viewList = new ArrayList<IconImageView>();

    private static Color BACKGROUND_COLOR = new Color(193, 226, 255);

    private int selectedPanel = 0;
    private Color selectedColor = Color.RED;

    public ImageIconPanel(IImageDisplayModel _displayModel) {
        displayModel = _displayModel;

        FormLayout layout = new FormLayout("8dlu, left:p, 8dlu", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, this);
        builder.appendRow(builder.getLineGapSpec());
        builder.nextLine();
        CellConstraints cc = new CellConstraints();

        for (int i = 0; i < displayModel.getNumLayers(); i++) {
            //System.out.println("creating icon image view for layer " + i);
            //IconImageView iview = new IconImageView(new ImageDisplayModelProxy(displayModel, i));
            //viewList.add(iview);


            //TitledPanel tp = new TitledPanel(iview.getImageDisplayModel().getName(), iview);
            //if (selectedPanel == i) {
                //iview.setBorder(new LineBorder(selectedColor));
            //
           // }

           // builder.setLeadingColumnOffset(1);
           // builder.append(tp);
           // builder.nextLine();

        }

       // builder.appendRow(builder.getLineGapSpec());
       // setBackground(BACKGROUND_COLOR);


    }


    public static void main(String[] args) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
