package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_Test extends DR_Auto_Setup {
    @Override
    public void init() {
        Servo armColorSensor;
        armColorSensor = hardwareMap.servo.get("ColorSensor_Arm");
    }
    public void loop(){

    }
}