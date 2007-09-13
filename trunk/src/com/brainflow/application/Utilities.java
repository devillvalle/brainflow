package com.brainflow.application;

import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 13, 2007
 * Time: 9:32:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class Utilities {


    private static final String dataDir = "resources/data/";
    
    public static URL getDataURL(String fileName) {
        return ClassLoader.getSystemResource(dataDir + "icbm452_atlas_probability_temporal.hdr");
    }


}
