package main.org.usfirst.frc.team1640.vision.messages;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;

/**
 * Message Interpreter Class - Derived from Team 254 2017 Code
 */
public class OffWireMessage extends VisionMessage
{
    private String mType;
    private String mMessage = "{}";
    private boolean mValid = false;

    /**
     * Creates a New OffWireMessage, Which Interprets A Received Message
     * Gives Type and Content Dynamically Based Off message Content
     * @param message - The Received Message
     */
    public OffWireMessage(String message)
    {
    	JSONParser parser = new JSONParser();
        try 
        {
            JSONObject json = (JSONObject) parser.parse(message);
            mType = (String) json.get("type");
            mMessage = (String) json.get("message");
            mValid = true;
        }
        catch (ParseException jsonex)
        {
            jsonex.printStackTrace();
        }
    }

    /**
     * Did the JSON Reader Run to Completion
     * @return mValid - If the JSON Reader Ran to Completion
     */
    public boolean isValid()
    {
        return mValid;
    }

    /**
     * Gets the Type Of the String Message
     * @return mType - Type from String (if possible, otherwise "unknown")
     */
    @Override
    public String getType()
    {
        return mType == null ? "unknown" : mType;
    }

    /**
     * Gets the Content of the String Message
     * @return mMessage - Message from String
     */
    @Override
    public String getMessage()
    {
        return mMessage;
    }
}
