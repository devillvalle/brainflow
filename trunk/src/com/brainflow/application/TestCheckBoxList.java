package com.brainflow.application;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.core.ImageLayer3D;
import com.brainflow.core.SnapShooter;
import com.brainflow.display.ImageLayerParameters;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.io.BasicImageReader;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.image.io.NiftiInfoReader;
import com.jidesoft.swing.CheckBoxList;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 31, 2006
 * Time: 9:25:48 AM
 * To change this template use File | Settings | File Templates.
 */


public class TestCheckBoxList {

    CheckBoxList clist;


    IImageDisplayModel displayModel;
    DefaultListModel listModel = new DefaultListModel();
    SnapShooter shooter;

    public TestCheckBoxList(IImageDisplayModel _displayModel) {
        displayModel = _displayModel;
        shooter = new SnapShooter(displayModel, AnatomicalVolume.getCanonicalAxial());

        /*clist = new CheckBoxList() {
            public boolean isCheckBoxEnabled(int index) {
                return true;
            }
        };  */

        clist = new CheckBoxList(new String[]{"Hello", "Goodbye"});

        //Bindings.bind(clist, displayModel.getSelection());
        clist.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        clist.setCellRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                double slice = displayModel.getCompositeImageSpace().getCentroid().getValue(AnatomicalVolume.getCanonicalAxial().ZAXIS).getX();

                label.setIcon(shooter.shootLayer(slice, index, 32, 32));
                return label;
            }
        });

        clist.setCheckBoxEnabled(true);

    }

    public JList getList() {
        return clist;
    }


    public static void main(String[] args) {
        

    }


}
