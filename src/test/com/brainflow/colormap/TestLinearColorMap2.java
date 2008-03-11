package test.com.brainflow.colormap;

import com.brainflow.colormap.ColorTable;
import com.brainflow.colormap.LinearColorMap2;
import org.junit.*;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Sep 15, 2007
 * Time: 11:32:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestLinearColorMap2 {

    private static LinearColorMap2 colorMap;

    public TestLinearColorMap2() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
        colorMap = new LinearColorMap2(0.0, 255, 12,230, ColorTable.SPECTRUM);
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
    } // unitCleanup()

    @Before
    public void methodSetup() {
    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testGetColor() {
        assertNotNull(colorMap.getColor(22));
        assertNotNull(colorMap.getColor(222));
        assertNotNull(colorMap.getColor(-55));
        assertNotNull(colorMap.getColor(255));
        assertNotNull(colorMap.getColor(2258));

        assertEquals(colorMap.getColor(237), colorMap.getColor(555));


    } // testGetColor()

    @Test
    public void testGetBinSize() {
        assertEquals(colorMap.getBinSize(), 0.86166, .0001);

    } // testGetBinSize()
}
