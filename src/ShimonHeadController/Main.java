package ShimonHeadController;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCPortIn;

public class Main {
    public static void main(String[] args) {
//        ShimonOSCHeadController headController = new ShimonOSCHeadController();
//        headController.launch();
        try {
            OSCPortIn receiver = new OSCPortIn(30310);
            OSCListener listener = (time, message) -> System.out.println("Message received!");
            receiver.addListener("/head-commands", listener);
            receiver.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
