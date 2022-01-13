// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import static frc.robot.Constants.Drivetrain_Gearing;
import static frc.robot.Constants.Left_Follower_ID;
import static frc.robot.Constants.Left_Leader_ID;
import static frc.robot.Constants.Pigeon_ID;
import static frc.robot.Constants.Right_Follower_ID;
import static frc.robot.Constants.Right_Leader_ID;
import static frc.robot.Constants.Robot_Width;
import static frc.robot.Constants.Wheel_Radius;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.configurations.DriveTrainConfiguration;
import frc.robot.wrappers.PilotFX;
import frc.robot.wrappers.PilotPigeon;

public class DriveBaseSubsystem extends SubsystemBase {
    private final PilotFX m_rightLeader = new PilotFX(Right_Leader_ID);
    private final PilotFX m_leftLeader = new PilotFX(Left_Leader_ID);
    private final PilotFX m_rightFollower = new PilotFX(Right_Follower_ID);
    private final PilotFX m_leftFollower = new PilotFX(Left_Follower_ID);
    private final PilotPigeon m_pigeon = new PilotPigeon(Pigeon_ID);

    private final DifferentialDriveKinematics m_kinematics =
            new DifferentialDriveKinematics(Units.inchesToMeters(Robot_Width));
    private final DifferentialDriveOdometry m_Odometry =
            new DifferentialDriveOdometry(new Rotation2d(m_pigeon.getYaw()));

    private ChassisSpeeds m_currentSpeeds = new ChassisSpeeds(0, 0, 0);
    private Pose2d m_currentPose = new Pose2d();

    /** Creates a new ExampleSubsystem. */
    public DriveBaseSubsystem() {
        addChild("Right Leader", m_rightLeader);
        addChild("Left Leader", m_leftLeader);
        addChild("Right Follower", m_rightFollower);
        addChild("Left Follower", m_leftFollower);
        addChild("Chassis IMU", m_pigeon);

        DriveTrainConfiguration.configure(m_rightLeader, m_rightFollower);
        DriveTrainConfiguration.configure(m_leftLeader, m_leftFollower);
    }

    public void arcadeDrive(double throttle, double turn) {
        m_rightLeader.set(ControlMode.PercentOutput, throttle + turn);
        m_leftLeader.set(ControlMode.PercentOutput, throttle - turn);
    }

    @Override
    public void periodic() {
        /* Update the odometry */
        m_currentPose =
                m_Odometry.update(
                        m_pigeon.getRotation2d(), getDistance(m_leftLeader), getDistance(m_rightLeader));
        /* Get wheel speeds from motors */
        var wheelSpeeds =
                new DifferentialDriveWheelSpeeds(getSpeed(m_rightLeader), getSpeed(m_leftLeader));
        /* Convert to chassis speeds */
        m_currentSpeeds = m_kinematics.toChassisSpeeds(wheelSpeeds);
    }

    @Override
    public void simulationPeriodic() {}

    public Pose2d getPose() {
        return m_currentPose;
    }

    public ChassisSpeeds getSpeeds() {
        return m_currentSpeeds;
    }

    public void resetOdometry(Pose2d pose) {
        m_Odometry.resetPosition(pose, m_pigeon.getRotation2d());
        m_pigeon.setYaw(pose.getRotation().getDegrees());
        m_leftLeader.setPosition(0);
        m_rightLeader.setPosition(0);
    }

    private static double getDistance(PilotFX motor) {
        /* Start with Rotations */
        double r = motor.getRotations();
        /* Gear it down according to gearing parameter */
        r *= Drivetrain_Gearing;
        /* Turn into meters */
        double m = r * Units.inchesToMeters(Wheel_Radius) * 2 * Math.PI;
        /* Return meters */
        return m;
    }

    private static double getSpeed(PilotFX motor) {
        /* Start with RPM */
        double rpm = motor.getRPM();
        /* Gear it down according to gearing parameter */
        rpm *= Drivetrain_Gearing;
        /* Turn into meters per minute */
        double mpm = rpm * Units.inchesToMeters(Wheel_Radius) * 2 * Math.PI;
        /* Turn into meters per second */
        double mps = mpm / 60;
        /* Return meters per second */
        return mps;
    }
}
