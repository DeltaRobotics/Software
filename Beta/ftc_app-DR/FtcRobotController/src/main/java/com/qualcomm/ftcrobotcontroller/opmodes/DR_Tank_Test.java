package com.qualcomm.ftcrobotcontroller.opmodes;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Delta on 9/30/2015.
 */
public class DR_Tank_Test extends OpMode{

    public float accelX = 0;
    public float accelY = 0;
    public float accelZ = 0;

    public float deltaX;
    public float deltaY;
    public float deltaZ;

    public boolean sweeperF;
    public boolean sweeperR;

    public boolean skeeball = false;
    public boolean drive = true;

    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;

    Servo leftLever;
    Servo rightLever;

    DcMotor motorWinchLeft;
    DcMotor motorWinchRight;
    DcMotor motorWinchHelp;
    //DcMotor motorSweeper;

    Servo plowLeft;
    Servo plowRight;
    Servo catLeft;
    Servo winchAngle;

    Servo armVert;
    Servo armGrab;


    //ColorSensor RGBSensor;

    //ColorSensor colorSensor;
    double armVertPosition = 0.988;
    double armGrabPosition = 0.878;
    double armVertDelta = 0.005;
    double armGrabDelta = 0.005;
    double leftLeverPosition = 0.019;
    double rightLeverPosition = 0.878;
    double leftLeverDelta = 0.01;
    double rightLeverDelta = 0.01;
    double catLeftPosition  = 0.800;
    double catLeftDelta = 0.002;
    double catRightPosition  = 0.005;
    double catRightDelta = 0.002;
    double winchAngleDelta = 0.0005;
    double winchAnglePosition = .78;
    //Servo plowInOut;
    //Servo armColorSensor;

    double plowDelta = 0.005;
    double plowDeltaLeft = plowDelta;
    double plowDeltaRight = -plowDelta;
    double plowPositionLeft = 0.5;
    double plowPositionRight = 0.5;
    //double inOutPosition = 0.0;
    //double inOutDelta = 0.005;

    int rev = 3;
    int count = 0;
    //double armColorSensorPosition = 0.8;

    boolean speed_mode;
    //Initialize the Accelerometer
    private SensorManager mSensorManager;
    private Sensor accelerometer;



    public DR_Tank_Test() {

    }
    public void init() {

        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        //motorLeftRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        //motorLeftFront.setDirection(DcMotor.Direction.REVERSE);

        motorWinchLeft = hardwareMap.dcMotor.get("Winch_Motor_Left");
        motorWinchRight = hardwareMap.dcMotor.get("Winch_Motor_Right");
        motorWinchHelp = hardwareMap.dcMotor.get("Winch_Help");
        winchAngle = hardwareMap.servo.get("Winch_Angle");
        winchAngle.setPosition(winchAnglePosition);

        //motorSweeper = hardwareMap.dcMotor.get("Sweeper");

        catLeft = hardwareMap.servo.get("CatLeft");
        catLeft.setPosition(0.79);
        catLeftPosition = 0.79;

        leftLever = hardwareMap.servo.get("Left_Lever");
        leftLever.setPosition(leftLeverPosition);

        rightLever = hardwareMap.servo.get("Right_Lever");
        rightLever.setPosition(rightLeverPosition);

        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");

        armVert = hardwareMap.servo.get("Vertical_Arm");
        armVert.setPosition(armVertPosition);

        armGrab = hardwareMap.servo.get("Grabber_Arm");
        armGrab.setPosition(armGrabPosition);

        plowLeft.setPosition(0.957);
        plowRight.setPosition(0.047);
        //plowInOut.setPosition(.455);
        plowPositionLeft = 0.957;
        plowPositionRight = 0.047;



        speed_mode = true;

        //mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        //accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //sweeperF = false;
        //sweeperR = false;
    }

