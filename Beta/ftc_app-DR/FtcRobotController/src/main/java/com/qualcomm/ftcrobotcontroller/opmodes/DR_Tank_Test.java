package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by steve.brooks on 9/30/2015.
 */
public class DR_Tank_Test extends OpMode implements SensorEventListener{

    public SensorManager mSensorManager;
    public Sensor Accelerometer;

    public float deltaX = 0;
    public float deltaY = 0;
    public float deltaZ = 0;
    public float deltaXMax = 0;
    public float deltaYMax = 0;
    public float deltaZMax = 0;
    public float lastX, lastY, lastZ;

    ColorSensor sensorRGB;
    DcMotor motorRight;
    DcMotor motorLeft;
    //Initialize the Accelerometer_
    SensorManager sensorService = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
    {
        Sensor Accelerometer = sensorService.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        SensorManager.registerListener(this, Accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

//TouchSensor touchSensor;
   // Servo servo1;

   // double servo1Position;
    //double servo1Delta;

    public DR_Tank_Test() {

    }
    public void init() {

        motorRight = hardwareMap.dcMotor.get ("Drive_Right");
        motorLeft = hardwareMap.dcMotor.get ("Drive_Left");
        motorRight.setDirection(DcMotor.Direction.REVERSE);
        sensorRGB = hardwareMap.colorSensor.get("Color_Sensor");
        sensorRGB.enableLed(true);
        //float hsvValues = {0F, 0F, 0F};
        //final float values[] = hsvValues;
        //touchSensor = hardwareMap.touchSensor.get ("touchSensor");
       // servo1 = hardwareMap.servo.get ("servo1");

       // servo1Position = 0.2;
        //servo1Delta = 0.1;

    }
   /* public void onSensorChanged(SensorEvent event) { //when sensor changes, calculate
        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

        if (timestamp != 0) {
            final float dT = (event.timestamp - timestamp) * NS2S; //set dT (change in time)
            //Find the un-normalized value of the rotation sample - pulled from an array.
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];
            //Calculate the angular speed
            float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);
            //Normalize the rotation vector (epsilon - max value allowed)
            if (omegaMagnitude > 1.0) {
                axisX /= omegaMagnitude;
                axisY /= omegaMagnitude;
                axisZ /= omegaMagnitude;
            }
            float thetaOverTwo = omegaMagnitude * dT / 2.0f;
            float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
            float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
            deltaRotationVector[0] = sinThetaOverTwo * axisX;
            deltaRotationVector[1] = sinThetaOverTwo * axisY;
            deltaRotationVector[2] = sinThetaOverTwo * axisZ;
            deltaRotationVector[3] = cosThetaOverTwo;
        }

        }
        timestamp = event.timestamp;
        float[] deltaRotationMatrix = new float[9];
        SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);

        telemetry.addData("X-Axis", deltaRotationMatrix[0]);
        telemetry.addData("Y-Axis", deltaRotationMatrix[1]);
        telemetry.addData("Z-Axis", deltaRotationMatrix[2]); */


       //}
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    public void loop() {
        float throttlel = gamepad1.left_stick_y;
        float throttler = gamepad1.right_stick_y;
        throttlel = Range.clip (throttlel, -1, 1);
        throttler = Range.clip(throttler, -1, 1);

        throttlel = (float)scaleInput(throttlel);
        throttler = (float) scaleInput(throttler);

        motorLeft.setPower(throttlel);
        motorRight.setPower(throttler);

        onSensorChanged();
        //servo1.setPosition(servo1Position);


        // if (touchSensor.isPressed()) {
        //     servo1Position += servo1Delta;
        //  }
        // convert the RGB values to HSV values.
        //Color.RGBToHSV((sensorRGB.red() * 8), (sensorRGB.green() * 8), (sensorRGB.blue() * 8), hsvValues);
        //Color.RGBToHSV(sensorRGB.red() * 8, sensorRGB.green() * 8, sensorRGB.blue() * 8, hsvValues);

        // send the info back to driver station using telemetry function.
        //telemetry.addData("Object Reference ", motorRight.toString());
        //telemetry.addData("Clear", sensorRGB.alpha());
        //telemetry.addData("Red  ", sensorRGB.red());
        //telemetry.addData("Green", sensorRGB.green());
        //telemetry.addData("Blue ", sensorRGB.blue());
        //telemetry.addData("Hue", hsvValues[0]);



        telemetry.addData("Left Drive: "
                ,	+ motorLeft.getPower ()
                    + motorLeft.getCurrentPosition());

        telemetry.addData("Right Drive:"
               ,    + motorRight.getPower()
                    + motorRight.getCurrentPosition());
        //telemetry.addData("Is Pressed", String.valueOf(touchSensor.isPressed()) );
    }
@Override
        public void onSensorChanged(SensorEvent event) {
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
