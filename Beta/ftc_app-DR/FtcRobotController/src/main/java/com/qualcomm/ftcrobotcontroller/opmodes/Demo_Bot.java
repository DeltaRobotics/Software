package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/*
 * Created by RoboticsUser on 6/18/2016.
 *
 * 6/18/2016 - SAE - Worked on adding demobot code
 */
public class Demo_Bot extends OpMode {
    DcMotor motorLeft;
    DcMotor motorRight;
    Servo servoShoulder;
    Servo servoWrist;
    double leftpower;
    double rightpower;

    public void init()
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        servoShoulder = hardwareMap.servo.get("servoShoulder");
        servoWrist = hardwareMap.servo.get("servoWrist");
    }

    public void loop()
    {
        leftpower = gamepad1.left_stick_y;
        rightpower = gamepad1.right_stick_y;
        leftpower = Range.clip(leftpower, -1.0, 1.0);
        rightpower = Range.clip(rightpower, -1.0, 1.0);
        motorLeft.setPower(leftpower);
        motorRight.setPower(rightpower);
/*
need to put in servo controls
 */
    }
}