    public void start() {
        //mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //}

        //}
        // @Override
        //public void onAccuracyChanged(Sensor sensor, int accuracy) {
         }
        public void loop() {
            plowPositionLeft = Range.clip(plowPositionLeft, 0.1, 0.937);
            plowPositionRight = Range.clip(plowPositionRight, 0.047, 0.867);
            //inOutPosition = Range.clip(inOutPosition, 0.18, 0.78);

            catLeftPosition = Range.clip(catLeftPosition, 0.184, 0.800);
            //catRightPosition = Range.clip(catRightPosition, 0.09, 0.862);
            winchAnglePosition = Range.clip(winchAnglePosition, 0.002, .997);
            leftLeverPosition = Range.clip(leftLeverPosition, 0.076,0.924);
            rightLeverPosition = Range.clip(rightLeverPosition, 0.076,0.878);
            armGrabPosition = Range.clip(armGrabPosition, 0.01, 0.99);
            armVertPosition = Range.clip(armVertPosition, 0.0313, 0.99);
            double throttleLB = gamepad1.left_stick_y;
            double throttleRB = gamepad1.right_stick_y;
            double throttleLF = gamepad1.left_stick_y;
            double throttleRF = gamepad1.right_stick_y;
            double throttleWinch = gamepad2.right_stick_y;

            if (gamepad1.y) {
                speed_mode = true;
            }
            if (gamepad1.a) {
                speed_mode = false;
            }

            throttleLB = Range.clip(throttleLB, -1.0, 1.0);
            throttleRB = Range.clip(throttleRB, -1.0, 1.0);
            throttleLF = Range.clip(throttleLF, -1.0, 1.0);
            throttleRF = Range.clip(throttleRF, -1.0, 1.0);
            throttleWinch = Range.clip(throttleWinch, -1.0, 1.0);

            motorWinchLeft.setPower(-throttleWinch);
            motorWinchRight.setPower(throttleWinch);
            motorWinchHelp.setPower(-throttleWinch/3);
            //rightLever.setPower(-rightLeverThrottle);

            if(drive)
            {
                motorLeftRear.setPower(-throttleLB);
                motorLeftFront.setPower(-throttleLF);
                motorRightRear.setPower(-throttleRB);
                motorRightFront.setPower(-throttleRF);
            }
            else
            {
                motorLeftRear.setPower(throttleLB);
                motorLeftFront.setPower(throttleLF);
                motorRightRear.setPower(throttleRB);
                motorRightFront.setPower(throttleRF);
            }


            if (gamepad2.left_trigger > .5) {
                catLeftPosition -= catLeftDelta;
            }
            if (gamepad2.left_bumper) {
                catLeftPosition += catLeftDelta;
            }

            if (gamepad2.y) {
                rightLeverPosition -= rightLeverDelta;
            }
            if (gamepad2.a) {
                rightLeverPosition += rightLeverDelta;
            }

            if (gamepad2.dpad_up) {
               leftLeverPosition += leftLeverDelta;
            }
            if (gamepad2.dpad_down) {
                leftLeverPosition -= leftLeverDelta;
            }

            if (gamepad2.right_trigger > .2) {
                winchAnglePosition += winchAngleDelta;
            }
            if (gamepad2.right_bumper) {
                winchAnglePosition -= winchAngleDelta;
            }
            //if (count > 10)
            //{
            //    if(gamepad1.a) {
            //        sweeperF = !sweeperF;
            //        count = 0;
            //    }
            //    if(gamepad1.b) {
            //        sweeperR = !sweeperR;
            //        count = 0;
            //    }
            //}

            if (gamepad1.right_bumper)
            {
                plowPositionRight += plowDeltaRight;
            }
            if (gamepad1.right_trigger > .5)
            {
                plowPositionRight -= plowDeltaRight;
            }

            if (gamepad1.left_bumper)
            {
                plowPositionLeft += plowDeltaLeft;
            }
            if (gamepad1.left_trigger > .5)
            {
                plowPositionLeft -= plowDeltaLeft;
            }

            if(gamepad2.left_stick_y > 0.2)
            {
                armVertPosition += armVertDelta;
            }
            if(gamepad2.left_stick_y < -0.2)
            {
                armVertPosition -= armVertDelta;
            }

            if(gamepad2.x)
            {
                armGrabPosition += armGrabDelta;
            }
            if(gamepad2.b)
            {
                armGrabPosition -= armGrabDelta;
            }

            if(gamepad2.guide)
            {
                skeeball = !skeeball;
            }
            if(skeeball)
            {
                leftLeverDelta = 0.075;
                rightLeverDelta = 0.075;
            }
            else
            {
                leftLeverDelta = 0.01;
                rightLeverDelta = 0.01;
            }
            if(gamepad1.guide)
            {
                drive = !drive;
            }

            plowLeft.setPosition(plowPositionLeft);
            plowRight.setPosition(plowPositionRight);
            catLeft.setPosition(catLeftPosition);
            winchAngle.setPosition(winchAnglePosition);
            leftLever.setPosition(leftLeverPosition);
            rightLever.setPosition(rightLeverPosition);
            armGrab.setPosition(armGrabPosition);
            armVert.setPosition(armVertPosition);
           //if(sweeperF)
           //{
           //    motorSweeper.setPower(1.0);
           //}
           // else
           //{
           //    motorSweeper.setPower(0.0);
           //}
           //if(!sweeperF)
           //{
           //    if(sweeperR)
           //    {
           //        motorSweeper.setPower(-1.0);
           //    }
           //    else
           //    {
           //        motorSweeper.setPower(0.0);
           //    }
           //}

                //telemetry.addData("Left Rear:"
                //, +motorLeftRear.getPower()
                //+ motorLeftRear.getCurrentPosition());
                //telemetry.addData("Right Rear:"
                //,   +motorRightRear.getPower()
                //    + motorRightRear.getCurrentPosition());
                //telemetry.addData("Left Front:"
                //,   + motorLeftFront.getPower ()
                ///    + motorLeftFront.getCurrentPosition());
                //telemetry.addData("Right Front:"
                //,   + motorRightFront.getPower()
                //    + motorRightFront.getCurrentPosition()); */
            //telemetry.addData("Left Plow:", plowLeft.getPosition());
            //telemetry.addData("Right Plow:", plowRight.getPosition());
            //telemetry.addData("InOut Plow:", plowInOut.getPosition());
            count++;
            telemetry.addData("Left Lever", leftLever.getPosition());
            telemetry.addData("Right Lever", rightLever.getPosition());
            telemetry.addData("CatLeft:", catLeft.getPosition());
            telemetry.addData("Left Plow", plowLeft.getPosition());
            telemetry.addData("Right Plow", plowRight.getPosition());
            telemetry.addData("Arm Grabber", armGrab.getPosition());
            telemetry.addData("Arm Vertical", armVert.getPosition());
            telemetry.addData("Winch Angle", winchAngle.getPosition());
            telemetry.addData("rev", rev);
            telemetry.addData("skeeball", skeeball);
            telemetry.addData("drive", drive);
            //telemetry.addData("RBG",RGBSensor.argb());


            //telemetry.addData("X Accelerometer", accelX);
            //telemetry.addData("Y Accelerometer", accelY);
            //telemetry.addData("Z Accelerometer", accelZ);

        }
    //possible issue
//@Override
        //public void onSensorChanged(SensorEvent event)
        //{
          //  telemetry.addData ("ASensor", "Sensor Hit");
            //if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            //{
                 //get the change of the x,y,z values of the accelerometer
                //deltaX = Math.abs(lastX - event.values[0]);
                //deltaY = Math.abs(lastY - event.values[1]);
                //deltaZ = Math.abs(lastZ - event.values[2]);
                 //if the change is below 2, it is just plain noise
                //if (deltaX < 2)
                //    deltaX = 0;
                //if (deltaY < 2)
                //   deltaY = 0;
                //if (deltaZ < 2)
                //    deltaZ = 0;

