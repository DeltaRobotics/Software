package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_Setup extends OpMode {

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    Servo catLeft;
    Servo armVert;
    Servo armGrab;
    Servo leftLever;
    Servo rightLever;

    Servo plowLeft;
    Servo plowRight;


    Servo winchAngle;
    //Servo plowLeft;
    //Servo plowRight;
    //Servo plowInOut;

    ColorSensor RGBSensor;
    OpticalDistanceSensor ODS;

    @Override
    public void init() {
        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);

        catLeft = hardwareMap.servo.get("CatLeft");
        leftLever = hardwareMap.servo.get("Left_Lever");
        leftLever.setPosition(0.019);

        rightLever = hardwareMap.servo.get("Right_Lever");
        rightLever.setPosition(0.878);

        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");

        armVert = hardwareMap.servo.get("Vertical_Arm");
        armVert.setPosition(0.988);

        armGrab = hardwareMap.servo.get("Grabber_Arm");
        armGrab.setPosition(0.878);
        winchAngle = hardwareMap.servo.get("Winch_Angle");

        RGBSensor = hardwareMap.colorSensor.get("BottomColorSensor");
        ODS = hardwareMap.opticalDistanceSensor.get("FrontODS");
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");

        plowLeft.setPosition(0.957);
        plowRight.setPosition(0.047);
        winchAngle.setPosition(0.64);
        catLeft.setPosition(0.7255);

    }

    public void loop() {

        winchAngle.setPosition(0.64);
        plowLeft.setPosition(0.957);
        plowRight.setPosition(0.047);
        catLeft.setPosition(0.7255);
        leftLever.setPosition(0.019);
        rightLever.setPosition(0.878);
        armGrab.setPosition(0.878);
        armVert.setPosition(0.988);


    }


}




