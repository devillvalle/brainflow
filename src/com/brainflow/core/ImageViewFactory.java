package com.brainflow.core;

import com.brainflow.application.toplevel.ImageCanvasManager;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.FilledSelectableBorder;
import com.brainflow.utils.ITitledBorder;
import com.brainflow.utils.StringGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:31:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageViewFactory {


    public static IImagePlot createAxialPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, AnatomicalVolume.getCanonicalAxial());
    }

    public static IImagePlot createCoronalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, AnatomicalVolume.getCanonicalCoronal());
    }

    public static IImagePlot createSagittalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, AnatomicalVolume.getCanonicalSagittal());
    }


    public static IImagePlot createPlot(IImageDisplayModel displayModel, AnatomicalVolume displayAnatomy) {


        AxisRange xrange = displayModel.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = displayModel.getImageAxis(displayAnatomy.YAXIS).getRange();
       
        return new ComponentImagePlot(displayModel, displayAnatomy, xrange, yrange);

    }

    public static ImageView createYokedAxialView(ImageView source) {
        SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalAxial());

        String id = ImageCanvasManager.getInstance().register(view);
        ImageCanvasManager.getInstance().yoke(source, view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }

    public static ImageView createYokedCoronalView(ImageView source) {
        SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalCoronal());

        String id = ImageCanvasManager.getInstance().register(view);
        ImageCanvasManager.getInstance().yoke(source, view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }


    public static ImageView createYokedSagittalView(ImageView source) {
        SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalSagittal());

        String id = ImageCanvasManager.getInstance().register(view);
        ImageCanvasManager.getInstance().yoke(source, view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }


    public static ImageView createAxialView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalAxial());
        String id = ImageCanvasManager.getInstance().register(view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }

    public static ImageView createOrthogonalView(IImageDisplayModel displayModel) {
        SimpleOrthogonalImageView view = new SimpleOrthogonalImageView(displayModel);
        String id = ImageCanvasManager.getInstance().register(view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }

    public static ImageView createCoronalView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalCoronal());
        String id = ImageCanvasManager.getInstance().register(view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());


        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));

        return view;
    }

    public static ImageView createSagittalView(ImageView view) {
        ImageView newView = createSagittalView(view.getModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(newView.getSelectedPlot(), CrosshairAnnotation.ID);
        if (annotation != null) {
            newView.setAnnotation(newView.getSelectedPlot(), CrosshairAnnotation.ID, annotation);
        }

        return newView;

    }

    public static ImageView createCoronalView(ImageView view) {
        ImageView newView = createCoronalView(view.getModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
        if (annotation != null) {
            newView.setAnnotation(newView.getSelectedPlot(), CrosshairAnnotation.ID, annotation);
        }

        return newView;

    }

    public static ImageView createAxialView(ImageView view) {
        ImageView newView = createAxialView(view.getModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(view.getSelectedPlot(), CrosshairAnnotation.ID);
        if (annotation != null) {
            newView.setAnnotation(newView.getSelectedPlot(), CrosshairAnnotation.ID, annotation);
        }

        return newView;

    }

    public static ImageView createSagittalView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalSagittal());
        String id = ImageCanvasManager.getInstance().register(view);

        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getModel().getName());


        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }


    public static class ImageViewTitleGenerator implements StringGenerator {

        private ImageView view;

        public ImageViewTitleGenerator(ImageView _view) {
            view = _view;

        }

        public String getString() {
            if (view != null) {
                return "[" + view.getId() + "] " + view.getModel().getName();
            }

            return null;

        }
    }


}
