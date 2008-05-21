package com.brainflow.application.toplevel;

import com.brainflow.application.services.ImageViewCursorEvent;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.jidesoft.status.LabelStatusBarItem;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.MessageFormat;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Feb 20, 2007
 * Time: 11:25:13 AM
 */
public class CrosshairCoordinates extends LabelStatusBarItem implements EventSubscriber {

    private LabelStatusBarItem xaxisLabel = new LabelStatusBarItem();

    private LabelStatusBarItem yaxisLabel = new LabelStatusBarItem();

    private LabelStatusBarItem zaxisLabel = new LabelStatusBarItem();


    private MessageFormat format = new MessageFormat("{0}: {1, number, ##0.0}");

    public CrosshairCoordinates() {
        EventBus.subscribeExactly(ImageViewCursorEvent.class, this);
        xaxisLabel.setText("0.0");
        yaxisLabel.setText("0.0");
        zaxisLabel.setText("0.0");
        xaxisLabel.setBorder(new EmptyBorder(0,0,0,0));
        xaxisLabel.setMinimumSize(new Dimension(60, 0));
        yaxisLabel.setMinimumSize(new Dimension(60, 0));
        zaxisLabel.setMinimumSize(new Dimension(60, 0));

        setBorder(new EmptyBorder(0,0,0,0));

    }

    public LabelStatusBarItem getXaxisLabel() {
        return xaxisLabel;
    }

    public LabelStatusBarItem getYaxisLabel() {
        return yaxisLabel;
    }

    public LabelStatusBarItem getZaxisLabel() {
        return zaxisLabel;
    }


    public void onEvent(Object evt) {
        ImageViewCursorEvent event = (ImageViewCursorEvent) evt;
        AnatomicalPoint3D cursor = event.getCursor();

        if (cursor != null) {

            Anatomy3D anatomy = cursor.getAnatomy();

            xaxisLabel.setText(format.format(new Object[]{
                    anatomy.XAXIS.toString().substring(0, 1),
                    cursor.getValue(anatomy.XAXIS).getValue()}
            ));

            yaxisLabel.setText(format.format(new Object[]{
                    anatomy.YAXIS.toString().substring(0, 1),
                    cursor.getValue(anatomy.YAXIS).getValue()}
            ));

            zaxisLabel.setText(format.format(new Object[]{
                    anatomy.ZAXIS.toString().substring(0, 1),
                    cursor.getValue(anatomy.ZAXIS).getValue()}
            ));


        }
    }
}
