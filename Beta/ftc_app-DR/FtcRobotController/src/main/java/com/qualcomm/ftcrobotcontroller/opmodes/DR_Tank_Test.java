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
public class DR_Tank_Test extends OpMode{

    public float accelX = 0;
    public float accelY = 0;
    public float accelZ = 0;

    public float deltaX;
    public float deltaY;
    public float deltaZ;

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    //DcMotor leftLever;
    //DcMotor rightLever;

    Servo catLeft;
    Servo catRight;
    ColorSensor RGBSensor;

    //ColorSensor colorSensor;

    double catLeftPosition  = 0.909;
    double catLeftDelta = 0.002;
    double catRightPosition  = 0.005;
    double catRightDelta = 0.002;
    //Servo plowLeft;
    //Servo plowRight;
    //Servo plowInOut;
    //Servo armColorSensor;

    //double plowDelta = 0.001;
    //double plowDeltaLeft = plowDelta;
    //double plowDeltaRight = -plowDelta;
    //double plowPositionLeft = 0.5;
    //double plowPositionRight = 0.5;
    //double inOutPosition = 0.0;
    //double inOutDelta = 0.005;

    int rev = 3;
    //double armColorSensorPosition = 0.8;

    boolean speed_mode;
    //Initialize the Accelerometer
    private SensorManager mSensorManager;
    private Sensor accelerometer;



