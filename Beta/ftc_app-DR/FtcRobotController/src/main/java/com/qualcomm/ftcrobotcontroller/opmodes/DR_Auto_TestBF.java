package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Delta on 10/10/2015.
 */
public class DR_Auto_TestBF extends DR_Auto_Setup {

    Servo plowLeft;
    Servo plowRight;
    double SCA;
    double SCAdelta;
    double pL;
    double pLdelta;
    double pR;
    double pRdelta;
    double plowdelta;
    private int a_state = 0;

    @Override
    public void init() {
        plowLeft = hardwareMap.servo.get("Left_Plow");
        plowRight = hardwareMap.servo.get("Right_Plow");
        SCA = 0.0;
        SCAdelta = 0.05;
        plowdelta = 0.05;
        pL = 0.3;
        pLdelta = plowdelta;
        pR = 0.7;
        pRdelta = -plowdelta;
        plowLeft.setPosition(pL);
        plowRight.setPosition(pR);

    }
    public void loop(){
        SCA = Range.clip(SCA,0.05, 0.9);
        pL = Range.clip(pL, 0.05, 0.9);
        pR = Range.clip(pR, 0.05, 0.9);
        telemetry.addData("Encoder LR", motorLeftRear.getCurrentPosition());
        telemetry.addData("Encoder RR", motorRightRear.getCurrentPosition());
        telemetry.addData("Encoder LF", motorLeftFront.getCurrentPosition());
        telemetry.addData("Encoder RF", motorRightFront.getCurrentPosition());
        switch (a_state){
            case 0:
                reset_drive_encoders();
                run_using_encoders();
                a_state++;
                break;
            case 1:
                set_drive_power(0.5,0.5,0.5,0.5);
                if (has_LeftRear_encoder_reached(far1))
                {
                    set_drive_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 2:
                reset_drive_encoders();
                set_drive_power(0.5,-0.5,0.5,-0.5);
                if (has_LeftRear_encoder_reached(turn1))
                {
                    set_drive_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 3:
                reset_drive_encoders();
                set_drive_power(0.5,0.5,0.5,0.5);
                if (has_LeftRear_encoder_reached(far2))
                {
                    set_drive_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 4:
                reset_drive_encoders();
                set_drive_power(0.5,-0.5,0.5,-0.5);
                if (has_LeftRear_encoder_reached(turn2))
                {
                    set_drive_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 5:
                reset_drive_encoders();
                set_drive_power(0.5,0.5,0.5,0.5);
                if (DriveMotor_encoder_count() > climb1)
                {
                    set_drive_power(0.0,0.0,0.0,0.0);
                    sleep(500);
                    a_state++;
                }
                break;
            case 6:
                telemetry.addData("Stopped", 0.0);
        }

    }
}