package com.delta.utilites;

/**
 * Created by RoboticsUser on 12/19/2015.
 */
public class GyroData {

    private float x;
    private float y;
    private float z;

    public GyroData() {
        x = 0;
        y = 0;
        z = 0;

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float val) {
        x = val;
    }

    public void setY(float val) {
        y = val;
    }

    public void setZ(float val){
        z = val;
    }


}
