package ShimonHeadController;

import ShimonHeadController.innards.debug.Debug;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;

public class Main {
    public static void main(String[] args) {
        Debug.setDebugOn(true);
        Debug.setChannelStatus("HeadMotors", true);
//        ShimonOSCHeadController headController = new ShimonOSCHeadController();
        HeadMotorController head = new HeadMotorController();
        head.init();
//        head.home();
        head.goTo("basePan", 0, 3.0f, 0.2f);
//        headController.launch();
//        headController.breathe(0.1f, 0.05f);
//        try {
//            OSCPortIn receiver = new OSCPortIn(30310);
//            OSCListener listener = (time, message) -> System.out.println("Message received!");
//            receiver.addListener("/head-commands", listener);
//            receiver.startListening();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
