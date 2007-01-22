package com.brainflow.core;

import com.brainflow.application.managers.ImageViewRegistry;
import com.brainflow.image.anatomy.AnatomicalVolume;
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


    public static ImageView createAxialView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalAxial());
        String id = ImageViewRegistry.getInstance().register(view);
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
        String id = ImageViewRegistry.getInstance().register(view);
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
        String id = ImageViewRegistry.getInstance().register(view);
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
         CrosshairAnnotation annotation = (CrosshairAnnotation)view.getAnnotation(CrosshairAnnotation.class);
         if (annotation != null) {
             newView.setAnnotation(annotation);
         }

         return newView;

     }

    public static ImageView createCoronalView(ImageView view) {
         ImageView newView = createCoronalView(view.getImageDisplayModel());
         CrosshairAnnotation annotation = (CrosshairAnnotation)view.getAnnotation(CrosshairAnnotation.class);
         if (annotation != null) {
             newView.setAnnotation(annotation);
         }

         return newView;

     }

    public static ImageView createAxialView(ImageView view) {
         ImageView newView = createAxialView(view.getImageDisplayModel());
         CrosshairAnnotation annotation = (CrosshairAnnotation)view.getAnnotation(CrosshairAnnotation.class);
         if (annotation != null) {
             newView.setAnnotation(annotation);
         }

         return newView;

     }

    public static ImageView createSagittalView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, AnatomicalVolume.getCanonicalSagittal());
        String id = ImageViewRegistry.getInstance().register(view);

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
