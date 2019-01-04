package main.org.usfirst.frc.team1640.vision;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import edu.wpi.first.wpilibj.Timer;
import main.org.usfirst.frc.team1640.constants.vision.ConnectionConstants;
import main.org.usfirst.frc.team1640.vision.messages.HeartbeatMessage;
import main.org.usfirst.frc.team1640.vision.messages.OffWireMessage;
import main.org.usfirst.frc.team1640.vision.messages.VisionMessage;
import main.org.usfirst.frc.team1640.vision.messages.VisionUpdate;

/**
 * A Thread that Maintain a Connection with A Specific Instance of a VisionConnection
 * ex. A Connected Android Phone would hopefully (if nothing changes) be Com. Through One of These
 * Derived from Team 254 2017 Code
 */
public class ServerThread extends CatchingThread
{
	private VisionServer visionServer;
	private Socket socket;
	private boolean useJavaTime;
	private double lastMessageReceivedTime;

	/**
	 * Creates the ServerThread (A Connection to an AndroidPhone) Object
	 * @param server - The VisionServer that this ServerThread Reports to
	 * @param socket - The Socket that this should Listen to
	 * @param shouldUseJavaTime - Which Timing Scheme Should Be Used (rio FPGA or Java)
	 */
    public ServerThread(VisionServer server, Socket exSocket, boolean shouldUseJavaTime) 
    {
    	visionServer = server;
        socket = exSocket;
        useJavaTime = shouldUseJavaTime;
        lastMessageReceivedTime = 0;
    }
    
    /**
     * Main Thread Loop - Operates Connection, Data Transfer, and Storing
     */
    @Override
    public void runWithCatch() 
    {
        if (socket == null) 
            return;
        
        try 
        {
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[2048];
            int read;
            while (socket.isConnected() && (read = is.read(buffer)) != -1) 
            {
            	double timestamp = getTimestamp();
                lastMessageReceivedTime = timestamp;
                String messageRaw = new String(buffer, 0, read);
                String[] messages = messageRaw.split("\n");
                for (String message : messages) 
                {
                    OffWireMessage parsedMessage = new OffWireMessage(message);
                    if (parsedMessage.isValid()) 
                    {
                        handleMessage(parsedMessage, timestamp);
                    }
                }
            }
            System.out.println("Socket disconnected");
        } 
        catch (IOException e) 
        {
            System.err.println("Could not talk to socket");
        }
        
        if (socket != null) 
        {
            try 
            {
                socket.close();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * How to Deal With a Given Vision Message (based on Type)
     * @param message - The VisionMessage in Question
     * @param timestamp - The Recently-Generated Timestamp (Current Time)
     */
    public void handleMessage(VisionMessage message, double timestamp) 
    {
        if (message.getType().equals("targets")) 
        {
            VisionUpdate update = VisionUpdate.generateFromJsonString(timestamp, message.getMessage());
            visionServer.addVisionUpdate(update);
        }
        else if (message.getType().equals("heartbeat")) 
        {
            send(HeartbeatMessage.getInstance());
        }
        else
        {
        	System.err.println("Vision ServerThread Message Broken");
        }
    }

    /**
     * Passes VisionMessages Back to the Android
     * @param message - VisionMessage to be Passed
     */
    public void send(VisionMessage message) 
    {
        String toSend = message.toJson() + "\n";
        if (socket != null && socket.isConnected()) {
            try 
            {
                OutputStream os = socket.getOutputStream();
                os.write(toSend.getBytes());
            } 
            catch (IOException e) 
            {
                System.err.println("VisionServer: Could not send data to socket");
            }
        }
    }

    /**
     * Returns the Time of the Last Received Vision Message
     * @return The Time of the Last Received Vision Message
     */
    public double getLastMessageTimestamp()
    {
    	return lastMessageReceivedTime;
    }
    
    /*
    public boolean isConnectionAlive() 
    {
        return m_socket != null && m_socket.isConnected() && !m_socket.isClosed();
    }
    */
    
    /**
     * Gets a Timestamp from Either Java or the roboRIO FPGA
     * @return Timestamp as a double
     */
    private double getTimestamp() 
    {
        if (useJavaTime)
            return System.currentTimeMillis();
        else
            return Timer.getFPGATimestamp() * 1000;
    }
}
