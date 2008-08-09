/**
 * Created by IntelliJ IDEA.
 * User: Brad
 * Date: Apr 6, 2008
 * Time: 11:07:28 PM
 * To change this template use File | Settings | File Templates.
 */



static main(String[] args) {
    def bf = new com.brainflow.application.toplevel.BrainFlow();
    bf.launch();

    
}


def bf() {
    BrainFlow().getInstance()
}


