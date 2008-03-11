package test.com.brainflow.core.mask;

import com.brainflow.core.mask.MaskSubstitution;
import com.brainflow.core.mask.BinaryExpressionParser;
import com.brainflow.core.mask.INode;
import com.brainflow.core.mask.VariableSubstitution;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.application.TestUtils;
import org.junit.*;
import static org.junit.Assert.*;
import jfun.parsec.Parser;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 11, 2008
 * Time: 8:28:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMaskSubstitution {

    IImageDisplayModel model;

    BinaryExpressionParser parser;


    public TestMaskSubstitution() {
    } // constructor

    @BeforeClass
    public static void unitSetup() {
    } // unitSetup()

    @AfterClass
    public static void unitCleanup() {
    } // unitCleanup()

    @Before
    public void methodSetup() {
        ImageLayer l1 = TestUtils.quickLayer("icbm452_atlas_probability_gray.hdr");
        ImageLayer l2 = TestUtils.quickLayer("icbm452_atlas_probability_white.hdr");

        model = new ImageDisplayModel("test");
        model.addLayer(l1);
        model.addLayer(l2);

    } // methodSetup()

    @After
    public void methodCleanup() {
    } // methodCleanup()

    @Test
    public void testMaskSubstitution() {
        Parser<INode> parser = new BinaryExpressionParser().createParser();
        INode node = parser.parse("V1 and V2 and 27");
        VariableSubstitution sub = new VariableSubstitution(model);
        sub.start(node);

        MaskSubstitution masksub = new MaskSubstitution();
        masksub.start(node);

    } // testInComparison()
}
