package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_PeopleTurn extends OpMode{

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;
    //Servo armColorSensor;
    Servo plowLeft;
    Servo plowRight;
    Servo plowInOut;
    Servo catapult;
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
    final private double CATAPULT_UP = 0.156;
    final private double CATAPULT_DOWN = 0.858;
    final private double CATAPULT_DELTA = 0.001;
    final private boolean SLOW_INCREMENT = true;

    enum States {
        INIT_MOTORS,
        DRIVE_FORWARD,
        PIVOT,
        DUMP_PEOPLE
    };

    States current_state;

    @Override
    public void init() {
        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorLeftFront.setDirection(DcMotor.Direction.REVERSE);

        set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);

        /*
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        //armColorSensor = hardwareMap.servo.get("ColorSensor_arm");
        plowInOut = hardwareMap.servo.get("InOut_Plow");
        catapult = hardwareMap.servo.get("Catapult");
        SCA = 0.80;
        InOut = .5;
        SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.61;
        pLdelta = plowdelta;
        pR = 0.39;
        pRdelta = -plowdelta;
        */
        /*
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);

        catapult.setPosition(CATAPULT_DOWN);
        armColorSensor.setPosition(SCA);
        */
        current_state = States.INIT_MOTORS;

        update_encoders();
    }
    public void loop(){
        telemetry.addData("Case", "X");
        telemetry.addData("a_state", a_state);
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
                telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                telemetry.addData("Test:", "run_enc");
                current_state = States.DRIVE_FORWARD;
                break;

            case DRIVE_FORWARD:
                // Drive forward at 100% power
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                telemetry.addData("Encoder Front", +motorLeftFront.getCurrentPosition());
                telemetry.addData("Encoder Rear", +motorLeftRear.getCurrentPosition());
                if (has_Left_encoder_reached(9500)) {
                    telemetry.addData("Test:", "enc_reached");
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    sleep(500);
                    current_state = States.PIVOT;
                }
                break;

            case PIVOT:
                set_motor_power(0.5, -0.5, 0.5, -0.5);
                if (has_Left_encoder_reached(1000)) {
                    set_motor_power(0, 0, 0, 0);
                    //current_state = States.DUMP_PEOPLE;
                }
                break;

            case DUMP_PEOPLE:
                // Raise the servo with the people
                if (SLOW_INCREMENT) {
                    for (double position = catapult.getPosition(); position > CATAPULT_UP; position -= CATAPULT_DELTA) {
                        catapult.setPosition(position);
                        telemetry.addData("Catapult Position", position);
                    }
                } else {
                    catapult.setPosition(CATAPULT_UP);
                }
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