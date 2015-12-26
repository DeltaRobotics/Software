package com.qualcomm.ftcrobotcontroller.opmodes;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;
/**
 * Created by RoboticsUser on 12/26/2015.
 */
public class Sensor_Test extends OpMode{

    OpticalDistanceSensor floorSensor;
    ColorSensor colorSensor;


    public void init () {

        floorSensor = hardwareMap.opticalDistanceSensor.get("Sensor_distance");
        colorSensor = hardwareMap.colorSensor.get("Sensor_color");
    }
    public void loop () {
        telemetry.addData("Distance_sensor_output", floorSensor.getLightDetected());
        telemetry.addData("Color sensor output", colorSensor.toString());
    }
}
