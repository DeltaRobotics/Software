package com.qualcomm.ftcrobotcontroller.opmodes;

import com.delta.utilites.GyroSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by RoboticsUser on 12/19/2015.
 */
public class GyroTestOpMode extends OpMode {

    GyroSensor sensor;
    float x;
    float y;
    float z;
    boolean sensorInitialized;

    public GyroTestOpMode() {

    }

    public void init() {
        sensor =  new GyroSensor(500, hardwareMap, "Gyro");
        sensorInitialized = false;
        x = 0;
        y = 0;
        z = 0;
    }

    public void loop() {
        if (sensorInitialized) {
            sensor.read();
            x = sensor.getX();
            y = sensor.getY();
            z = sensor.getZ();

            telemetry.addData("Gyro X", x);
            telemetry.addData("Gyro Y", y);
            telemetry.addData("Gyro Z", z);
        } else {
            telemetry.addData("Initialized state", "Aw man! It ain't workin");
        }
    }
}
