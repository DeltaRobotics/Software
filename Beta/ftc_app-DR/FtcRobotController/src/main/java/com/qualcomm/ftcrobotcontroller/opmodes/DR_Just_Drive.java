package com.qualcomm.ftcrobotcontroller.opmodes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Delta on 9/30/2015.
 */
public class DR_Just_Drive extends OpMode{

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;


    int rev = 3;

    boolean speed_mode;




    public DR_Just_Drive() {

    }
    public void init() {

        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        //motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        //motorLeftFront.setDirection(DcMotor.Direction.REVERSE);


        speed_mode = true;
    }

    public void start() {
        //mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //}

        //}
        // @Override
        //public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    public void loop() {

        double throttleLB = gamepad1.left_stick_y;
        double throttleRB = gamepad1.right_stick_y;
        double throttleLF = gamepad1.left_stick_y;
        double throttleRF = gamepad1.right_stick_y;

        if (gamepad1.y) {
            speed_mode = true;
        }
        if (gamepad1.a) {
            speed_mode = false;
        }

        throttleLB = Range.clip(throttleLB, -0.75, 0.75);
        throttleRB = Range.clip(throttleRB, -0.75, 0.75);
        throttleLF = Range.clip(throttleLF, -0.75, 0.75);
        throttleRF = Range.clip(throttleRF, -0.75, 0.75);

        motorLeftRear.setPower(-throttleLB);
        motorLeftFront.setPower(-throttleLF);
        motorRightRear.setPower(-throttleRB);
        motorRightFront.setPower(-throttleRF);


        telemetry.addData("Throttle LB", throttleLB);
        telemetry.addData("Throttle LF", throttleLF);
        telemetry.addData("Throttle RB", throttleRB);
        telemetry.addData("Throttle RF", throttleRF);
        telemetry.addData("rev", rev);


        //telemetry.addData("X Accelerometer", accelX);
        //telemetry.addData("Y Accelerometer", accelY);
        //telemetry.addData("Z Accelerometer", accelZ);

    }

    //  }
    double scaleInputRearHigh(double dVal) {
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
    double scaleInputRearLow(double dVal){
        double[] scaleArray = {0.0, 0.0375, 0.0675, 0.075, 0.09, 0.1125, 0.135, 0.18,
                0.225, 0.27, 0.3225, 0.375, 0.45, 0.54, 0.6375, 0.75, 0.75};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
    double scaleInputFrontHigh(double dVal){
        double[] scaleArray = {0.0, 0.025, 0.045, 0.05, 0.06, 0.075, 0.09, 0.12,
                0.15, 0.18, 0.215, 0.25, 0.30, 0.36, 0.425, 0.5, 0.5};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
    double scaleInputFrontLow(double dVal){
        double[] scaleArray = {0.0, 0.01875, 0.03375, 0.0375, 0.045, 0.05625, 0.0675, 0.09,
                0.1125, 0.135, 0.16125, 0.1875, 0.225, 0.27, 0.31875, 0.375, 0.375};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
}
