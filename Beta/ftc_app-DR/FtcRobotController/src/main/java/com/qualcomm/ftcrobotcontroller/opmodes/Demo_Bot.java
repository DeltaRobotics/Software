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
    Servo Catapult;
    Servo servoWrist;
    double leftpower;
    double rightpower;
    double Catapultposition;
    public void init()
    {
        motorLeft = hardwareMap.dcMotor.get("motorLeft");
        motorRight = hardwareMap.dcMotor.get("motorRight");
        Catapult = hardwareMap.servo.get("servoCatapult");
        //servoWrist = hardwareMap.servo.get("servoWrist");
        Catapultposition = 0.85;
        Catapult.setPosition(Catapultposition);
    }

    public void loop()
    {
        leftpower = gamepad1.left_stick_y;
        rightpower = gamepad1.right_stick_y;
        leftpower = Range.clip(leftpower, -1.0, 1.0);
        rightpower = Range.clip(rightpower, -1.0, 1.0);
        motorLeft.setPower(-leftpower);
        motorRight.setPower(rightpower);
        Catapult.setPosition(Catapultposition);
        Catapultposition = Range.clip(Catapultposition, 0.1, 0.85);

        if (gamepad1.a)
        {
            Catapultposition += 0.01;
        }
        if (gamepad1.y)
        {
            Catapultposition -= 0.01;
        }

        if (gamepad2.a)
        {
            Catapultposition += 0.01;
        }
        if (gamepad2.y)
        {
            Catapultposition -= 0.01;
        }
        telemetry.addData("Cat Position", Catapultposition);
        telemetry.addData("right motor", rightpower);
        telemetry.addData("left motor",   leftpower);

/*
need to put in servo controls

 */
    }
}
