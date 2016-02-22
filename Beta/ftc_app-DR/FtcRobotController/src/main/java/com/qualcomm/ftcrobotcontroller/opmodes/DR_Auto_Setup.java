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

    //Servo catLeft;
    //Servo catRight;

    boolean flag = false;
    boolean flag1 = false;

    Servo winchAngle;
    //Servo plowLeft;
    //Servo plowRight;
    //Servo plowInOut;

    ColorSensor RGBSensor;
    OpticalDistanceSensor ODS;

    ;

    @Override
    public void init() {
        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);

        //catLeft = hardwareMap.servo.get("CatLeft");
        //catRight = hardwareMap.servo.get("CatRight");
        //plowLeft = hardwareMap.servo.get("Left_Plow");
        //plowRight = hardwareMap.servo.get("Right_Plow");
        //plowInOut = hardwareMap.servo.get("InOut_Plow");
        winchAngle = hardwareMap.servo.get("Winch_Angle");

        RGBSensor = hardwareMap.colorSensor.get("BottomColorSensor");
        ODS = hardwareMap.opticalDistanceSensor.get("FrontODS");
        winchAngle.setPosition(.64);
        //catLeft.setPosition(0.886);
        //catRight.setPosition(0.09);

    }

    public void loop() {

        winchAngle.setPosition(.64);
        //catLeft.setPosition(0.886);
        //catRight.setPosition(0.09);

    }


}




