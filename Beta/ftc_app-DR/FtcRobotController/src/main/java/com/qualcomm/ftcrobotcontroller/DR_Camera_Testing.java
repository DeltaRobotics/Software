package com.qualcomm.ftcrobotcontroller;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import android.graphics.Bitmap;
/**
 * Created by RoboticsUser on 12/29/2015.
 */
public class DR_Camera_Testing extends OpModeCamera {
    private int looped = 0;
    private int ds2 = 2;
    public void init()
    {
        setCameraDownsampling(2);
        super.init();
    }

}
