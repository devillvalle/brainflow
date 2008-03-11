package com.brainflow.core.mask;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.image.data.IImageData;
import com.brainflow.image.data.ImageData;

import java.util.Map;
import java.util.HashMap;

import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 6:14:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class VariableSubstitution extends AnalysisAdapter {

    private IImageDisplayModel model;


    @Testable
    public VariableSubstitution(IImageDisplayModel model) {
        this.model = model;
    }



    private int mapIndex(String varName) {
        if (!varName.toUpperCase().startsWith("V")) {
            throw new IllegalArgumentException("illegal variable name : " + varName);
        }

        String numberPart = varName.substring(1);
        return Integer.parseInt(numberPart) -1 ;

    }

    public void caseConstantNode(ConstantNode node) {
        node.replaceBy(new ImageDataNode(ImageData.createConstantData(node.getValue(), model.getSelectedLayer().getData().getImageSpace())));
        
    }

    public void caseVariableNode(VariableNode node) {

        int index = mapIndex(node.getVarName());

        if (index < 0 || (index > model.getNumLayers() - 1) ) {
            throw new IllegalArgumentException("illegal layer index " + index);
        }

        IImageData data = model.getLayer(index).getData();
        node.replaceBy(new ImageDataNode(data));
    }




}
