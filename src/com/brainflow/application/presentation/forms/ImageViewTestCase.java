package com.brainflow.application.presentation.forms;

import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.PropertyContext;
import net.java.dev.properties.events.PropertyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 14, 2007
 * Time: 12:25:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewTestCase {

    public ImageViewTestCase() {

        ImageViewTest test = new ImageViewTest();
        BeanContainer.get().addListener(test.displayModel, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                System.out.println("prop : " + prop);
                System.out.println("old  : " + oldValue);
                System.out.println("new  : " + newValue);
                System.out.println("index : " + index);
            }
        });
        test.displayModel.set(new Integer(55));

        BeanContainer.get().addListener(test.innerBean.get().lowValue, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                 System.out.println("prop : " + prop);
                System.out.println("old  : " + oldValue);
                System.out.println("new  : " + newValue);
                System.out.println("index : " + index);
            }
        });

         BeanContainer.get().addListener(test.innerBean, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                 System.out.println("prop : " + prop);
                System.out.println("old  : " + oldValue);
                System.out.println("new  : " + newValue);
                System.out.println("index : " + index);
            }
        });



        test.innerBean.get().lowValue.set(34.5);
        test.innerBean.set(new ImageViewTest.InnerBean(214.7));
    }

    public static void main(String[] args) {
        new ImageViewTestCase();
    }
}
