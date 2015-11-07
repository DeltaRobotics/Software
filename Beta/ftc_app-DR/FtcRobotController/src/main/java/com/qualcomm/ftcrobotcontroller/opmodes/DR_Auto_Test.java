package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_Test extends DR_Auto_Setup {

    Servo armColorSensor;
    Servo plowLeft;
    Servo plowRight;
    double SCA;
    double SCAdelta;
    double pL;
    double pLdelta;
    double pR;
    double pRdelta;
    double plowPosition;
    double plowdelta;
    private int a_state = 0;

    @Override
    public void init() {
        armColorSensor = hardwareMap.servo.get("ColorSensor_Arm");
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        SCA = 0.0;
        SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.0;
        pLdelta = plowdelta;
        pR = 1.0;
        pRdelta = -plowdelta;
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);
        armColorSensor.setPosition(SCA);
    }
    public void loop(){
        SCA = Range.clip(SCA,0.05, 0.9);
        pL = Range.clip(pL, 0.05, 0.9);
        pR = Range.clip(pR, 0.05, 0.9);
        switch (a_state){
            case 0:
                reset_drive_encoders();
                a_state++;
                break;
            case 1:
                a_state++;
                break;
            case 2:
                /*armColorSensor.setPosition(SCA += SCAdelta);
                telemetry.addData("SCA", SCA);
                sleep(500);
                if (SCA >= 1)
                {
                    a_state++;
                }
                break;
                armColorSensor.setPosition(0.7);
                */
                plowLeft.setPosition(pL += pLdelta);
                plowRight.setPosition(pR += pRdelta);
                if (pL >= .9)
                {
                    a_state++;
                }
                break;
            case 3:
                sleep(500);
                //armColorSensor.setPosition(0.3);
                a_state++;
                break;
            case 4:
                telemetry.addData("plowLeft", pL);
                telemetry.addData("plowRight", pR);
                sleep(10000);


        }

    }
}