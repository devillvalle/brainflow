package com.brainflow.image.space;

import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.utils.*;
import com.brainflow.utils.Index3D;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 12, 2008
 * Time: 9:20:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IImageSpace3D extends IImageSpace {

    @Override
    Anatomy3D getAnatomy();

    ImageMapping3D getMapping();

    IDimension<Float> getOrigin();

    IDimension<Integer> getDimension();

    float[] gridToWorld(int x, int y, int z);

    float[] gridToWorld(int[] gridpos);

    float[] worldToGrid(float[] coord);

    com.brainflow.utils.Index3D indexToGrid(int idx, Index3D voxel);

    Index3D indexToGrid(int idx);

    int indexToGridX(int idx);

    int indexToGridY(int idx);

    int indexToGridZ(int idx);

    float worldToGridX(float x, float y, float z);

    float worldToGridY(float x, float y, float z);

    float worldToGridZ(float x, float y, float z);

    float gridToWorldX(float x, float y, float z);

    float gridToWorldY(float x, float y, float z);

    float gridToWorldZ(float x, float y, float z);
}
