package main.org.usfirst.frc.team1640.robot.intake.separate;

import main.org.usfirst.frc.team1640.intake.Intake;


public class DefaultIntakeSeparately implements IntakeSeparatelyStrategy {
	private Intake intake;
	
	private double leftWheelDrive;
	private double rightWheelDrive;
	private boolean isExtending;
	
	public DefaultIntakeSeparately(Intake intake) {
		this.intake = intake;
	}

	@Override
	public void init() {
		isExtending = intake.getExtending();
		leftWheelDrive = 0;
		rightWheelDrive = 0;
	}

	@Override
	public void execute() {
		intake.setLeftWheelDrive(leftWheelDrive);
		intake.setRightWheelDrive(rightWheelDrive);
		intake.setExtending(!isExtending);
	}

	@Override
	public void end() {
		intake.setLeftWheelDrive(0);
		intake.setRightWheelDrive(0);
	}

	@Override
	public void setExtending(boolean isExtending) {
		this.isExtending = isExtending;
	}

	@Override
	public boolean getExtending() {
		return isExtending;
	}

	@Override
	public double getLeftWheelCurrent() {
		return intake.getLeftWheelCurrent();
	}

	@Override
	public double getRightWheelCurrent() {
		return intake.getLeftWheelCurrent();
	}

	@Override
	public void setLeftWheelDrive(double drive) {
		leftWheelDrive = drive;
	}

	@Override
	public void setRightWheelDrive(double drive) {
		rightWheelDrive = drive;
	}
}
