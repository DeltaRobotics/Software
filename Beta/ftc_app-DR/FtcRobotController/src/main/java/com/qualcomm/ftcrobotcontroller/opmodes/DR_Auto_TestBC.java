package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by RoboticsUser on 11/7/2015.
 */
public class DR_Auto_TestBC extends OpMode{
    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;
    Servo plowLeft;
    Servo plowRight;
    //double SCA;
    //double SCAdelta;
    double pL;
    double pLdelta;
    double pR;
    double pRdelta;
    double plowdelta;
    private int a_state = 0;
    private int LR_enc;
    private int RR_enc;
    private int LF_enc;
    private int RF_enc;
    private int currentEncLR;
    private int currentEncLF;
    private int currentEncRR;
    private int currentEncRF;
    @Override
    public void init() {
        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        //SCA = 0.0;
        //SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.3;
        pLdelta = plowdelta;
        pR = 0.7;
        pRdelta = -plowdelta;
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);

    }
    public void loop(){
        //SCA = Range.clip(SCA, 0.05, 0.9);
        pL = Range.clip(pL, 0.05, 0.9);
        pR = Range.clip(pR, 0.05, 0.9);
        telemetry.addData("Encoder LR", motorLeftRear.getCurrentPosition());
        telemetry.addData("Encoder RR", motorRightRear.getCurrentPosition());
        telemetry.addData("Encoder LF", motorLeftFront.getCurrentPosition());
        telemetry.addData("Encoder RF", motorRightFront.getCurrentPosition());
        currentEncLR = LR_enc;
        currentEncLF = LF_enc;
        currentEncRR = RR_enc;
        currentEncRF = RF_enc;
        switch (a_state){
            case 0:
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                a_state++;
                break;
            case 1:
                set_motor_power(0.5, 0.5, 0.5, 0.5);
                if (has_Left_encoder_reached(LR_enc + 1000))
                {
                    set_motor_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 2:
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5,-0.5,0.5,-0.5);
                if (has_Left_encoder_reached(LR_enc + 1000))
                {
                    set_motor_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 3:
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5,0.5,0.5,0.5);
                if (has_Left_encoder_reached(LR_enc + 2000))
                {
                    set_motor_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 4:
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5,-0.5,0.5,-0.5);
                if (has_Left_encoder_reached(LR_enc + 2000))
                {
                    set_motor_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 5:
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5,0.5,0.5,0.5);
                if (LR_enc > (LR_enc + 2000))
                {
                    set_motor_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 6:
                telemetry.addData("Stopped", 0.0);
        }

    }
    public void set_motor_power(double LR_power, double RR_power, double LF_power, double RF_power)
    {
        motorLeftRear.setPower(LR_power);
        motorRightRear.setPower(RR_power);
        motorLeftFront.setPower(LF_power);
        motorRightFront.setPower(RF_power);
    }
    public void set_drive_mode(DcMotorController.RunMode mode) {
        if (motorLeftRear.getChannelMode() != mode)
        {
            motorLeftRear.setChannelMode(mode);
        }
        if (motorRightRear.getChannelMode() != mode)
        {
            motorRightRear.setChannelMode(mode);
        }
        if (motorLeftFront.getChannelMode() != mode)
        {
            motorLeftFront.setChannelMode(mode);
        }
        if (motorRightFront.getChannelMode() != mode)
        {
            motorRightFront.setChannelMode(mode);
        }

    }
    public void run_using_encoders()
    {
        if (motorLeftRear != null && motorRightRear != null && motorLeftFront != null && motorRightFront != null)
        {
            motorLeftRear.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorRightRear.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorLeftFront.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorRightFront.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }
    }
    boolean has_Left_encoder_reached(double e_count)
    //Has the left rear reached the value of the encoder that was designated?
    {
        boolean returnLRe = false;
        if (motorLeftRear != null)
        {
            if (
                    (Math.abs(motorLeftRear.getCurrentPosition())/2 + Math.abs(motorLeftFront.getCurrentPosition())/2) > e_count)
            {
                returnLRe = true;
            }
        }
        return returnLRe;
    }
    boolean has_Right_encoder_reached(double e_count)
    //Has the right rear reached the value of the encoder that was designated?
    {
        boolean returnRRe = false;
        if (motorRightRear != null)
        {
            if (((Math.abs(motorRightRear.getCurrentPosition())
                    + Math.abs(motorRightFront.getCurrentPosition()))/2) > e_count)
            {
                returnRRe = true;
            }
        }
        return returnRRe;
    }
    public static void sleep(int amt) // In milliseconds
    {
        double a = System.currentTimeMillis();
        double b = System.currentTimeMillis();
        while ((b - a) <= amt) {
            b = System.currentTimeMillis();
        }
    }
    public boolean encodersAreZero()
    {
        return (motorLeftRear.getCurrentPosition() == 0 &&
                motorRightRear.getCurrentPosition() == 0 &&
                motorLeftFront.getCurrentPosition() == 0 &&
                motorRightFront.getCurrentPosition() == 0);
    }
    public void update_encoders()
    {
        LR_enc = motorLeftRear.getCurrentPosition();
        RR_enc = motorRightRear.getCurrentPosition();
        LF_enc = motorLeftFront.getCurrentPosition();
        RF_enc = motorRightFront.getCurrentPosition();
    }
}
