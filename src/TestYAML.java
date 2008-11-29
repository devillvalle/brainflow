import org.jvyaml.YAML;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Jul 6, 2008
 * Time: 10:23:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestYAML {

    public static void main(String[] args) {
        try {
            HashMap map = (HashMap)YAML.load(new FileReader("c:/javacode/googlecode/brainflow/test.yml"));
            //System.out.println(map);
            for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
                Object obj = iter.next();
                //System.out.println(obj);
                //System.out.println(obj.getClass());
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
