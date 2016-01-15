package com.qualcomm.ftcrobotcontroller.opmodes;
import android.app.Activity;
import android.view.View;
import android.graphics.Color;

import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by RoboticsUser on 12/26/2015.
 */
public class Sensor_Test extends OpMode{

    OpticalDistanceSensor distanceSensor;
    ColorSensor colorSensor;
    boolean flag = true;

    public void init() {

        distanceSensor = hardwareMap.opticalDistanceSensor.get("Sensor_distance");
        colorSensor = hardwareMap.colorSensor.get("Sensor_color");
    }
    public void loop () {

        if(flag) {

            double ODSr = distanceSensor.getLightDetectedRaw();
            double ODS = distanceSensor.getLightDetected();

            float hsvValues[] = {6F, 6F, 6F};
            final float values[] = hsvValues;
            final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);

            Color.RGBToHSV(colorSensor.red() * 8, colorSensor.green() * 8, colorSensor.blue() * 8, hsvValues);

            telemetry.addData("Object Reference ", colorSensor.toString());
            telemetry.addData("Clear", colorSensor.alpha());
            telemetry.addData("Red  ", colorSensor.red());
            telemetry.addData("Blue", colorSensor.blue());
            telemetry.addData("Green", colorSensor.green());
            telemetry.addData("Hue", hsvValues[0]);

            telemetry.addData("Distance_sensor_output", ODS);
            telemetry.addData("Distance_sensor_output_Raw", ODSr);

            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
        }
    }
}
