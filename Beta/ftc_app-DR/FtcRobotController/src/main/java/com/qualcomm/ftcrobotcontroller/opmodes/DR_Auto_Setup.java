package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.util.Range;
/**
 * Created by Delta on 10/8/2015.
 */
public class DR_Auto_Setup extends OpMode {


    DcMotor motorLeftRear;
    DcMotor motorRightRear;
    DcMotor motorLeftFront;
    DcMotor motorRightFront;


    public DR_Auto_Setup() {

    }
    @Override public void init() {
        //Gets all four drive motors, and reverses the direction of both right motors. This allows values to be put in the same on each side.

        motorLeftRear = hardwareMap.dcMotor.get("Drive_Left_Rear");
        motorRightRear = hardwareMap.dcMotor.get("Drive_Right_Rear");
        motorRightRear.setDirection(DcMotor.Direction.REVERSE);
        motorLeftFront = hardwareMap.dcMotor.get("Drive_Left_Front");
        motorRightFront = hardwareMap.dcMotor.get("Drive_Right_Front");
        motorRightFront.setDirection(DcMotor.Direction.REVERSE);
    }
    public void loop()
    {

    }
    public void reset_drive_encoders()
    //Reset all four drive motors, if they are connected. If a motor is not connected (i.e. reads null), it will not be reset.
    {
        if (motorLeftRear != null)  //if the motor is connected
        {
            motorLeftRear.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS); //Reset the encoder count
        }
        if (motorRightRear != null)
        {
            motorRightRear.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        if (motorLeftFront != null)
        {
            motorLeftFront.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
        if (motorRightFront != null)
        {
            motorRightFront.setChannelMode(DcMotorController.RunMode.RESET_ENCODERS);
        }
    }
    public void run_using_encoders()
            //turns on the encoders/starts reading them
    {
        if (motorLeftRear != null && motorRightRear != null && motorLeftFront != null && motorRightFront != null)
        {
            motorLeftRear.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorRightRear.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorLeftFront.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
            motorRightFront.setChannelMode(DcMotorController.RunMode.RUN_USING_ENCODERS);
        }

    }

    int LeftRear_encoder_count()
    //Returns the encoder position of the motor.
    {
        int LR_return = 0;
        if (motorLeftRear != null)
        {
            LR_return = motorLeftRear.getCurrentPosition();
        }
        return LR_return;
    }
    int RightRear_encoder_count()
    //Returns the encoder position of the motor.
    {
        int RR_return = 0;
        if (motorRightRear != null)
        {
            RR_return = motorRightRear.getCurrentPosition();
        }
        return RR_return;
    }
    int LeftFront_encoder_count()
    //Returns the encoder position of the motor.
    {
        int LF_return = 0;
        if (motorLeftFront != null)
        {
            LF_return = motorLeftFront.getCurrentPosition();
        }
        return LF_return;
    }
    int RightFront_encoder_count()
    //Returns the encoder position of the motor.
    {
        int RF_return = 0;
        if (motorRightFront != null)
        {
            RF_return = motorRightFront.getCurrentPosition();
        }
        return RF_return;
    }
    int DriveMotor_encoder_count()
    //Computes the average of the four drive motors' encoder counts.
    {
        int DM_return = 0;
        if (motorLeftRear != null && motorRightRear != null && motorLeftFront != null && motorRightFront != null)
        {
            DM_return = ((LeftRear_encoder_count() + RightRear_encoder_count() + LeftFront_encoder_count() + RightFront_encoder_count())/4);
        }
        return DM_return;
    }
    boolean has_LeftRear_encoder_reached(double e_count)
    //Has the left rear reached the value of the encoder that was designated?
        {
        boolean returnLRe = false;
        if (motorLeftRear != null)
        {
            if (Math.abs(motorLeftRear.getCurrentPosition()) > e_count)
            {
                returnLRe = true;
            }
        }
        return returnLRe;
    }
    boolean has_RightRear_encoder_reached(double e_count)
    //Has the right rear reached the value of the encoder that was designated?
    {
        boolean returnRRe = false;
        if (motorRightRear != null)
        {
            if (Math.abs(motorRightRear.getCurrentPosition()) > e_count)
            {
                returnRRe = true;
            }
        }
        return returnRRe;
    }
    boolean has_LeftFront_encoder_reached(double e_count)
    //Has the left front reached the value of the encoder that was designated?
    {
        boolean returnLFe = false;
        if (motorLeftFront != null)
        {
            if (Math.abs(motorLeftFront.getCurrentPosition()) > e_count)
            {
                returnLFe = true;
            }
        }
        return returnLFe;
    }
    boolean has_RightFront_encoder_reached(double e_count)
    //Has the right front reached the value of the encoder that was designated?
    {
        boolean returnRFe = false;
        if (motorRightFront != null)
        {
            if (Math.abs(motorRightFront.getCurrentPosition()) > e_count)
            {
                returnRFe = true;
            }
        }
        return returnRFe;
    }

    boolean have_drive_encoders_reached(double e_countLR, double e_countRR, double e_countLF, double e_countRF)
    //Have the drive encoders reached their designated value?
    {
        boolean returnAMe = false;
        if(has_LeftRear_encoder_reached(e_countLR) && has_RightRear_encoder_reached(e_countRR) &&
           has_LeftFront_encoder_reached(e_countLF) && has_RightFront_encoder_reached(e_countRF))
        {
            returnAMe = true;
        }
        return returnAMe;
    }
    void set_drive_power(double LR_power, double RR_power, double LF_power, double RF_power)
    //Set the power for each motor
    {
        if(motorLeftRear != null)
        {
            motorLeftRear.setPower(LR_power);
        }
        if(motorRightRear != null)
        {
            motorRightRear.setPower(RR_power);
        }
        if(motorLeftFront != null)
        {
            motorLeftFront.setPower(LF_power);
        }

        if(motorRightFront != null)
        {
            motorRightFront.setPower(RF_power);
        }
    }
    boolean drive_using_encoders(double LR_power, double RR_power, double LF_power, double RF_power,
                                 double e_countLR, double e_countRR, double e_countLF, double e_countRF)
    ///Drive with encoders and indicate if the encoder count is reached.
    {
        boolean Preturn = false;
        run_using_encoders();
        set_drive_power(LR_power, RR_power, LF_power, RF_power);
        if(have_drive_encoders_reached(e_countLR, e_countRR, e_countLF, e_countRF))
        {
            reset_drive_encoders();
            set_drive_power(0.0f, 0.0f, 0.0f, 0.0f);
            Preturn = true;
        }
        return Preturn;
    }
    public static void sleep(int amt) // In milliseconds
    {
        double a = System.currentTimeMillis();
        double b = System.currentTimeMillis();
        while ((b - a) <= amt)
        {
            b = System.currentTimeMillis();
        }
    }
    float scale_motor_power (float scale_power)
    {
        //
        // Assume no scaling.
        //
        float l_scale = 0.0f;

        //
        // Ensure the values are legal.
        //
        float l_power = Range.clip (scale_power, -1, 1);

        float[] l_array =
                { 0.00f, 0.05f, 0.09f, 0.10f, 0.12f
                        , 0.15f, 0.18f, 0.24f, 0.30f, 0.36f
                        , 0.43f, 0.50f, 0.60f, 0.72f, 0.85f
                        , 1.00f, 1.00f
                };

        //
        // Get the corresponding index for the specified argument/parameter.
        //
        int l_index = (int)(l_power * 16.0);
        if (l_index < 0)
        {
            l_index = -l_index;
        }
        else if (l_index > 16)
        {
            l_index = 16;
        }

        if (l_power < 0)
        {
            l_scale = -l_array[l_index];
        }
        else
        {
            l_scale = l_array[l_index];
        }

        return l_scale;

    }







    }


