package com.qualcomm.ftcrobotcontroller.opmodes;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.ftcrobotcontroller.DR_Camera_Testing;
import com.qualcomm.ftcrobotcontroller.OpModeCamera;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Blue extends OpModeCamera {

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    Servo catLeft;
    //Servo catRight;

    boolean flag = false;
    int flag1 = 3;
    int flag2 = 3;
    boolean hit = false;

    Servo winchAngle;
    //Servo plowLeft;
    //Servo plowRight;
    //Servo plowInOut;

    ColorSensor RGBSensor;
    OpticalDistanceSensor ODS;

    final private double CATLeft_UP = 0.114;
    final private double CATLeft_DOWN = 0.886;
    final private double CATLeft_DELTA = 0.001;
    final private double CATRight_UP = 0.802;
    final private double CATRight_DOWN = 0.09;
    final private double CATRight_DELTA = 0.001;
    final private boolean SLOW_INCREMENT = true;
    final private double WINCHAngle_DOWN = 0.5;
    final private double WINCHAngle_UP = 0.5;

    //final private double PLOWLeft_UP = 0.23137255;
    //final private double PLOWLeft_DOWN = 0.71;
    //final private double PLOWLeft_DELTA = 0.002;
    //final private double PLOWRight_UP = 0.64705884;
    //final private double PLOWRight_DOWN = 0.11;
    //final private double PLOWRight_DELTA = 0.002;
    //final private double PLOWInOut_IN = .4;
    //final private double PLOWInOut_OUT = .83;

    private double catLeftPosition;
    private double catRightPosition;
    private double winchAnglePosition;
    //private double plowLeftPosition;
    //private double plowRightPosition;
    //private double plowInOutPosition;

    private int a_state = 0;
    private int LR_enc;
    private int RR_enc;
    private int LF_enc;
    private int RF_enc;
    private int ds2;
    private int x = 0;
    private float y = 0;
    public int rev = 0;
    public boolean line;

    String statesstring;

    int colorR = 10;
    int colorL = 10;

    double white = .15;

    enum States {INIT_MOTORS, DRIVE_FORWARD, PIVOT, DRIVEFORWARD2, DUMP_PEOPLE, STOP, RESTBPIVOT, SETUP_PIVOT, RESTBDRIVE_FORWARD2, SETUP_DRIVE_FORWARD2, READ_BEACON, FOLLOW_LINE}

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
        //catRight = hardwareMap.servo.get("CatRight");
        //plowLeft = hardwareMap.servo.get("Left_Plow");
        //plowRight = hardwareMap.servo.get("Right_Plow");
        //plowInOut = hardwareMap.servo.get("InOut_Plow");
        winchAngle = hardwareMap.servo.get("Winch_Angle");

        RGBSensor = hardwareMap.colorSensor.get("BottomColorSensor");
        ODS = hardwareMap.opticalDistanceSensor.get("FrontODS");


        //set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);

        current_state = States.INIT_MOTORS;

        //update_encoders();

        setCameraDownsampling(2);
        super.init();
        catLeftPosition = CATLeft_DOWN;
        catRightPosition = CATRight_DOWN;
        //winchAnglePosition = WINCHAngle_DOWN;

        // catLeft.setPosition(CATLeft_DOWN);
        // catRight.setPosition(CATRight_DOWN);

        //plowLeft.setPosition(PLOWLeft_UP);
        //plowRight.setPosition(PLOWRight_UP);
        line = false; //blue side of field

    }

    public void loop() {
        y = RGBSensor.argb()/1000000;
        int ODSr = ODS.getLightDetectedRaw();
        catLeft.setPosition(catLeftPosition);
        //catRight.setPosition(catRightPosition);
        //winchAngle.setPosition(winchAnglePosition);
        //plowLeft.setPosition(plowLeftPosition);
        //plowRight.setPosition(plowRightPosition);
        //plowInOut.setPosition(plowInOutPosition);
        if (ODSr < 8) {
            RGBSensor.enableLed(true);
            // float hsvValues[] = {6F, 6F, 6F};
            ///telemetry.addData("ARGB", y);

        }
        else {
            RGBSensor.enableLed(false);
        }
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
                    int pixelR = rgbImage.getPixel(a, b);
                    redValueRight += red(pixelR);
                    blueValueRight += blue(pixelR);
                    greenValueRight += green(pixelR);
                }
            }
            redValueLeft = normalizePixels(redValueLeft);
            blueValueLeft = normalizePixels(blueValueLeft);
            //greenValueLeft = normalizePixels(greenValueLeft);
            redValueRight = normalizePixels(redValueRight);
            blueValueRight = normalizePixels(blueValueRight);
            //greenValueRight = normalizePixels(greenValueRight);
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

            /* telemetry.addData("1 - Color Left", colorStringLeft);
            telemetry.addData("2 - Left Red", redValueLeft);
            telemetry.addData("3 - Left Blue", blueValueLeft);
            telemetry.addData("1 - Color Right", colorStringRight);
            telemetry.addData("2 - Right Red", redValueRight);
            telemetry.addData("3 - Right Blue", blueValueRight);
            */
        }
        a_state++;

        //statesstring = statesstring + " " + current_state;

        telemetry.addData("7 - Case", current_state);
        ///telemetry.addData("5 - a_state", statesstring);  // Causes NullPointerErrors
        telemetry.addData("6 - X:", x);
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

                //plowLeftPosition = PLOWLeft_DOWN;
                //plowRightPosition = PLOWRight_DOWN;
                //plowInOutPosition = PLOWInOut_IN;
                winchAngle.setPosition(.78);
                //winchAngle.setPosition(WINCHAngle_UP);
                sleep(1000);
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.DRIVE_FORWARD;
                break;

            case DRIVE_FORWARD:
                // Drive forward at 100% power
                ///telemetry.addData("8 - Encoder Front", +motorLeftFront.getCurrentPosition());
                ///telemetry.addData("8 - Encoder Rear", +motorLeftRear.getCurrentPosition());
                //sleep(100);
                if (motorLeftFront.getCurrentPosition() >= 2000 + x) {
                    //telemetry.addData("Test:", "enc_reached");
                    current_state = States.RESTBPIVOT;
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    //telemetry.addData("Test1", "if_statement");
                    x = motorLeftFront.getCurrentPosition();
                    break;
                } else {
                    ///telemetry.addData("Test1", "else_statement");
                }
                break;

            case RESTBPIVOT:
                sleep(500);
                set_motor_power(1.0, -1.0, 1.0, -1.0);
                current_state = States.PIVOT;
                break;

            case PIVOT:
                if (motorLeftFront.getCurrentPosition() >= 1300 + x) {
                    set_motor_power(0.0, 0.0, 0.0, 0.0);
                    current_state = States.RESTBDRIVE_FORWARD2;
                    x = motorLeftFront.getCurrentPosition();
                } else {
                    ///telemetry.addData("Test:", "Else_Statement");
                }
                break;
            case RESTBDRIVE_FORWARD2:
                sleep(500);
                current_state = States.SETUP_DRIVE_FORWARD2;
                break;

            case SETUP_DRIVE_FORWARD2:
                set_motor_power(1.0, 1.0, 1.0, 1.0);
                current_state = States.DRIVEFORWARD2;
                break;

            case DRIVEFORWARD2:
                if (y >= 180 && y <= 220){
                    telemetry.addData("Color", "White");
                    set_motor_power(0.0,0.0,0.0,0.0);
                    current_state = States.FOLLOW_LINE;
                }
                else
                {
                    telemetry.addData("Color", "Not White");

                }
                break;

            case FOLLOW_LINE:
                telemetry.addData("Color", "LFWhite");
                if (ODS.getLightDetectedRaw() < 50){
                    if (lineFollow(180, 220, line) == 0)
                    {
                        if(flag2 ==1)
                        {
                            rev++;
                        }
                        flag2 = 0;
                        set_motor_power(.5, .6, .5, .6);
                        line = !line;
                    }
                    else if (lineFollow(180, 220, line) == 1)
                        {
                            if(flag2 ==0)
                            {
                                rev++;
                            }
                            flag2 = 1;
                            set_motor_power(.6, .5, .6, .5);
                            line = !line;
                        }
                        else if (flag1 == 2)
                    {
                        telemetry.addData("Stuff", "2");
                    }
                        {
                            telemetry.addData("Stuff", "Invalid Line number");
                        }
                }
                else{
                    set_motor_power(0.0,0.0,0.0,0.0);
                    current_state = States.DUMP_PEOPLE;
                }
                telemetry.addData("Line", line);
                telemetry.addData("Color", RGBSensor.argb()/1000000);
                telemetry.addData("Loop", rev);
                telemetry.addData("Left", motorLeftFront.getPower());
                telemetry.addData("Right", motorRightFront.getPower());
                telemetry.addData("Loops", flag2);
                break;

            case DUMP_PEOPLE:
                if (catLeftPosition > CATLeft_UP) {
                    catLeftPosition -= .1;
                    sleep(50);
                } else {
                    current_state = States.STOP;
                }
                break;

            case STOP:
                //telemetry.addData("Enc_Count", motorLeftRear.getCurrentPosition());
                set_drive_mode(DcMotorController.RunMode.RESET_ENCODERS);
                //telemetry.addData("Enc_Count1", motorLeftRear.getCurrentPosition());
                //telemetry.addData("Test:", "reset_enc");
                set_drive_mode(DcMotorController.RunMode.RUN_USING_ENCODERS);
                //telemetry.addData("Test:", "run_enc");
                break;
            default:
                ///telemetry.addData("Case", "You're all done");
                break;
        }
        telemetry.addData("Color Sensor", y);
        telemetry.addData("Distance Sensor", ODS.getLightDetectedRaw());
        telemetry.addData("State", current_state);
        //telemetry.addData("LeftPosition", plowLeftPosition);
        //telemetry.addData("PlowRightPostition", plowRightPosition);

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

    public boolean runUntilColor(double min, double max, double motorPower, int failEncoder) {

        motorLeftRear.setPower(motorPower);
        motorRightRear.setPower(motorPower);
        motorLeftFront.setPower(motorPower);
        motorRightFront.setPower(motorPower);

        y = RGBSensor.argb()/1000000;

        if (y >= min && y <= max) {

            motorLeftRear.setPower(0);
            motorRightRear.setPower(0);
            motorLeftFront.setPower(0);
            motorRightFront.setPower(0);
            flag = true;

        }
        /*if (motorLeftRear.getCurrentPosition() >= failEncoder) {

            motorLeftRear.setPower(0);
            motorRightRear.setPower(0);
            motorLeftFront.setPower(0);
            motorRightFront.setPower(0);

        }*/
        y = RGBSensor.argb()/1000000;
        telemetry.addData("RGB", RGBSensor.argb() / 1000000);
        telemetry.addData("Distance", ODS.getLightDetectedRaw());
        telemetry.addData("Y", y);

        return flag;

    }

    public int lineFollow(double min, double max, boolean right) {
        y = RGBSensor.argb() / 1000000;
        if (y >= min && y <= max) {
            hit = true;
            flag1 = 2;
        }

            if (right && hit && !(y >= min && y <= max)) {
                flag1 = 0;
            }
            if (!right && hit && !(y >= min && y <= max)) {
                flag1 = 1;
            }

        return flag1;
    }

    public float scanForClosest() {
        float closest = 0;


        return closest;
    }

    public float Color_Run(int distance) {
        float y;
        if (distance < 4) {
            RGBSensor.enableLed(true);
            // float hsvValues[] = {6F, 6F, 6F};
            y = RGBSensor.argb();
            /*final float values[] = hsvValues;
            final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(R.id.RelativeLayout);
            relativeLayout.post(new Runnable() {
            public void run() {
            relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
            });
            Color.RGBToHSV(RGBSensor.red() * 8, RGBSensor.green() * 8, RGBSensor.blue() * 8, hsvValues);

            telemetry.addData("Distance_sensor_output", ODS);
            telemetry.addData("Distance_sensor_output_Raw", distance);
            //telemetry.addData("Object Reference ", RGBSensor.toString());
            telemetry.addData("Clear", RGBSensor.alpha());
            telemetry.addData("Red  ", RGBSensor.red());
            telemetry.addData("Blue", RGBSensor.blue());
            telemetry.addData("Green", RGBSensor.green());
            y = hsvValues[0];*/
        } else {
            RGBSensor.enableLed(false);
            y = 0;
        }
        return y;
    }
}


