package main.org.usfirst.frc.team1640.vision.messages;

/**
 * Empty Message Periodically Sent So Both Sides Know the State of the Connection - Derived from Team 254 2017 Code
 */
public class HeartbeatMessage extends VisionMessage
{
	private static final String HEARTBEAT_TYPE = "heartbeat";
	private static final String HEARTBEAT_MESSAGE = "{}";
	
    private static HeartbeatMessage sInst = null;

    /**
     * Singleton Instance Method
     * Message Type is "heartbeat" and the Content is and Empty JSON String
     * @return Instance of HeartbeatMessage
     */
    public static HeartbeatMessage getInstance()
    {
        if (sInst == null)
        {
            sInst = new HeartbeatMessage();
        }
        return sInst;
    }

    /**
     * The 'type' of the Message - In HeartbeatMessage it is always "heartbeat"
     * @return 'type' "heartbeat"
     */
    @Override
    public String getType()
    {
        return HEARTBEAT_TYPE;
    }

    /**
     * The content of the Message - In HeartbeatMessage it is always an empty JSON
     * @return An Empty JSON Content string
     */
    @Override
    public String getMessage()
    {
        return HEARTBEAT_MESSAGE;
    }
}
