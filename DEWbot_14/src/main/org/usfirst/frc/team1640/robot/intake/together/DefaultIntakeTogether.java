package main.org.usfirst.frc.team1640.robot.intake.together;

import main.org.usfirst.frc.team1640.robot.intake.separate.IntakeSeparatelyStrategy;
import main.org.usfirst.frc.team1640.utilities.timers.ElapsedTimer;

public class DefaultIntakeTogether implements IntakeTogetherStrategy {
	// Drives the intake wheels
	
	private IntakeSeparatelyStrategy intakeStrat;
	
	private double percentLeft;
	private double percentRight;
	
	private ElapsedTimer timer;
	private boolean holdCube;
	
	public DefaultIntakeTogether(IntakeSeparatelyStrategy intakeStrat, double percentLeft, double percentRight) {
		this.intakeStrat = intakeStrat;
		this.percentLeft = percentLeft;
		this.percentRight = percentRight;
		
		timer = new ElapsedTimer();
	}
	
	@Override
	public void setExtending(boolean isExtending) {
		intakeStrat.setExtending(isExtending);
	}

	@Override
	public boolean getExtending() {
		return intakeStrat.getExtending();
	}

	@Override
	public double getLeftWheelCurrent() {
		return intakeStrat.getLeftWheelCurrent();
	}

	@Override
	public double getRightWheelCurrent() {
		return intakeStrat.getRightWheelCurrent();
	}

	@Override
	public void init() {
		intakeStrat.init();
	}

	@Override
	public void execute() {
		intakeStrat.execute();
	}

	@Override
	public void end() {
		intakeStrat.end();
	}

	@Override
	public void setWheelDrive(double drive) {
		if (drive >= 0) {
			//new code
			double leftDrive, rightDrive;
//			if(drive == 0.3) {
//				System.out.println("holding intake");
//				leftDrive = drive;
//				rightDrive = drive;	
//			}else {
//				leftDrive = percentLeft*drive;
//				rightDrive = percentRight*drive;
//			}
			if(getExtending() || !holdCube) {
				leftDrive = percentLeft*drive;
				rightDrive = percentRight*drive;
				holdCube = holdCube || drive > 0;
			}
			else{
				leftDrive = Math.max(percentLeft*drive, 0.15);
				rightDrive = Math.max(percentRight*drive, 0.15);
			}
			
			//old code
//			double leftDrive = Math.max(percentLeft*drive, 0.15);
//			double rightDrive = Math.max(percentRight*drive, 0.15);
			
			intakeStrat.setLeftWheelDrive(rightDrive);// left is right
			intakeStrat.setRightWheelDrive(leftDrive);
		}
		else {
//			intakeStrat.setLeftWheelDrive(-1);
//			intakeStrat.setRightWheelDrive(-1);
			
			intakeStrat.setLeftWheelDrive(0.4*drive);
			intakeStrat.setRightWheelDrive(0.4*drive);
			timer.restart();
			holdCube = false;
		}
	}

}
