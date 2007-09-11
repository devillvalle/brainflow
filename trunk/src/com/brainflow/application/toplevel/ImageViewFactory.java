package com.brainflow.application.toplevel;

import com.brainflow.application.actions.SetPreserveAspectCommand;
import com.brainflow.core.*;
import com.brainflow.core.annotations.CrosshairAnnotation;
import com.brainflow.core.annotations.SelectedPlotAnnotation;
import com.brainflow.core.annotations.SliceAnnotation;
import com.brainflow.gui.PopupAdapter;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.axis.AxisRange;
import com.brainflow.utils.StringGenerator;
import com.pietschy.command.ActionCommand;
import com.pietschy.command.CommandContainer;
import com.pietschy.command.group.CommandGroup;

import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Jul 31, 2006
 * Time: 11:31:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class ImageViewFactory {

    private static final Logger log = Logger.getLogger(ImageViewFactory.class.getCanonicalName());


    public static IImagePlot createAxialPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalAxial());
    }

    public static IImagePlot createCoronalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalCoronal());
    }

    public static IImagePlot createSagittalPlot(IImageDisplayModel displayModel) {
        return ImageViewFactory.createPlot(displayModel, Anatomy3D.getCanonicalSagittal());
    }

    public static void addDefaultAnnotations(ImageView view) {
        CrosshairAnnotation crosshairAnnotation = new CrosshairAnnotation(view.getCrosshair());
        view.setAnnotation(CrosshairAnnotation.ID, crosshairAnnotation);
        view.setAnnotation(SelectedPlotAnnotation.ID, new SelectedPlotAnnotation(view));
        view.setAnnotation(SliceAnnotation.ID, new SliceAnnotation());
    }


    public static IImagePlot createPlot(IImageDisplayModel displayModel, Anatomy3D displayAnatomy) {


        AxisRange xrange = displayModel.getImageAxis(displayAnatomy.XAXIS).getRange();
        AxisRange yrange = displayModel.getImageAxis(displayAnatomy.YAXIS).getRange();

        return new ComponentImagePlot(displayModel, displayAnatomy, xrange, yrange);

    }


    public static ImageView createOrthogonalView(ImageView source, OrthoPlotLayout.ORIENTATION orientation) {
        ImageView view = new ImageView(source.getModel());
        view.setPlotLayout(new OrthoPlotLayout(view,  orientation));
        addDefaultAnnotations(view);
        ImageCanvasManager.getInstance().yoke(source, view);

        return view;

    }

    public static ImageView createYokedAxialView(ImageView source) {
        ImageView view = new ImageView(source.getModel(), Brainflow.getInstance().getCommandContainer());
        view.setPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalAxial()));
        addDefaultAnnotations(view);

        return view;
    }

    public static ImageView createYokedCoronalView(ImageView source) {
        ImageView view = new ImageView(source.getModel(), Brainflow.getInstance().getCommandContainer());
        view.setPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalCoronal()));
        ImageCanvasManager.getInstance().yoke(source, view);
        return view;
    }


    public static ImageView createYokedSagittalView(ImageView source) {
        ImageView view = new ImageView(source.getModel(), Brainflow.getInstance().getCommandContainer());
        view.setPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalSagittal()));
        ImageCanvasManager.getInstance().yoke(source, view);
        return view;

    }


    public static ImageView createAxialView(IImageDisplayModel displayModel) {
        ImageView view = new ImageView(displayModel, Brainflow.getInstance().getCommandContainer());
        view.setPlotLayout(new SimplePlotLayout(view, Anatomy3D.getCanonicalAxial()));
        addDefaultAnnotations(view);

        /*ActionCommand aspectCommand = new SetPreserveAspectCommand(view);
        aspectCommand.bind(view);
        CommandGroup viewGroup = new CommandGroup("image-view-menu");
        viewGroup.bind(view);
        PopupAdapter adapter = new PopupAdapter(view, viewGroup.createPopupMenu());
        */

        return view;
    }


    public static ImageView createExpandedView(IImageDisplayModel displayModel) {
        ExpandedImageView view = new ExpandedImageView(displayModel, Anatomy3D.getCanonicalAxial());

        return view;
    }

    public static ImageView createMontageView(IImageDisplayModel displayModel, int nrows, int ncols, double sliceGap) {
        MontageImageView view = new MontageImageView(displayModel, Anatomy3D.getCanonicalAxial(), nrows, ncols, sliceGap);

        return view;
    }

    public static ImageView createOrthogonalView(IImageDisplayModel displayModel) {
        SimpleOrthogonalImageView view = new SimpleOrthogonalImageView(displayModel);
      //ITitledBorder tborder = border;
        //tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }

    public static ImageView createCoronalView(IImageDisplayModel displayModel) {
        SimpleImageView view = new SimpleImageView(displayModel, Anatomy3D.getCanonicalCoronal());

        //ITitledBorder tborder = border;
        //tborder.setTitleGenerator(new ImageViewTitleGenerator(view));

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
        SimpleImageView view = new SimpleImageView(displayModel, Anatomy3D.getCanonicalSagittal());
       //ITitledBorder tborder = border;
        //tborder.setTitleGenerator(new ImageViewTitleGenerator(view));


        return view;
    }


    public static class ImageViewTitleGenerator implements StringGenerator {

        private ImageView view;

        public ImageViewTitleGenerator(ImageView _view) {
            view = _view;

        }

        public String getString() {
            if (view != null) {
                return "[" + view.toString() + "] " + view.getModel().getName();
            }

            return null;

        }
    }


}
