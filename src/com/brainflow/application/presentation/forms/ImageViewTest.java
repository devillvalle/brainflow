package com.brainflow.application.presentation.forms;

import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageDisplayModel;
import net.java.dev.properties.Property;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.container.ObservableProperty;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Oct 14, 2007
 * Time: 12:24:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageViewTest {

    public final Property<Integer> displayModel = ObservableProperty.create(new Integer(44));

    public final Property<InnerBean> innerBean = ObservableProperty.create(new InnerBean(3.14));
    
    public ImageViewTest() {
        BeanContainer.bind(this);
    }

    public static class InnerBean {
        public final Property<Double> lowValue = ObservableProperty.create(new Double(0));

        InnerBean(double val) {
            BeanContainer.bind(this);
            lowValue.set(val);
        }
    }
}
