/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package main.org.usfirst.frc.team1640.robot;

import main.org.usfirst.frc.team1640.robot.context.IRobotContext;
import main.org.usfirst.frc.team1640.robot.context.RobotContext;
import main.org.usfirst.frc.team1640.robot.states.AutonRobotState;
import main.org.usfirst.frc.team1640.robot.states.DisabledRobotState;
import main.org.usfirst.frc.team1640.robot.states.RobotState;
import main.org.usfirst.frc.team1640.robot.states.TeleopRobotState;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private IRobotContext robotContext;
	private RobotState autonRobotState, teleopRobotState, disabledRobotState;

	/**
	 * This function is run when the robot is first started up.
	 */
	@Override
	public void robotInit() {
		robotContext = new RobotContext();
		
		autonRobotState = new AutonRobotState(robotContext);
		teleopRobotState = new TeleopRobotState(robotContext);
		disabledRobotState = new DisabledRobotState(robotContext);
	}

	/**
	 * This function is called at the beginning of autonomous.
	 */
	@Override
	public void autonomousInit() {
		try {
			autonRobotState.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		try {
			autonRobotState.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function is called at the beginning of teleop.
	 */
	@Override
	public void teleopInit() {
		try {
			teleopRobotState.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function is called periodically during teleop.
	 */
	@Override
	public void teleopPeriodic() {
		try {
			teleopRobotState.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function is called as soon as the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		try {
			disabledRobotState.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This function is called periodically while the robot is disabled.
	 */
	@Override
	public void disabledPeriodic() {
		try {
			disabledRobotState.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void robotPeriodic() {
		try {
			robotContext.getDriverController().update();
			robotContext.getOperatorController().update();
		} catch (Exception e) {
			e.printStackTrace();
		};
	}
	
	@Override
	public void testInit() {
		robotContext.getAirCompressor().start();
		System.out.println("Compressor Started");
	}
}
