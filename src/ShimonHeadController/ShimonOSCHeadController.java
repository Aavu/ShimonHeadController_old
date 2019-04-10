package ShimonHeadController;

import ShimonHeadController.siggraph.ArmPositionClusterer;
import ShimonHeadController.siggraph.headbehaviors.ArmTrackBehavior;
import ShimonHeadController.siggraph.headbehaviors.BasepanControl;
import ShimonHeadController.siggraph.headbehaviors.BreatheBehavior;
import ShimonHeadController.siggraph.headbehaviors.ListenBehavior;
import ShimonHeadController.siggraph.headbehaviors.TriggeredHeadBangBehavior;
import ShimonHeadController.siggraph.OSCReceiver;
import ShimonHeadController.innards.iLaunchable;

/**
 * Created by Guy Hoffman
 * Date: Dec 3, 2009
 * Time: 1:13:32 PM
 */
public class ShimonOSCHeadController implements iLaunchable {

    private DofManager dm;
    private ArmPositionClusterer clusterer;

//    private ArmTrackBehavior armTrackBehavior;
    private BasepanControl basepanControl;
    private TriggeredHeadBangBehavior headBangBehavior;
    private ListenBehavior listenBehavior;
    private BreatheBehavior breathBehavior;

    private HeadMotorController hmc;

//    private final static String ARM_POS_OSC_ADDR = "/arm-positions";
//    private final static int    ARM_POS_OSC_PORT =  30308;
//    private final static String STRIKE_OSC_ADDR = "/mallet-strikes";
//    private final static int    STRIKE_OSC_PORT =  30309;
    private final static String HEAD_CMD_OSC_ADDR = "/head-commands";
    private final static int    HEAD_CMD_OSC_PORT =  30310;

    private static double basepanvel = 1.5;
    private static double basePos = 0.;
    
    private volatile boolean going = false;


    public ShimonOSCHeadController() {

        hmc = new HeadMotorController();
        hmc.init();

        dm = new DofManager(hmc);
        clusterer = new ArmPositionClusterer();

//        armTrackBehavior = new ArmTrackBehavior(dm, clusterer);
        headBangBehavior = new TriggeredHeadBangBehavior(dm);
//        listenBehavior = new ListenBehavior(dm);
        breathBehavior = new BreatheBehavior(dm);
        
      dm.goTo("neckTilt", 0.f, 5f);
      dm.goTo("headTilt", -.4f, 20f);

    }

