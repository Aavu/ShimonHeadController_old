package ShimonHeadController;

import ShimonHeadController.OSCHead;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ShimonOSCHeadController headController = new ShimonOSCHeadController();
//        Scanner myObj = new Scanner(System.in);
//        String x = "";
        headController.listen();
//        headController.breathe(0.1f, 0.1f);
//        while (!x.equals("-1")) {
//            x = myObj.nextLine();
//            System.out.println(Integer.parseInt(x));
//            headController.listen(Integer.parseInt(x));
//        }
//        headController.breathe(0.1f, 0.1f);
    }
}
