package com.qualcomm.ftcrobotcontroller.opmodes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by steve.brooks on 9/30/2015.
 */
public class DR_Tank_Test extends OpMode implements SensorEventListener{

    //public float accelX = 0;
    //public float accelY = 0;
   //public float accelZ = 0;

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    Servo plowLeft;
    Servo plowRight;
    Servo plowInOut;
    Servo armColorSensor;

    double plowDelta = 0.01;
    double plowDeltaLeft = plowDelta;
    double plowDeltaRight = -plowDelta;
    double plowPositionLeft = 0.5;
    double plowPositionRight = 0.5;
    double inOutPosition = 0.0;
    double inOutDelta = 0.01;
    double armColorSensorPosition = 0.8;

    boolean speed_mode;
    //Initialize the Accelerometer
    //private SensorManager mSensorManager;
    //private Sensor accelerometer;



    public DR_Tank_Test() {

    }
    public void init() {

        motorLeftRear = hardwareMap.dcMotor.get ("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get ("Drive_Right_Rear");
        motorLeftFront = hardwareMap.dcMotor.get ("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get ("Drive_Right_Front");
        motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);

        plowLeft = hardwareMap.servo.get ("Left_Plow");
        plowRight = hardwareMap.servo.get ("Right_Plow");
        plowInOut = hardwareMap.servo.get ("InOut_Plow");
        armColorSensor = hardwareMap.servo.get ("ColorSensor_arm");

        speed_mode = true;

        //mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        //accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    //public void start() {
    //    mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    //}

       //}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    public void loop() {
        plowPositionLeft = Range.clip(plowPositionLeft,0.02,0.70);
        plowPositionRight = Range.clip(plowPositionRight,0.30,0.97);
        inOutPosition = Range.clip(inOutPosition,0.02,0.98);

        float throttleL = gamepad1.left_stick_y;
        float throttleR = gamepad1.right_stick_y;

        if (gamepad1.y)
        {
            speed_mode = true;
        }
        if (gamepad1.a)
        {
            speed_mode = false;
        }

        if (speed_mode)
        {
            throttleL = Range.clip(throttleL, -1, 1);
            throttleR = Range.clip(throttleR, -1, 1);
            throttleL = (float) scaleInput(throttleL);
            throttleR = (float) scaleInput(throttleR);
        }
        if (!speed_mode )
        {
            throttleL = (float) Range.clip(throttleL, -0.5, 0.5);
            throttleR = (float) Range.clip(throttleR, -0.5, 0.5);
            throttleL = (float) scaleInputLow(throttleL);
            throttleR = (float) scaleInputLow(throttleR);
        }


        motorLeftRear.setPower(throttleL);
        motorLeftFront.setPower(throttleL);
        motorRightRear.setPower(throttleR);
        motorRightFront.setPower(throttleR);

        if (gamepad2.right_bumper)
        {
            plowPositionLeft += plowDeltaLeft;
            plowPositionRight += plowDeltaRight;
        }
        if (gamepad2.right_trigger > 0.2)
        {
            plowPositionLeft -= plowDeltaLeft;
            plowPositionRight -= plowDeltaRight;
        }
        if (gamepad2.left_bumper)
        {
            inOutPosition += inOutDelta;
        }
        if (gamepad2.left_trigger > 0.2)
        {
            inOutPosition -= inOutDelta;
        }
        if (gamepad2.y)
        {
            plowPositionLeft = 0.61;
            plowPositionRight = 0.39;
        }
        if (gamepad2.a)
        {
            plowPositionLeft = 0.05;
            plowPositionRight = 0.95;
        }
        if (gamepad2.x)
        {
            inOutPosition = 0.3;
        }
        if (gamepad2.b)
        {
            inOutPosition = 0.7;
        }

        plowInOut.setPosition(inOutPosition);
        plowLeft.setPosition(plowPositionLeft);
        plowRight.setPosition(plowPositionRight);
        armColorSensor.setPosition(armColorSensorPosition);



        telemetry.addData("Left Rear:"
                ,   +motorLeftRear.getPower()
                    + motorLeftRear.getCurrentPosition());
                telemetry.addData("Right Rear:"
                ,   +motorRightRear.getPower()
                    + motorRightRear.getCurrentPosition());
                telemetry.addData("Left Front:"
                ,   + motorLeftFront.getPower ()
                    + motorLeftFront.getCurrentPosition());
                telemetry.addData("Right Front:"
                ,   + motorRightFront.getPower()
                    + motorRightFront.getCurrentPosition());
                telemetry.addData("Left Plow:", plowLeft.getPosition());
                telemetry.addData("Right Plow:", plowRight.getPosition());
                telemetry.addData("InOut Plow:", plowInOut.getPosition());


        //telemetry.addData("X Accelerometer", accelX);
        //telemetry.addData("Y Accelerometer", accelY);
        //telemetry.addData("Z Accelerometer", accelZ);

    }
@Override
        public void onSensorChanged(SensorEvent event)
        {
            /*telemetry.addData ("ASensor", "Sensor Hit");
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                // get the change of the x,y,z values of the accelerometer
                deltaX = Math.abs(lastX - event.values[0]);
                deltaY = Math.abs(lastY - event.values[1]);
                deltaZ = Math.abs(lastZ - event.values[2]);
                // if the change is below 2, it is just plain noise
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
    double scaleInputLow(double dVal){
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
}
