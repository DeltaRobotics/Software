package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
/**
 * Created by RoboticsUser on 1/16/2016.
 */
public class Distance_Test extends OpMode{
    OpticalDistanceSensor distanceSensor;
    double ODS;

    public void init() {
        distanceSensor = hardwareMap.opticalDistanceSensor.get("Sensor_distance");
    }
    public void loop() {
        ODS = distanceSensor.getLightDetected();
        telemetry.addData("ODS", ODS);
    }
}
