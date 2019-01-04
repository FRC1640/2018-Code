package main.org.usfirst.frc.team1640.vision;

/**
 * Simple Thread Which Has It's Run Method Overridden With a Try-Catch
 * Inspired by Team 254 Code (CrashTrackingRunnable)
 */
public abstract class CatchingThread extends Thread
{
	/**
	 * Standard Method Run By Thread
	 */
	@Override
	public void run()
	{
		try
		{
			runWithCatch();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * Abstract Method Required in Subclasses (Run by run(), surrounded by try-catch)
	 */
	protected abstract void runWithCatch();
}
