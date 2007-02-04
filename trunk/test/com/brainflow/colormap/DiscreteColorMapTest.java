package com.brainflow.colormap;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.awt.*;
import java.util.Iterator;

/**
 * DiscreteColorMap Tester.
 *
 * @author Brad
 * @version $Revision$, $Date$
 * @created January 4, 2007
 * @since 1.0
 */
public class DiscreteColorMapTest {


    LinearColorMap lmap;
    DiscreteColorMap dmap;

    @BeforeTest()
    public void setUp() {
        lmap = new LinearColorMap(0, 255, ColorTable.SPECTRUM);
        dmap = new DiscreteColorMap(lmap);

        Iterator<ColorInterval> iter1 = lmap.iterator();
        Iterator<ColorInterval> iter2 = lmap.iterator();

        while (iter1.hasNext()) {
            ColorInterval ival1 = iter1.next();
            ColorInterval ival2 = iter2.next();
            assert ival1.equals(ival2) : "ival1: " + ival1 + "not equal to " + ival2;
        }

    }

    @Test
    public void testGetInterval() {
        assert lmap.getInterval(0).equals(dmap.getInterval(0));
        assert lmap.getInterval(255).equals(dmap.getInterval(255));
    }

    @Test
    public void testGetColor() {
        assert dmap.getColor(123).equals(dmap.getColor(123));
        assert!dmap.getColor(123).equals(dmap.getColor(4));

    }

    @Test
    public void testSetColor() {
        int oldSize = dmap.getMapSize();
        ColorInterval oldInterval = dmap.getInterval(45);
        dmap.setColor(45, new Color(23, 23, 45));

        assert oldSize == dmap.getMapSize();
        assert oldInterval != dmap.getInterval(45);


    }

    @Test
    public void testGetMapSize() {
        assert dmap.getMapSize() == lmap.getMapSize() : "map size = " + dmap.getMapSize();


    }

    @Test
    public void testEqualizeIntervals() {

        ColorInterval oldfirst = dmap.getInterval(0);

        dmap.equalizeIntervals(0, 100);
        assert dmap.getInterval(0).getSize() == dmap.getInterval(5).getSize();
        assert!(dmap.getInterval(5).getSize() == dmap.getInterval(101).getSize());
        System.out.println("dmap 101 interval size = " + dmap.getInterval(101).getSize());
        System.out.println("dmap 99 interval size = " + dmap.getInterval(99).getSize());
        System.out.println("old first interval " + oldfirst);
        System.out.println("new first interval " + dmap.getInterval(0));

    }

    @Test
    public void testSetMapSize() {
       
    }


}
