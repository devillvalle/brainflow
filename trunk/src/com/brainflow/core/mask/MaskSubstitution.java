package com.brainflow.core.mask;

import test.Testable;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:24:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class MaskSubstitution extends AnalysisAdapter {

   @Override
   @Testable
    public void inComparison(ComparisonNode node) {
        System.out.println("node:left " + node.left().getClass());
        System.out.println("node:right " + node.left().getClass());
    }
}
