package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by steve.brooks on 9/19/2015.
 */
public class DR_Tank_Test extends OpMode {

    DcMotor motorRight;
    DcMotor motorLeft;
    TouchSensor touchSensor;
    Servo servo1;

    double servo1Position;
    double servo1Delta;

    public DR_Tank_Test() {

    }
    public void init() {

        motorRight = hardwareMap.dcMotor.get ("Drive_Right");
        motorLeft = hardwareMap.dcMotor.get ("Drive_Left");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        touchSensor = hardwareMap.touchSensor.get ("touchSensor");
        servo1 = hardwareMap.servo.get ("servo1");

        servo1Position = 0.2;
        servo1Delta = 0.1;
    }
    public void loop() {
        float throttlel = gamepad1.left_stick_y;
        float throttler = gamepad1.right_stick_y;
        throttlel = Range.clip (throttlel, -1, 1);
        throttler = Range.clip (throttler, -1, 1);

        throttlel = (float)scaleInput(throttlel);
        throttler = (float)scaleInput(throttler);

        motorLeft.setPower(throttlel);
        motorRight.setPower(throttler);

        servo1.setPosition(servo1Position);


        if (touchSensor.isPressed()) {
            servo1Position += servo1Delta;
        }

        telemetry.addData("Left Motor Power:"
                , +motorLeft.getPower());
        telemetry.addData("Right Motor Power:"
               ,  +motorRight.getPower());
        telemetry.addData("Is Pressed", String.valueOf(touchSensor.isPressed()) );
    }
    double scaleInput(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
}
