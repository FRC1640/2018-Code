package main.org.usfirst.frc.team1640.vision.messages;

import org.json.simple.JSONObject;

/**
 * Abstract Class Framework For the Vision Messages - Derived from Team 254 2017 Code
 */
public abstract class VisionMessage
{
	private static final String JSON_TAG_TYPE = "type";
	private static final String JSON_TAG_MESSAGE = "message";
	
	/**
	 * Abstract Method Requiring Each Message Passed Between Android and Robot to have a String 'type'
	 * @return The String Type of Message
	 */
    public abstract String getType();

    /**
     * Abstract Method Requiring Each Message Passed Between Android and Robot to have a String of JSON content
     * @return The String JSON Content of Message
     */
    public abstract String getMessage();

    /**
     * Procedure for Putting the Message into String JSON
     * @return A Compatible JSON String (Hopefully)
     */
    public String toJson()
    {
        JSONObject j = new JSONObject();
        j.put(JSON_TAG_TYPE, getType());
        j.put(JSON_TAG_MESSAGE, getMessage());
        return j.toString();
    }
}
