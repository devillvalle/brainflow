package test.com.brainflow.core.mask;

import com.brainflow.core.mask.*;
import com.brainflow.core.IImageDisplayModel;
import com.brainflow.core.ImageLayer;
import com.brainflow.core.ImageDisplayModel;
import com.brainflow.application.TestUtils;
import org.junit.*;
import jfun.parsec.Parser;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: Brad Buchsbaum
 * Date: Mar 4, 2008
 * Time: 6:53:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestVariableSubstitution {

    IImageDisplayModel model;

    BinaryExpressionParser parser;

    public TestVariableSubstitution() {
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
    public void testVariableSubstitution1() {

        Parser<INode> parser = new BinaryExpressionParser().createParser();
        INode node = parser.parse("V1 and V2");
        VariableSubstitution sub = new VariableSubstitution(model);
        sub.start(node);

        assertTrue("node is instanceof ComparisonNode", node instanceof ComparisonNode);
        ComparisonNode cnode = (ComparisonNode) node;
        assertTrue(cnode.left() instanceof ImageDataNode);
        assertTrue(cnode.right() instanceof ImageDataNode);

        ImageDataNode inode_left = (ImageDataNode) cnode.left();
        ImageDataNode inode_right = (ImageDataNode) cnode.right();

        assertTrue("left node is layer 1", model.getLayer(0).getData() == inode_left.getData());
        assertTrue("right node is layer 2", model.getLayer(1).getData() == inode_right.getData());

    } // testVariableSubstitution()

    @Test
    public void testVariableSubstitutionWithConstant() {
        Parser<INode> parser = new BinaryExpressionParser().createParser();
        INode node = parser.parse("V1 and V2 and 27");
        VariableSubstitution sub = new VariableSubstitution(model);
        sub.start(node);

        assertTrue("node is instanceof ComparisonNode", node instanceof ComparisonNode);
        ComparisonNode cnode = (ComparisonNode) node;

        assertTrue(cnode.left() instanceof ComparisonNode);
        assertTrue(cnode.right() instanceof ImageDataNode);

        ComparisonNode left = (ComparisonNode) cnode.left();
        ImageDataNode right = (ImageDataNode) cnode.right();


        assertTrue(left.left().toString(), left.left() instanceof ImageDataNode);
        assertTrue(left.right().toString(), left.right() instanceof ImageDataNode);

        assertEquals(right.getData().value(0), 27, .0001);

    }


    @Test
    public void testVariableSubstitution() {
        fail(); // @todo - implement
    } // testVariableSubstitution()
}
