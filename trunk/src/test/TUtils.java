package test;

import org.junit.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: May 17, 2008
 * Time: 1:27:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TUtils {


    public static void assertArrayEquals(float[] f1, float[] f2, float tol) {
        for (int i = 0; i < f1.length; i++) {
            Assert.assertEquals(f1[i], f2[i], tol);
        }
    }

}
