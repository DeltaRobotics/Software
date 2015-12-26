package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class Auto_Testing extends OpMode {

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    OpticalDistanceSensor floorSensor;

    private int a_state = 0;
    private int LR_enc;
    private int RR_enc;
    private int LF_enc;
    private int RF_enc;
    private int currentEncLR;
    private int currentEncLF;
    private int currentEncRR;
    private int currentEncRF;
    private int x = 0;

    double white = .15;
    double color = floorSensor.getLightDetected();

    enum States {INIT_MOTORS, DRIVE_FORWARD, PIVOT, FOLLOW_LINE, DUMP_PEOPLE, STOP, RESTBPIVOT, SETUP_PIVOT, RESTBDRIVE_FORWARD2, SETUP_DRIVE_FORWARD2}

    ;

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


        floorSensor = hardwareMap.opticalDistanceSensor.get("Sensor_distance");

        current_state = States.INIT_MOTORS;

        update_encoders();
    }

    public void loop() {
        a_state++;
        telemetry.addData("Case", current_state);
        telemetry.addData("a_state", a_state);
        telemetry.addData("X:", x);
        telemetry.addData("Distance_sensor_output", floorSensor.getLightDetected());
        //SCA = Range.clip(SCA, 0.05, 0.9);
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

                // plowLeft.setPosition(0.0196);
                //plowRight.setPosition(0.9686);
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
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.FOLLOW_LINE;
                break;
            case DRIVE_FORWARD:
                // Drive forward at 100% power
                telemetry.addData("Encoder Front", +motorLeftFront.getCurrentPosition());
                telemetry.addData("Encoder Rear", +motorLeftRear.getCurrentPosition());
                //sleep(100);
                if (floorSensor.getLightDetected() > 0.15) {
                    motorLeftRear.setPower(0);
                    motorRightRear.setPower(0);
                    motorLeftFront.setPower(0);
                    motorRightFront.setPower(0);
                    telemetry.addData("Test:", "enc_reached");
                    current_state = States.STOP;
                    telemetry.addData("Test1", "if_statement");
                    x = motorLeftFront.getCurrentPosition();
                    break;
                } else {
                    telemetry.addData("Test1", "else_statement");
                }
                break;

            case PIVOT:


            case FOLLOW_LINE:

                if (color < white)
                {

                }

                break;
            case DUMP_PEOPLE:

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
        if (motorRightRear.getChannelMode() != mode) {
            motorRightRear.setChannelMode(mode);
        }
        if (motorLeftFront.getChannelMode() != mode) {
            motorLeftFront.setChannelMode(mode);
        }
        if (motorRightFront.getChannelMode() != mode) {
            motorRightFront.setChannelMode(mode);
        }

    }
    public static void sleep(int amt) // In milliseconds
    {
        double a = System.currentTimeMillis();
        double b = System.currentTimeMillis();
        while ((b - a) <= amt) {
            b = System.currentTimeMillis();
        }
    }


    public void update_encoders() {
        LR_enc = motorLeftRear.getCurrentPosition();
        RR_enc = motorRightRear.getCurrentPosition();
        LF_enc = motorLeftFront.getCurrentPosition();
        RF_enc = motorRightFront.getCurrentPosition();
    }


}