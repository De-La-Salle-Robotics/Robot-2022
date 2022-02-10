// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;

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
    public static final int Right_Leader_ID = 1;
    public static final int Left_Leader_ID = 2;
    public static final int Right_Follower_ID = 4;
    public static final int Left_Follower_ID = 3;
    public static final boolean Left_Side_Inverted = true;
    public static final boolean Right_Side_Inverted = false;

    public static final int Pigeon_ID = 0;

    /* Arm CAN IDs */
    public static final int Arm_Pivot_ID = 5;
    public static final int Arm_Cancoder_ID = 5;

    /* Joystick IDs */
    public static final int Driver_Controller_Port = 0;
    public static final int Operator_Controller_Port = 1;

    /* Joystick constants */
    public static final int Operator_Stow_Button = XboxController.Button.kA.value;
    public static final int Operator_Index_Button = XboxController.Button.kB.value;
    public static final int Operator_Collect_Button = XboxController.Button.kY.value;
    /* Distance between left wheels and right wheels in inches */
    public static final double Robot_Width = 18.0;
    /* Radius of drive wheels in inches */
    public static final double Wheel_Radius = 6.0;
    /* Gearing of drivetrain*/
    public static final double Drivetrain_Gearing = 10.71;

    /* Arm Constants */
    public static final int Stowed_Position = 0;
    public static final int Indexing_Position = 1;
    public static final int Collecting_Position = 2;
    public static final double Arm_Gearbox_Ratio = 25.0 / 1.0;
}
