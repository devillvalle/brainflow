package com.brainflow.image.operations;

import com.brainflow.image.anatomy.AnatomicalPoint1D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.data.BasicImageData2D;
import com.brainflow.image.data.IImageData3D;
import com.brainflow.image.data.ImageFiller;
import com.brainflow.image.data.DataAccessor3D;

import java.util.logging.Logger;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 *
 * @author Bradley Buchsbaum
 * @version 1.0
 */

public class ImageSlicer {


    private DataAccessor3D image;

    private Anatomy3D displayAnatomy = Anatomy3D.AXIAL_LAI;


    public ImageSlicer(DataAccessor3D _image) {
        image = _image;

    }


    public Anatomy3D getDisplayAnatomy() {
        return displayAnatomy;
    }

    public void setDisplayAnatomy(Anatomy3D displayAnatomy) {
        this.displayAnatomy = displayAnatomy;
    }


    public BasicImageData2D getSlice(Anatomy3D displayAnatomy, int fixedSlice) {
     
        ImageFiller filler = new ImageFiller();
        return filler.fillImage(image, displayAnatomy, fixedSlice);



    }



}


