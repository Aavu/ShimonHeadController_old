package ShimonHeadController.innards.sound.basics;


import ShimonHeadController.innards.iLaunchable;

/**
 * @author marc
 */
public class T_PhantomMidiFile implements iLaunchable
{
	PhantomMidiFile phantomMidiFile;
	
	public void launch()
	{
		try
		{
//			Midi.outputToNewVirtualSource("source");
//			TaskQueue queue= Midi.getMidi().registerInputHandler(new Midi.InputHandler()
//			{
//				public void handle(long hostTime, Message message)
//				{
//					System.out.println(hostTime +" "+message);
//				}
//			});
			
			phantomMidiFile = new PhantomMidiFile("/Developer/Examples/Java/QTJava/QTJavaDemos/media/jazz.mid").play();
			
//			Launcher.getLauncher().registerUpdateable(queue);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
