package com.brainflow.image.io;


/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 9:41:58 AM
 */
public enum AFNIAttributeKey implements HeaderKey {
   
    MARKS_FLAGS,
    MARKS_HELP,
    MARKS_LAB,
    MARKS_XYZ,
    BRICK_STATSYM,
    BRICK_STATAUX,
    STAT_AUX,
    IJK_TO_DICOM_REAL,
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
    BYTEORDER_STRING,
    TAXIS_NUMS,
    TAXIS_FLOATS,
    WARPDRIVE_MATVEC_FOR_000000,
    WARPDRIVE_MATVEC_INV_000000;


    public static void main(String[] args) {
        AFNIAttributeKey[] keys = values();

        for (int i = 0; i < keys.length; i++) {
            System.out.println("" + keys[i].name());
        }

        
    }


}