                //accelX = deltaX;
                //accelY = deltaY;
                //accelZ = deltaZ;
            //}

          //  }
    double scaleInputRearHigh(double dVal) {
        double[] scaleArray = {0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00};

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        } else if (index > 16) {
            index = 16;
        }

        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        return dScale;
    }
    double scaleInputRearLow(double dVal){
        double[] scaleArray = {0.0, 0.0375, 0.0675, 0.075, 0.09, 0.1125, 0.135, 0.18,
                 0.225, 0.27, 0.3225, 0.375, 0.45, 0.54, 0.6375, 0.75, 0.75};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
    double scaleInputFrontHigh(double dVal){
        double[] scaleArray = {0.0, 0.025, 0.045, 0.05, 0.06, 0.075, 0.09, 0.12,
                0.15, 0.18, 0.215, 0.25, 0.30, 0.36, 0.425, 0.5, 0.5};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
    double scaleInputFrontLow(double dVal){
        double[] scaleArray = {0.0, 0.01875, 0.03375, 0.0375, 0.045, 0.05625, 0.0675, 0.09,
                0.1125, 0.135, 0.16125, 0.1875, 0.225, 0.27, 0.31875, 0.375, 0.375};

        int index = (int) (dVal * 16.0);
        if (index < 0) {
            index = -index;
        }
        else if (index > 16) {
            index = 16;
        }
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        }
        else {
            dScale = scaleArray[index];
        }
        return dScale;
    }
}
