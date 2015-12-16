package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Red extends OpMode {

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;
    //Servo armColorSensor;
    Servo plowLeft;
    Servo plowRight;
    Servo plowInOut;
    Servo catLeft;
    Servo catRight;
    //double SCA;
    //double SCAdelta;
    double pL;
    double pLdelta;
    double pR;
    double pRdelta;
    double plowdelta;
    double plowUp = 0.9;
    double InOut;
    double pldown = 0.7607843;
    double prdown = 0.11372549;
    private int a_state = 0;
    private int LR_enc;
    private int RR_enc;
    private int LF_enc;
    private int RF_enc;
    private int currentEncLR;
    private int currentEncLF;
    private int currentEncRR;
    private int currentEncRF;
    final private double CATLeft_UP = 0.114;
    final private double CATLeft_DOWN = 0.886;
    final private double CATLeft_DELTA = 0.001;
    final private double CATRight_UP = 0.802;
    final private double CATRight_DOWN = 0.09;
    final private double CATRight_DELTA = 0.001;
    final private boolean SLOW_INCREMENT = true;
    private int x = 0;


    enum States {INIT_MOTORS,DRIVE_FORWARD,PIVOT,DRIVE_FORWARD2, DUMP_PEOPLE, STOP, RESTBPIVOT, SETUP_PIVOT, RESTBDRIVE_FORWARD2, SETUP_DRIVE_FORWARD2};

    States current_state;

    @Override
    public void init() {
        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);

        set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);


        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        //armColorSensor = hardwareMap.servo.get("ColorSensor_arm");
        plowInOut = hardwareMap.servo.get("InOut_Plow");
        catLeft = hardwareMap.servo.get("CatLeft");
        catRight = hardwareMap.servo.get("CatRight");
        //SCA = 0.80;
        InOut = .455;
        plowInOut.setPosition(InOut);
        //SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.23137255;
        pLdelta = plowdelta;
        pR = 0.64705884;
        pRdelta = -plowdelta;
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);

        catLeft.setPosition(CATLeft_DOWN);
        catRight.setPosition(CATRight_DOWN);
        //armColorSensor.setPosition(SCA);u

        current_state = States.INIT_MOTORS;

        update_encoders();
    }
    public void loop(){
        a_state++;
        telemetry.addData("Case", current_state);
        telemetry.addData("a_state", a_state);
        telemetry.addData("X:", x);
        //SCA = Range.clip(SCA, 0.05, 0.9);
        pL = Range.clip(pL, 0.05, 0.9);
        pR = Range.clip(pR, 0.05, 0.9);

        // Update encoder values
        currentEncLR = LR_enc;
        currentEncLF = LF_enc;
        currentEncRR = RR_enc;
        currentEncRF = RF_enc;

        switch (current_state) {
            // Setup motor encoders
            case INIT_MOTORS:
                while(pldown >= pL && prdown <= pR) {
                    pL += 0.001;
                    pR -= 0.001;
                    pL = Range.clip(pL,0.10,0.8);
                    pR = Range.clip(pR,0.10,0.8);
                    plowLeft.setPosition(pL);
                    plowRight.setPosition(pR);

                }
                telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                telemetry.addData("Test:", "run_enc");

                // plowLeft.setPosition(0.0196);
                //plowRight.setPosition(0.9686);
                plowInOut.setPosition(0.25);
                sleep(100);
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.DRIVE_FORWARD;
                break;

            case RESTBPIVOT:
                sleep(500);
                current_state = States.SETUP_PIVOT;
                break;
            case RESTBDRIVE_FORWARD2:
                sleep(500);
                current_state = States.SETUP_DRIVE_FORWARD2;
                break;
            case SETUP_PIVOT:
                set_motor_power(0.8, -0.8, 0.8, -0.8);
                current_state = States.PIVOT;
                break;
            case SETUP_DRIVE_FORWARD2:
                set_motor_power(1.0,1.0,1.0,1.0);
                current_state = States.DRIVE_FORWARD2;
                break;
            case DRIVE_FORWARD:
                // Drive forward at 100% power
                telemetry.addData("Encoder Front", +motorLeftFront.getCurrentPosition());
                telemetry.addData("Encoder Rear", +motorLeftRear.getCurrentPosition());
                //sleep(100);
                if (has_Left_encoder_reached(x + 12000)) {
                    motorLeftRear.setPower(0);
                    motorRightRear.setPower(0);
                    motorLeftFront.setPower(0);
                    motorRightFront.setPower(0);
                    telemetry.addData("Test:", "enc_reached");
                    current_state = States.STOP;
                    telemetry.addData("Test1", "if_statement");
                    x = motorLeftFront.getCurrentPosition();
                    break;
                }
                else {
                    telemetry.addData("Test1", "else_statement");
                }
                break;

            case PIVOT:
                if (has_Left_encoder_reached(x + 2400)) {
                    set_motor_power(0, 0, 0, 0);
                    current_state = States.RESTBDRIVE_FORWARD2;
                    x = motorLeftFront.getCurrentPosition();
                    telemetry.addData("Test1", "wait_before_forward");
                    break;
                }
                else {
                    telemetry.addData("Test1", "else_statement");
                    telemetry.addData("Encoder Rear", +motorLeftRear.getCurrentPosition());
                }
                break;

            case DRIVE_FORWARD2:
                // Raise the servo with the people
                /*
                if (SLOW_INCREMENT) {
                    for (double position = catapult.getPosition(); position > CATAPULT_UP; position -= CATAPULT_DELTA) {
                        catapult.setPosition(position);
                        sleep (250);
                        telemetry.addData("Catapult Position", position);
                    }
                } else {
                    catapult.setPosition(CATAPULT_UP);
                }
                */

                //CatRight up position is .70

                if (has_Left_encoder_reached(x + 1200)) {
                    set_motor_power(0, 0, 0, 0);
                    current_state = States.DUMP_PEOPLE;
                    break;
                }
                else {
                    telemetry.addData("Test1", "else");
                }
                break;
            case DUMP_PEOPLE:
                //plowLeft.setPosition(0.0196);
                //plowRight.setPosition(0.9686);
                catLeft.setPosition(CATLeft_UP);
                sleep(1000);
                current_state = States.STOP;
                break;
            case STOP:
                telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                telemetry.addData("Test:", "run_enc");
                break;
            default:
                telemetry.addData("Case", "You all done");
                break;
        }
    }

    public void set_motor_power(double LR_power, double RR_power, double LF_power, double RF_power) {
        motorLeftRear.setPower(LR_power);
        motorRightRear.setPower(RR_power);
        motorLeftFront.setPower(LF_power);
        motorRightFront.setPower(RF_power);
    }
    public void set_drive_mode(DcMotorController.RunMode mode) {
        if (motorLeftRear.getChannelMode() != mode) {
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