    public void launch () {


        going = true;
        
        dm.goTo("neckTilt", -.7f, 5f);
        dm.goTo("basePan", -1.f, 20f);
//        listenBehavior.setBasePan(-.9f);
//
        dm.goTo("neckPan", 0.f, 15f);
        dm.goTo("headTilt", -.5f, 10f);
        dm.goTo("basePan", -0.9f);
        dm.goTo("neckTilt", 0.f, 10f);
//
//
//        listenBehavior.start();

        new OSCReceiver(HEAD_CMD_OSC_ADDR, HEAD_CMD_OSC_PORT) {
            @Override
            protected void handleMessage(Object[] args) {

                if (!going)
                     return;

                if (args.length < 1)
                    return;

                // System.out.println(ANSIColorUtils.red("Command  " + args[0]));
                String cmd = (String) args[0];

                for (int i = 1; i < args.length; i++) {
                    // System.out.println("args["+i+"] = " + args[i]);
                }


                if (cmd.equals("LIDS")) {
                    // System.out.println("lids command");


                    dm.goTo("lowerLid", (Float) args[1],4.f);
                    dm.goTo("upperLid", (Float) args[2],4.f);
                }

                else if (cmd.equals("BASEPAN")) {
                     System.out.println("basepan command");
                	System.out.println("basePos diff = "+(basePos-(Float) args[1]));
                	basePos = (Float) args[1];
                	if(args.length==3){//YINGJIA CODE
                		float vel=(Float)args[2];//YINGJIA CODE
                		dm.goTo("basePan", (Float) args[1],vel);//YINGJIA CODE
                	}//YINGJIA CODE
                	else	//YINGJIA CODE
//	                dm.goTo("basePan", (Float) args[1],(float) basepanvel);//GUY CODE
                    dm.goTo("basePan", (Float) args[1],7.f);//GUY CODE
                }

                else if (cmd.equals("SETBASEPANVEL")) {
                     System.out.println("set basepan command: "+(Float)args[1]);

	            	basepanvel = (Float) args[1];
                }

                else if (cmd.equals("BASEPAN2")) {
                    // System.out.println("basepan2 command");

	                dm.goTo("basePan", (Float) args[1],3.f);

                }

                else if (cmd.equals("BREATHING")) {
                  // System.out.println("breathing command");

                //	            armTrackBehavior.stop();
                  headBangBehavior.stop();
                  breathBehavior.start();
                }

                else if (cmd.equals("SETNECK")) {
                   System.out.println("------- set neck command "+(Float)args[1]);

                  breathBehavior.setNeckPosition((Float) args[1]);
                }

                else if (cmd.equals("NECK")) {
                    // System.out.println("neck command");
                	if(args.length==2) //YINGJIA CODE
                    dm.goTo("neckTilt", (Float) args[1],4.f);//GUY CODE
                	else//YINGJIA CODE
                		dm.goTo("neckTilt", (Float) args[1],(Float)args[2]);//YINGJIA CODE
                }
//
//                else if (cmd.equals("NECKPAN")) {
//                    System.out.println("head command");
//
//                    listenBehavior.setNeckPan((Float) args[1]);
//                    listenBehavior.start();
//
//                }
//
                else if (cmd.equals("HEADTILT")) {
                    // System.out.println("head command");

                	if(args.length==2) //YINGJIA CODE
                		dm.goTo("headTilt", (Float) args[1],8.f);//GUY CODE
                	else//YINGJIA CODE
                		dm.goTo("headTilt", (Float) args[1],(Float)args[2]);//YINGJIA CODE

                }
//
                else if (cmd.equals("STOPBREATH")) {
                  breathBehavior.stop();
                }

                else if (cmd.equals("TRACK")) {
                    // System.out.println("tracking query");

                    if (args.length > 2) {
                        trackArms((Integer) args[1] != 0, (Integer) args[2]);
                    }
                    else {
                        trackArms(true, ArmTrackBehavior.DEFAULT_INTERVAL);
                        // System.out.println("tracking is true");
                    }
                }

                else if (cmd.equals("BANG")) {
                    if (args.length > 1) {
                        headBang((Integer) args[1] != 0);
                    }
                    else {
                        headBang(true);
                    }
                }

                else if (cmd.equals("PLAY")) {
                    play();
                }

                else if (cmd.equals("BEAT")) {
                    int length = 960;
                    if (args.length > 1) {
                        if ((Integer)args[1] >300) {
                            length = (Integer) args[1];
                        }
                    }
                    headBangBehavior.beat(length);
                }

            }
        };

    }

    private void headBang(boolean startBang) {
        if (startBang) {
            breathBehavior.stop();
            headBangBehavior.start();
        } else {
            headBangBehavior.stop();
        }
    }

    private void trackArms(boolean doTrack, int interval) {
//        if (doTrack) {
//            armTrackBehavior.setInterval(interval);
////            armTrackBehavior.start();
//            breathBehavior.setPosition(-0.4f, -0.8f);
//            breathBehavior.start();
//            listenBehavior.stop();
//        } else {
//            armTrackBehavior.stop();
//            breathBehavior.stop();
//           listenBehavior.start();
//        }
    }

    public void breathe(float neck, float head)
    {
//        listenBehavior.stop();
        breathBehavior.setPosition(neck, head);
        breathBehavior.start();
    }
    
    public void listen(float pos)
    {

        breathBehavior.stop();
//        armTrackBehavior.stop();
        headBangBehavior.stop();
//        listenBehavior.setBasePan(pos);
//        listenBehavior.start();
    }

    public void listen()
    {
        listen(ListenBehavior.DEFAULT_END_POSITION);
    }

    public void play()
    {

//        armTrackBehavior.start();
//        headBangBehavior.start();
//        listenBehavior.stop();

    }

    public void home()
    {
        hmc.home();
    }

    public void stop() {
//
//        dm.goTo("neckPan", 0.f, 15f);
//        dm.goTo("headTilt", 0.f, 10f);
//        dm.goTo("basePan", 0.f);
//        dm.goTo("neckTilt", 0.f, 10f);
    	
        going = false;

//        armTrackBehavior.stop();
        headBangBehavior.stop();
//        listenBehavior.stop();
        breathBehavior.stop();
        

        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        hmc.zero();

    }

    public void disable() {
        hmc.disableAll();
    }

    public boolean isPlaying()
    {
        return clusterer.isPlaying();
    }
}
