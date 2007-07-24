package com.brainflow.image.io.afni;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 9:41:58 AM
 */
public enum AFNIAttributeKey {


    HISTORY_NOTE,
    TYPESTRING,
    IDCODE_STRING,
    IDCODE_DATE,
    SCENE_DATA,
    LABEL_1,
    LABEL_2,
    DATASET_NAME,
    ORIENT_SPECIFIC,
    ORIGIN,
    DELTA,
    IJK_TO_DICOM,
    BRICK_STATS,
    DATASET_RANK,
    DATASET_DIMENSIONS,
    BRICK_TYPES,
    BRICK_FLOAT_FACS,
    BRICK_LABS,
    BRICK_KEYWORDS,
    BYTEORDER_STRING,;


    public static void main(String[] args) {
        AFNIAttributeKey[] keys = AFNIAttributeKey.values();

        for (int i = 0; i < keys.length; i++) {
            System.out.println("" + keys[i].name());
        }

        System.out.println(AFNIAttributeKey.valueOf("HISTORY_NOTE"));
    }


}
