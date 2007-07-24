package com.brainflow.image.io.afni;

import com.brainflow.image.anatomy.AnatomicalAxis;
import com.brainflow.image.anatomy.Anatomy3D;
import com.brainflow.image.io.ImageInfo;
import com.brainflow.utils.*;

import java.nio.ByteOrder;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * BrainFlow Project
 * User: Bradley Buchsbaum
 * Date: Jul 21, 2007
 * Time: 10:04:11 AM
 */
public class AFNIImageInfo extends ImageInfo {

    private static Logger log = Logger.getLogger(AFNIImageInfo.class.getName());

    private Map<AFNIAttributeKey, AFNIAttribute> attributeMap;


    public AFNIImageInfo() {
        attributeMap = new HashMap<AFNIAttributeKey, AFNIAttribute>();
    }

    public AFNIImageInfo(Map<AFNIAttributeKey, AFNIAttribute> attributeMap) {
        this.attributeMap = attributeMap;
    }


    public Map<AFNIAttributeKey, AFNIAttribute> getAttributeMap() {
        return Collections.unmodifiableMap(attributeMap);
    }


    void putAttribute(AFNIAttributeKey key, AFNIAttribute attribute) {
        processAttribute(key, attribute);
        attributeMap.put(key, attribute);

    }

    public AFNIAttribute getAttribute(AFNIAttributeKey key) {
        return attributeMap.get(key);

    }

    private void processOrigin(List<Float> origin) {
        Point3D pt = new Point3D();
        pt.setX(origin.get(0));
        pt.setY(origin.get(1));
        pt.setZ(origin.get(2));
        setOrigin(pt);

    }

    private void processDimensions(List<Integer> dims) {
        int i = dims.size() - 1;

        while (i > 0) {
            int dnum = dims.get(i);
            if (dnum == 0) {
                dims.remove(i);
            }

            i--;
        }

        IDimension idim = DimensionFactory.create(dims);
        setArrayDim(idim);
    }

    private void processSpacing(List<Float> delta) {
        Dimension3D<Float> spacing = new Dimension3D<Float>(Math.abs(delta.get(0)), Math.abs(delta.get(1)), Math.abs(delta.get(2)));
        setSpacing(spacing);
    }

    private void processByteOrder(List<String> orderList) {
        String orderStr = orderList.get(0);
        if (orderStr.equals("LSB_FIRST")) {
            this.setEndian(ByteOrder.LITTLE_ENDIAN);
        } else if (orderStr.equals("MSB_FIRST")) {
            this.setEndian(ByteOrder.BIG_ENDIAN);
        } else {
            log.warning("unrecognized BYTEORDER attribute value : " + orderStr);

        }
    }

    private void processBrickTypes(List<Integer> types) {
        //only handle first type

        int code = types.get(0);
        switch (code) {
            case 0:
                setDataType(DataType.BYTE);
                break;
            case 1:
                setDataType(DataType.SHORT);
                break;
            case 3:
                setDataType(DataType.FLOAT);
                break;
            case 5:
                setDataType(DataType.COMPLEX);
                break;
            default:
                log.severe("unrecognized AFNI data code: " + code);
                throw new IllegalArgumentException("Illegal AFNI data code " + code);


        }
    }


    private void processDatasetRank(List<Integer> rank) {
        int dim1 = rank.get(0);
        if (dim1 != 3) {
            throw new IllegalArgumentException("AFNI attribute DATASET_RANK[0] must be 3");
        }

        int dim2 = rank.get(1);
        setNumImages(dim2);
    }

    private void processOrientSpecific(List<Integer> codes) {
        if (codes.size() != 3) {
            throw new IllegalArgumentException("AFNI attribute ORIENT_SPECIFIC must have three entries");
        }

        AnatomicalAxis[] axes = new AnatomicalAxis[3];
        for (int i = 0; i < axes.length; i++) {
            int code = codes.get(i);
            switch (code) {
                case 0:
                    axes[i] = AnatomicalAxis.RIGHT_LEFT;
                    break;
                case 1:
                    axes[i] = AnatomicalAxis.LEFT_RIGHT;
                    break;
                case 2:
                    axes[i] = AnatomicalAxis.POSTERIOR_ANTERIOR;
                    break;
                case 3:
                    axes[i] = AnatomicalAxis.ANTERIOR_POSTERIOR;
                    break;
                case 4:
                    axes[i] = AnatomicalAxis.INFERIOR_SUPERIOR;
                    break;
                case 5:
                    axes[i] = AnatomicalAxis.SUPERIOR_INFERIOR;
                    break;
                default:
                    throw new IllegalArgumentException("unrecognized code " + code + " for AFNI attribure ORIENT_SPECIFIC");
            }

        }

        Anatomy3D ret = Anatomy3D.matchAnatomy(axes[0], axes[1], axes[2]);
        if (ret == null) {
            throw new IllegalArgumentException("Illegal Axis configuration in AFNI attriute ORIENT_SPECIFIC");
        } else {
            setAnatomy(ret);
        }
    }

    private void processAttribute(AFNIAttributeKey key, AFNIAttribute attribute) {
        AFNIAttribute.IntegerAttribute iattr;
        AFNIAttribute.FloatAttribute fattr;
        AFNIAttribute.StringAttribute sattr;

        switch (key) {

            case BRICK_FLOAT_FACS:
                break;
            case BRICK_KEYWORDS:
                break;
            case BRICK_LABS:
                break;
            case BRICK_STATS:
                break;
            case BRICK_TYPES:
                iattr = (AFNIAttribute.IntegerAttribute) attribute;
                processBrickTypes(iattr.getData());
                break;
            case BYTEORDER_STRING:
                sattr = (AFNIAttribute.StringAttribute) attribute;
                processByteOrder(sattr.getData());
                break;
            case DATASET_DIMENSIONS:
                iattr = (AFNIAttribute.IntegerAttribute) attribute;
                processDimensions(iattr.getData());
                break;
            case DATASET_NAME:
                break;
            case DATASET_RANK:
                iattr = (AFNIAttribute.IntegerAttribute) attribute;
                processDatasetRank(iattr.getData());
                break;
            case DELTA:
                fattr = (AFNIAttribute.FloatAttribute) attribute;
                processSpacing(fattr.getData());
                break;
            case HISTORY_NOTE:
                break;
            case IDCODE_DATE:
                break;
            case IDCODE_STRING:
                break;
            case IJK_TO_DICOM:
                break;
            case LABEL_1:
                break;
            case LABEL_2:
                break;
            case ORIENT_SPECIFIC:
                iattr = (AFNIAttribute.IntegerAttribute) attribute;
                processOrientSpecific(iattr.getData());
                break;
            case ORIGIN:
                fattr = (AFNIAttribute.FloatAttribute) attribute;
                processOrigin(fattr.getData());
            case SCENE_DATA:
                break;
            case TYPESTRING:
                break;
        }
    }
}
