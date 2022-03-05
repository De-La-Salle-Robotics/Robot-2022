// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.pilotlib.controllerwrappers.DriverController;
import frc.pilotlib.controllerwrappers.OperatorController;

/**
* The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
* constants. This class should not be used for any other purpose. All constants should be declared
* globally (i.e. public static). Do not put anything functional in this class.
*
* <p>It is advised to statically import this class (or one of its inner classes) wherever the
* constants are needed, to reduce verbosity.
*/
public final class Constants {
    /* Drivetrain CAN IDs */
    public static final int Right_Leader_ID = 4;
    public static final int Left_Leader_ID = 2;
    public static final int Right_Follower_ID = 8;
    public static final int Left_Follower_ID = 3;
    public static final boolean Left_Side_Inverted = true;
    public static final boolean Right_Side_Inverted = false;

    public static final int Pigeon_ID = 0;

    /* Arm CAN IDs */
    public static final int Arm_Pivot_ID = 7;
    public static final int Arm_Cancoder_ID = 20;
    public static final int Arm_Intake1_ID = 9;
    public static final int Arm_Intake2_ID = 5;
    /* ARM GPIO IDs */
    public static final int Ball_Detect_Input_ID = 15;

    /* Hopper CAN IDs */
    public static final int Lower_Hopper_ID = 0;
    public static final int Upper_Hopper_ID = 10;

    /* Joystick IDs */
    public static final int Driver_Controller_Port = 0;
    public static final int Operator_Controller_Port = 1;

    /* Driver Joystick constants */
    public static final DriverController.Axis Throttle_Axis = DriverController.Axis.kLeftY;
    public static final DriverController.Axis Wheel_Axis = DriverController.Axis.kRightX;
    /* Operator Joystick constants */
    public static final OperatorController.Axis Manual_Arm_Axis = OperatorController.Axis.kLeftY;
    public static final OperatorController.Button Operator_Collect_Button =
            OperatorController.Button.kY;
    public static final OperatorController.Button Operator_Index_Button =
            OperatorController.Button.kB;
    public static final OperatorController.Button Operator_Stow_Button = OperatorController.Button.kA;

    /* Distance between left wheels and right wheels in inches */
    public static final double Robot_Width = 18.0;
    /* Radius of drive wheels in inches */
    public static final double Wheel_Radius = 6.0;
    /* Gearing of drivetrain*/
    public static final double Drivetrain_Gearing = 10.71;

    /* Arm Constants */
    public static final double Stowed_Position = 90;
    public static final double Indexing_Position = 70;
    public static final double Collecting_Position = 10;
    public static final double Arm_Gearbox_Ratio = 25.0 / 1.0;
    public static final double Arm_Magnet_Offset = 125.0;
}