    public DR_Tank_Test() {

    }
    public void init() {

        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        //motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        //motorLeftFront.setDirection(DcMotor.Direction.REVERSE);

        catLeft = hardwareMap.servo.get("CatLeft");
        catLeft.setPosition(0.886);
        catLeftPosition = 0.886;

        catRight = hardwareMap.servo.get("CatRight");
        catRight.setPosition(0.09);
        catRightPosition = 0.09;

        //plowLeft = hardwareMap.servo.get("Left_Plow");
        //plowRight = hardwareMap.servo.get("Right_Plow");
        //plowInOut = hardwareMap.servo.get("InOut_Plow");

        //leftLever = hardwareMap.dcMotor.get("Left_Lever");
        //rightLever = hardwareMap.dcMotor.get("Right_Lever");
        //colorSensor = hardwareMap.colorSensor.get("Color_Sensor");
        //plowLeft.setPosition(0.23137255);
        //plowRight.setPosition(0.64705884);
        //plowInOut.setPosition(.455);
        //plowPositionLeft = 0.7697843;
        //plowPositionRight = 0.11372549;



        speed_mode = true;
        //plowLeft.setPosition(0.7607843);
        //plowRight.setPosition(0.11372549);
        //plowPositionLeft = 0.76;
        //plowPositionRight = 0.11;

        //mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        //accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    public void start() {
        //mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //}

        //}
        // @Override
        //public void onAccuracyChanged(Sensor sensor, int accuracy) {
         }
        public void loop() {
            //plowPositionLeft = Range.clip(plowPositionLeft, 0.10, 0.80);
            //plowPositionRight = Range.clip(plowPositionRight, 0.10, 0.8);
            //inOutPosition = Range.clip(inOutPosition, 0.18, 0.78);

            catLeftPosition = Range.clip(catLeftPosition, 0.114, 0.886);
            catRightPosition = Range.clip(catRightPosition, 0.09, 0.862);

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

            throttleLB = Range.clip(throttleLB, -1.0, 1.0);
            throttleRB = Range.clip(throttleRB, -1.0, 1.0);
            throttleLF = Range.clip(throttleLF, -1.0, 1.0);
            throttleRF = Range.clip(throttleRF, -1.0, 1.0);

            float leftLeverThrottle = gamepad2.left_stick_y;
            float rightLeverThrottle = gamepad2.right_stick_y;

            leftLeverThrottle = Range.clip(leftLeverThrottle, -1, 1);
            rightLeverThrottle = Range.clip(rightLeverThrottle, -1, 1);

            //leftLever.setPower(leftLeverThrottle);
            //rightLever.setPower(-rightLeverThrottle);

            motorLeftRear.setPower(-throttleLB);
            motorLeftFront.setPower(-throttleLF);
            motorRightRear.setPower(-throttleRB);
            motorRightFront.setPower(-throttleRF);


            if (gamepad2.dpad_up) {
                catLeftPosition -= catLeftDelta;
            }
            if (gamepad2.dpad_down) {
                catLeftPosition += catLeftDelta;
            }

            if (gamepad2.y) {
                catRightPosition += catRightDelta;
            }
            if (gamepad2.a) {
                catRightPosition -= catRightDelta;
            }
            /*
            if (gamepad2.right_bumper) {
                plowPositionLeft -= plowDeltaLeft;
                plowPositionRight -= plowDeltaRight;
            }
            if (gamepad2.right_trigger > 0.2) {
                plowPositionLeft += plowDeltaLeft;
                plowPositionRight += plowDeltaRight;
            }
            if (gamepad2.left_bumper) {
                inOutPosition += inOutDelta;
            }
            if (gamepad2.left_trigger > 0.2) {
                inOutPosition -= inOutDelta;
            }
            */
        /*if (gamepad2.y)
        {
            plowPositionLeft = 0.61;
            plowPositionRight = 0.39;
        }
        if (gamepad2.a) {
            plowPositionLeft = 0.05;
            plowPositionRight = 0.95;
        }

            if (gamepad2.b) {
                inOutPosition = 0.001;
            }
            if (gamepad2.x) {
                inOutPosition = 0.83;
            }
            */
            //plowInOut.setPosition(inOutPosition);
            //plowLeft.setPosition(plowPositionLeft);
            //plowRight.setPosition(plowPositionRight);
            //armColorSensor.setPosition(armColorSensorPosition);

            catLeft.setPosition(catLeftPosition);
            catRight.setPosition(catRightPosition);

                /*telemetry.addData("Left Rear:"
                , +motorLeftRear.getPower()
                + motorLeftRear.getCurrentPosition());
                telemetry.addData("Right Rear:"
                ,   +motorRightRear.getPower()
                    + motorRightRear.getCurrentPosition());
                telemetry.addData("Left Front:"
                ,   + motorLeftFront.getPower ()
                    + motorLeftFront.getCurrentPosition());
                telemetry.addData("Right Front:"
                ,   + motorRightFront.getPower()
                    + motorRightFront.getCurrentPosition()); */
            //telemetry.addData("Left Plow:", plowLeft.getPosition());
            //telemetry.addData("Right Plow:", plowRight.getPosition());
            //telemetry.addData("InOut Plow:", plowInOut.getPosition());
            telemetry.addData("CatLeft:", catLeft.getPosition());
            telemetry.addData("CatRight:", catRight.getPosition());
            telemetry.addData("Throttle LB", throttleLB);
            telemetry.addData("Throttle LF", throttleLF);
            telemetry.addData("Throttle RB", throttleRB);
            telemetry.addData("Throttle RF", throttleRF);
            telemetry.addData("rev", rev);
            //telemetry.addData("RBG",RGBSensor.argb());


            //telemetry.addData("X Accelerometer", accelX);
            //telemetry.addData("Y Accelerometer", accelY);
            //telemetry.addData("Z Accelerometer", accelZ);

        }
    //possible issue
//@Override
        /*public void onSensorChanged(SensorEvent event)
        {
            telemetry.addData ("ASensor", "Sensor Hit");
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                 //get the change of the x,y,z values of the accelerometer
                deltaX = Math.abs(lastX - event.values[0]);
                deltaY = Math.abs(lastY - event.values[1]);
                deltaZ = Math.abs(lastZ - event.values[2]);
                 //if the change is below 2, it is just plain noise
                if (deltaX < 2)
                    deltaX = 0;
                if (deltaY < 2)
                    deltaY = 0;
                if (deltaZ < 2)
                    deltaZ = 0;

                accelX = deltaX;
                accelY = deltaY;
                accelZ = deltaZ;
            }
            */

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
