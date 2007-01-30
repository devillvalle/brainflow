package com.brainflow.application.actions;

import org.bushe.swing.action.BasicAction;

import java.awt.event.ActionEvent;

import com.brainflow.core.ImageView;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.display.Crosshair;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.space.Axis;
import com.brainflow.image.axis.ImageAxis;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jan 29, 2007
 * Time: 12:36:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class NextSliceAction extends BasicAction {


    protected void execute(ActionEvent evt) throws Exception {

        ImageView view = (ImageView) getContextValue(ActionContext.SELECTED_IMAGE_VIEW);

        if (view != null) {
           Crosshair cross = view.getCrosshair();
           AnatomicalVolume displayAnatomy = view.getDisplayAnatomy();
           Axis axis = cross.getViewport().getBounds().findAxis(displayAnatomy.ZAXIS);
           ImageAxis iaxis = view.getImageDisplayModel().getImageAxis(axis);

           int sample = iaxis.nearestSample(cross.getValue(displayAnatomy.ZAXIS));
           int nsample = sample + 1;
           if (nsample >= 0 && nsample <iaxis.getNumSamples()) {
               cross.setValue(iaxis.valueOf(nsample));
           }
        }


    }


}
