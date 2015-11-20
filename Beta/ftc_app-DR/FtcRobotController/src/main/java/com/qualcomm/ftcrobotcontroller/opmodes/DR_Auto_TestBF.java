package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_TestBF extends OpMode{

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;
    //Servo armColorSensor;
    Servo plowLeft;
    Servo plowRight;
    Servo plowInOut;
    //double SCA;
    //double SCAdelta;
    double pL;
    double pLdelta;
    double pR;
    double pRdelta;
    double plowdelta;
    double InOut;
    private int a_state;
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
        motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        //armColorSensor = hardwareMap.servo.get("ColorSensor_arm");
        plowInOut = hardwareMap.servo.get("InOut_Plow");
        //SCA = 0.80;
        InOut = .5;
        //SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.61;
        pLdelta = plowdelta;
        pR = 0.39;
        pRdelta = -plowdelta;
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);
        //armColorSensor.setPosition(SCA);
        a_state = 0;

        update_encoders();
    }
    public void loop(){
        telemetry.addData("Case", "X");
        telemetry.addData("a_state", a_state);
        //SCA = Range.clip(SCA, 0.05, 0.9);
        pL = Range.clip(pL, 0.05, 0.9);
        pR = Range.clip(pR, 0.05, 0.9);
        /*telemetry.addData("Encoder LR", motorLeftRear.getCurrentPosition());
        telemetry.addData("Encoder RR", motorRightRear.getCurrentPosition());
        telemetry.addData("Encoder LF", motorLeftFront.getCurrentPosition());
        telemetry.addData("Encoder RF", motorRightFront.getCurrentPosition()); */
        currentEncLR = LR_enc;
        currentEncLF = LF_enc;
        currentEncRR = RR_enc;
        currentEncRF = RF_enc;
        switch (a_state){
            case 0:
                telemetry.addData("Case", "0");
                telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                telemetry.addData("Test:", "run_enc");
                a_state++;
                break;
            case 1:
                telemetry.addData("Case", "1");
                set_motor_power(1.0,1.0,1.0,1.0);
                telemetry.addData("Encoder Front", +motorLeftFront.getCurrentPosition());
                telemetry.addData("Encoder Rear", +motorLeftRear.getCurrentPosition());
                if (has_Left_encoder_reached(10500))
                {
                    telemetry.addData("Test:", "enc_reached");
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    a_state++;
                }
                break;
             /*case 2:
                telemetry.addData("Case", "2");
                update_encoders();
                telemetry.addData("Enc_Count2", LR_enc);
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                update_encoders();
                telemetry.addData("Enc_Count3", LR_enc);
                telemetry.addData("encodersAreZero", encodersAreZero());
                set_motor_power(0.5, -0.5, 0.5, -0.5);
                if (has_Left_encoder_reached(LR_enc + 3000))
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    a_state++;
                }
                else
                {
                    telemetry.addData("Waiting in case 2", "True");
                }
                break;
            case 3:
                telemetry.addData("Case", "3");
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5, 0.5, 0.5, 0.5);
                if (has_Left_encoder_reached(LR_enc + 4000))
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    a_state++;
                }
                else
                {
                    telemetry.addData("Waiting in case 3", "True");
                }
                break;
            case 4:
                telemetry.addData("Case", "4");
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5, -0.5, 0.5, -0.5);
                if (has_Left_encoder_reached(LR_enc + 1000))
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 5:
                telemetry.addData("Case", "5");
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                set_motor_power(0.5, 0.5, 0.5, 0.5);
                if (LR_enc > (LR_enc + 2000))
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 6:
                telemetry.addData("Case", "6");
                telemetry.addData("Stopped", 0.0);
                break;
                */
            default:
                telemetry.addData("Case", "Default");
                telemetry.addData("Default Case Reached", "True");
                break;
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