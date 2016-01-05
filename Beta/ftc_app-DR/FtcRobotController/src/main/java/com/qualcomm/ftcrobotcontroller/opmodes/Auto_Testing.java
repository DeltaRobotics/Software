package com.qualcomm.ftcrobotcontroller.opmodes;
import android.graphics.Bitmap;

import com.qualcomm.ftcrobotcontroller.DR_Camera_Testing;
import com.qualcomm.ftcrobotcontroller.OpModeCamera;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class Auto_Testing extends OpModeCamera {

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    Servo catLeft;
    Servo catRight;

    final private double CATLeft_UP = 0.114;
    final private double CATLeft_DOWN = 0.886;
    final private double CATLeft_DELTA = 0.001;
    final private double CATRight_UP = 0.802;
    final private double CATRight_DOWN = 0.09;
    final private double CATRight_DELTA = 0.001;
    final private boolean SLOW_INCREMENT = true;

    private double catLeftPosition;
    private double catRightPosition;

    OpticalDistanceSensor floorSensor;

    private int a_state = 0;
    private int LR_enc;
    private int RR_enc;
    private int LF_enc;
    private int RF_enc;
    private int ds2;
    private int x = 0;

    int colorR = 10;
    int colorL = 10;

    double white = .15;
    double color = floorSensor.getLightDetected();

    enum States {INIT_MOTORS, DRIVE_FORWARD, PIVOT, DRIVEFORWARD2, DUMP_PEOPLE, STOP, RESTBPIVOT, SETUP_PIVOT, RESTBDRIVE_FORWARD2, SETUP_DRIVE_FORWARD2}

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

        catLeft = hardwareMap.servo.get("CatLeft");
        catRight = hardwareMap.servo.get("CatRight");

        set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);


        floorSensor = hardwareMap.opticalDistanceSensor.get("Sensor_distance");

        current_state = States.INIT_MOTORS;

        update_encoders();

        setCameraDownsampling(2);
        super.init();

        catLeftPosition = CATLeft_DOWN;
        catRightPosition = CATRight_DOWN;
    }

    public void loop() {
        catLeftPosition = Range.clip(catLeftPosition, 0.114, 0.886);
        catRightPosition = Range.clip(catRightPosition, 0.09, 0.862);

        if (imageReady()) {
            int redValueLeft = -76800;
            int blueValueLeft = -76800;
            int greenValueLeft = -76800;
            int redValueRight = -76800;
            int blueValueRight = -76800;
            int greenValueRight = -76800;

            Bitmap rgbImage;
            rgbImage = convertYuvImageToRgb(yuvImage, width, height, ds2);
            for (int x = 0; x < 320; x++) {
                for (int y = 121; y < 240; y++) {
                    int pixelL = rgbImage.getPixel(x, y);
                    redValueLeft += red(pixelL);
                    blueValueLeft += blue(pixelL);
                    greenValueLeft += green(pixelL);
                }
            }
            for (int a = 0; a < 320; a++) {
                for (int b = 0; b < 120; b++) {
                    int pixelR = rgbImage.getPixel(a,b);
                    redValueRight += red(pixelR);
                    blueValueRight += blue(pixelR);
                    greenValueRight += green(pixelR);
                }
            }
            redValueLeft = normalizePixels(redValueLeft);
            blueValueLeft = normalizePixels(blueValueLeft);
            greenValueLeft = normalizePixels(greenValueLeft);
            redValueRight = normalizePixels(redValueRight);
            blueValueRight = normalizePixels(blueValueRight);
            greenValueRight = normalizePixels(greenValueRight);
            int colorLeft = highestColor(redValueLeft, blueValueLeft);
            int colorRight = highestColor(redValueRight, blueValueRight);
            String colorStringLeft = "";
            String colorStringRight = "";
            colorR = colorRight;
            colorL = colorLeft;
            switch (colorLeft) {
                case 0:
                    colorStringLeft = "RED";
                    break;
                case 1:
                    colorStringLeft = "Color Undetected.";
                    break;
                case 2:
                    colorStringLeft = "BLUE";
            }
            switch (colorRight) {
                case 0:
                    colorStringRight = "RED";
                    break;
                case 1:
                    colorStringRight = "Color Undetected.";
                    break;
                case 2:
                    colorStringRight = "BLUE";
            }

            telemetry.addData("1 - Color Left", colorStringLeft);
            telemetry.addData("2 - Left Red", redValueLeft);
            telemetry.addData("3 - Left Blue", blueValueLeft);
            telemetry.addData("1 - Color Right", colorStringRight);
            telemetry.addData("2 - Right Red", redValueRight);
            telemetry.addData("3 - Right Blue", blueValueRight);
        }
        a_state++;
        telemetry.addData("7 - Case", current_state);
        telemetry.addData("5 - a_state", a_state);
        telemetry.addData("6 - X:", x);
        telemetry.addData("4 - Distance_sensor_output", floorSensor.getLightDetected());
        //SCA = Range.clip(SCA, 0.05, 0.9);
        // Update encoder values

        switch (current_state) {
            // Setup motor encoders
            case INIT_MOTORS:
                //telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                //telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                //telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                //telemetry.addData("Test:", "run_enc");

                // plowLeft.setPosition(0.0196);
                //plowRight.setPosition(0.9686);
                sleep(100);
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.DRIVE_FORWARD;
                break;

            case RESTBPIVOT:
                sleep(500);
                set_motor_power(-1.0, -1.0, 1.0, 1.0);
                current_state = States.PIVOT;
                break;
            case RESTBDRIVE_FORWARD2:
                sleep(500);
                current_state = States.SETUP_DRIVE_FORWARD2;
                break;
            case SETUP_PIVOT:
                current_state = States.PIVOT;
                break;
            case SETUP_DRIVE_FORWARD2:
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.DRIVEFORWARD2;
                break;
            case DRIVE_FORWARD:
                // Drive forward at 100% power
                telemetry.addData("8 - Encoder Front", +motorLeftFront.getCurrentPosition());
                telemetry.addData("8 - Encoder Rear", +motorLeftRear.getCurrentPosition());
                //sleep(100);
                if (motorLeftFront.getCurrentPosition() >= 2000 + x) {
                    //telemetry.addData("Test:", "enc_reached");
                    current_state = States.RESTBPIVOT;
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    //telemetry.addData("Test1", "if_statement");
                    x = motorLeftFront.getCurrentPosition();
                    break;
                } else {
                    telemetry.addData("Test1", "else_statement");
                }
                break;

            case PIVOT:
                if(motorLeftFront.getCurrentPosition()>= 2000 + x)
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    current_state = States.RESTBDRIVE_FORWARD2;
                    x = motorLeftFront.getCurrentPosition();
                }
                else {
                    telemetry.addData("Test:", "Else_Statement");
                }


            case DRIVEFORWARD2:
                if (motorLeftFront.getCurrentPosition() >= 11000 + x)
                {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    current_state = States.STOP;

                }

                break;
            case DUMP_PEOPLE:

            case STOP:
                //telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                //telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                //telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                //telemetry.addData("Test:", "run_enc");
                break;
            default:
                telemetry.addData("Case", "You all done");
                break;
        }
        catLeft.setPosition(catLeftPosition);
        catRight.setPosition(catRightPosition);

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