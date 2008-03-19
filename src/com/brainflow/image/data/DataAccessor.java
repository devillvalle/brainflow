package com.brainflow.image.data;

import com.brainflow.utils.IDimension;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 17, 2008
 * Time: 10:04:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DataAccessor {


    public double getValue(int index);

    public IDimension<Integer> getDimension();

    public int numElements();

}
