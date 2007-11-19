package com.brainflow.application.presentation.binding;

import net.java.dev.properties.BaseProperty;
import net.java.dev.properties.Property;
import net.java.dev.properties.RProperty;
import net.java.dev.properties.container.BeanContainer;
import net.java.dev.properties.events.PropertyListener;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Nov 18, 2007
 * Time: 11:41:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertyConnector<T> {

    private Property<T> prop1;

    private Property<T> prop2;

    public PropertyConnector(Property<T> p1, Property<T> p2) {
        prop1 = p1;
        prop2 = p2;

        initListeners();

    }

    private void initListeners() {
        BeanContainer.get().addListener(prop1, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {              
                RProperty<T> rprop = (RProperty<T>) prop;

                if (!rprop.get().equals(prop2.get())) {
                    prop2.set(rprop.get());
                }
            }
        });

         BeanContainer.get().addListener(prop2, new PropertyListener() {
            public void propertyChanged(BaseProperty prop, Object oldValue, Object newValue, int index) {
                RProperty<T> rprop = (RProperty<T>) prop;

                if (!rprop.get().equals(prop1.get())) {
                    prop1.set(rprop.get());
                }
            }
        });



    }


}
