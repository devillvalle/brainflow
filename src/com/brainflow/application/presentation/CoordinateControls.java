package com.brainflow.application.presentation;

import com.brainflow.application.services.ImageViewEvent;
import com.brainflow.core.ImageView;
import com.jidesoft.pane.CollapsiblePane;
import com.jidesoft.pane.CollapsiblePanes;
import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventSubscriber;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 16, 2006
 * Time: 1:40:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoordinateControls {

    private ImageView activeView;
    private CollapsiblePanes cpanes;

    private WorldCoordinatePresenter worldCoordinatePresenter;
    private IndexCoordinatePresenter indexCoordinatePresenter;
    private ImageViewportPresenter imageViewportPresenter;
    //private ImageExtentPresenter imageExtentPresenter;

    public CoordinateControls() {
        EventBus.subscribeStrongly(ImageViewEvent.class, new EventSubscriber() {
            public void onEvent(Object e) {
                ImageViewEvent event = (ImageViewEvent) e;
                activeView = event.getImageView();
                if (activeView != null) {
                    worldCoordinatePresenter.setCrosshair(activeView.getCrosshair());
                    indexCoordinatePresenter.setCrosshair(activeView.getCrosshair());
                    //imageViewportPresenter.setViewport(activeView.getViewport());
                    //imageExtentPresenter.setViewport(activeView.getViewport());

                } 
            }
        });

        init();


    }

    public JComponent getComponent() {
        return cpanes;
    }

    private void init() {
        cpanes = new CollapsiblePanes();


        if (activeView != null) {
            worldCoordinatePresenter = new WorldCoordinatePresenter(activeView.getCrosshair());
            indexCoordinatePresenter = new IndexCoordinatePresenter(activeView.getCrosshair());
            imageViewportPresenter = new ImageViewportPresenter();
            //imageExtentPresenter = new ImageExtentPresenter(activeView.getViewport());

        } else {
            worldCoordinatePresenter = new WorldCoordinatePresenter(null);
            indexCoordinatePresenter = new IndexCoordinatePresenter(null);
            imageViewportPresenter = new ImageViewportPresenter();
            //imageExtentPresenter = new ImageExtentPresenter(null);
        }

        CollapsiblePane p1 = new CollapsiblePane();
        p1.setContentPane(worldCoordinatePresenter.getComponent());
        p1.setTitle("Crosshair (World)");
        p1.setEmphasized(true);
        p1.setOpaque(false);
        cpanes.add(p1);

        CollapsiblePane p2 = new CollapsiblePane();
        p2.setContentPane(indexCoordinatePresenter.getComponent());
        p2.setTitle("Crosshair (Voxel)");
        p2.setEmphasized(true);
        p2.setOpaque(false);
        cpanes.add(p2);

        CollapsiblePane p3 = new CollapsiblePane();
        p3.setContentPane(imageViewportPresenter.getComponent());
        p3.setTitle("Plot Viewport");
        p3.setEmphasized(true);
        p3.setOpaque(false);
        cpanes.add(p3);

        //CollapsiblePane p4 = new CollapsiblePane();
        //p4.setContentPane(imageExtentPresenter.getComponent());
        //p4.setTitle("Image Extent");
        //p4.setEmphasized(true);
        //p4.setOpaque(false);
        //cpanes.add(p4);

        cpanes.addExpansion();


    }
}
