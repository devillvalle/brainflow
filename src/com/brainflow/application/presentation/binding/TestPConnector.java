package com.brainflow.application.presentation.binding;

import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 18, 2007
 * Time: 11:49:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestPConnector {

    public final Property<Integer> p1 = ObservableProperty.create(22);

    public final Property<Integer> p2 = ObservableProperty.create(33);

    public TestPConnector() {
        BeanContainer.bind(this);
        PropertyConnector connector = new PropertyConnector(p1,p2);

    }

    public static void main(String[] args) {
        TestPConnector tp = new TestPConnector();
        System.out.println("p1 : " + tp.p1.get());
        System.out.println("p2 : " + tp.p2.get());

        tp.p1.set(66);

        System.out.println("p1 : " + tp.p1.get());
        System.out.println("p2 : " + tp.p2.get());



    }
}
