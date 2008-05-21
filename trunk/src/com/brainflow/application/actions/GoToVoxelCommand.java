package com.brainflow.application.actions;

import com.brainflow.core.ImageView;
import com.brainflow.image.anatomy.AnatomicalPoint3D;
import com.brainflow.image.anatomy.Anatomy3D;
import com.jidesoft.dialog.JideOptionPane;

import java.util.StringTokenizer;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 6, 2008
 * Time: 1:13:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoToVoxelCommand extends BrainFlowCommand {

    public GoToVoxelCommand() {
        super("goto-voxel");
    }

    protected void handleExecute() {
        ImageView view = getSelectedView();
        int[] vox = new int[3];
        if (view != null) {
            String res = JideOptionPane.showInputDialog("Enter voxel coordinates (i j k): ");
            StringTokenizer tokenizer = new StringTokenizer(res, " ");


            int count=0;
            while (tokenizer.hasMoreTokens() && count < 3) {
                String t = tokenizer.nextToken();
                try {
                    vox[count] = Integer.parseInt(t);
                    System.out.println("vox " + count + " " + vox[count]);
                    count++;
                } catch (NumberFormatException e) {
                    //todo report error on status bar?
                    return;
                }

            }
        }

        float[] coords = view.getModel().getImageSpace().indexToWorld(vox);
        System.out.println("coords = " + Arrays.toString(coords));
        System.out.println("anatomy of view : " + view.getModel().getImageSpace().getAnatomy());
        System.out.println("anatomy of cursor : " + view.cursorPos.get().getAnatomy());

        AnatomicalPoint3D ap = new AnatomicalPoint3D((Anatomy3D)view.getModel().getImageSpace().getAnatomy(), coords[0], coords[1], coords[2]);
        view.cursorX.set(ap.getX());
        view.cursorY.set(ap.getY());
        view.cursorZ.set(ap.getZ());
    }
}
