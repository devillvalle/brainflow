package com.brainflow.core;


import com.brainflow.application.managers.ImageCanvasManager;
import com.brainflow.image.anatomy.AnatomicalVolume;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.image.space.ImageSpace3D;
import com.brainflow.utils.FilledSelectableBorder;
import com.brainflow.utils.ITitledBorder;
import com.brainflow.utils.StringGenerator;
import com.brainflow.core.annotations.CrosshairAnnotation;

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
        DefaultImageProcurer procurer = new DefaultImageProcurer(displayModel, displayAnatomy);

        ImageSpace3D space = (ImageSpace3D) displayModel.getImageSpace();
        procurer.setSlice(space.getCentroid().getValue(displayAnatomy.ZAXIS));

        IImageCompositor compositor = new DefaultImageCompositor();
        ImagePlotRenderer plotRenderer = new ImagePlotRenderer(compositor, procurer);

        return new BasicImagePlot(displayAnatomy, xrange, yrange, plotRenderer);

    }

    public static ImageView createYokedAxialView(ImageView source) {
        SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalAxial());

        String id = ImageCanvasManager.getInstance().register(view);
        ImageCanvasManager.getInstance().yokeViews(source, view);
        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());

        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }

    public static ImageView createYokedCoronalView(ImageView source) {
            SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalCoronal());

            String id = ImageCanvasManager.getInstance().register(view);
            ImageCanvasManager.getInstance().yokeViews(source, view);
            view.setId(id);
            FilledSelectableBorder border = new FilledSelectableBorder(view);
            view.setBorder(border);
            view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());

            ITitledBorder tborder = border;
            tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


            return view;
        }


    public static ImageView createYokedSagittalView(ImageView source) {
            SimpleImageView view = new SimpleImageView(source, AnatomicalVolume.getCanonicalSagittal());

            String id = ImageCanvasManager.getInstance().register(view);
            ImageCanvasManager.getInstance().yokeViews(source, view);
            view.setId(id);
            FilledSelectableBorder border = new FilledSelectableBorder(view);
            view.setBorder(border);
            view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());

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
        view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());

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
        view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());

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
        view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());


        ITitledBorder tborder = border;
        tborder.setTitleGenerator(new ImageViewTitleGenerator(view));

        return view;
    }

    public static ImageView createSagittalView(ImageView view) {
        ImageView newView = createSagittalView(view.getImageDisplayModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(CrosshairAnnotation.class);
        if (annotation != null) {
            newView.setAnnotation(annotation);
        }

        return newView;

    }

    public static ImageView createCoronalView(ImageView view) {
        ImageView newView = createCoronalView(view.getImageDisplayModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(CrosshairAnnotation.class);
        if (annotation != null) {
            newView.setAnnotation(annotation);
        }

        return newView;

    }

    public static ImageView createAxialView(ImageView view) {
        ImageView newView = createAxialView(view.getImageDisplayModel());
        CrosshairAnnotation annotation = (CrosshairAnnotation) view.getAnnotation(CrosshairAnnotation.class);
        if (annotation != null) {
            newView.setAnnotation(annotation);
        }

        return newView;

    }

    public static ImageView createSagittalView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalSagittal());
        String id = ImageCanvasManager.getInstance().register(view);

        view.setId(id);
        FilledSelectableBorder border = new FilledSelectableBorder(view);
        view.setBorder(border);
        view.setName("[" + view.getId() + "] " + view.getImageDisplayModel().getName());


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
                return "[" + view.getId() + "] " + view.getImageDisplayModel().getName();
            }

            return null;

        }
    }


}
