package com.delta.utilites;

import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by RoboticsUser on 12/19/2015.
 *
 * Original code creaded by Adafruit Industries for the Adafruit_L3GD20 Arduino library
 *
 */
public class GyroSensor {

    final private byte GYRO_ADDRESS = (byte) 0xD7;
    final private byte CTRL_REG1 = 0x20;
    final private byte CTRL_REG2 = 0x21;
    final private byte CTRL_REG3 = 0x22;
    final private byte CTRL_REG4 = 0x23;
    final private byte CTRL_REG5 = 0x24;
    final private byte OUT_X_L = 0x28;
    final private byte OUT_X_H = 0x29;
    final private byte OUT_Y_L = 0x2A;
    final private byte OUT_Y_H = 0x2B;
    final private byte OUT_Z_L = 0x2C;
    final private byte OUT_Z_H = 0x2D;

    private GyroData data;
    private Wire gyroWire;
    private int measurementRange; //the measurement range of the sensor is 500 degrees per second (dps)

    /**
     * Constructs a new GyroSensor with a given measurement range
     */
    public GyroSensor(int given_range, HardwareMap hardwareMap, String name) {
        measurementRange = given_range;
        data = new GyroData();
        begin(measurementRange);
        gyroWire =  new Wire(hardwareMap, name, GYRO_ADDRESS);
    }

    public boolean begin(int range) {
        int readID = read8(CTRL_REG1);
        if (readID != GYRO_ADDRESS) {
            return false;
        }

        write8(CTRL_REG1, (byte) 0x0F);
        switch (range) {
            case 250:
                write8(CTRL_REG4, (byte) 0x00);
                break;
            default:
            case 500:
                write8(CTRL_REG4, (byte) 0x10);
                break;
            case 2000:
                write8(CTRL_REG4, (byte) 0x20);
                break;
        }
        return true;
    }

    //TODO Finish Javadoc for read()
    public void read() {

        int xh,xl, yh, yl, zh, zl;

        gyroWire.beginWrite(GYRO_ADDRESS);
        //TODO Understand what this is doing...
        gyroWire.write((byte) (OUT_X_H | 0x80)); // Make sure to set address auto-increment bit
        gyroWire.endWrite();
        gyroWire.requestFrom(GYRO_ADDRESS, (byte) 6);

        while (gyroWire.available()< 6);

        xl = gyroWire.read();
        xh = gyroWire.read();
        yl = gyroWire.read();
        yh = gyroWire.read();
        zl = gyroWire.read();
        zh = gyroWire.read();

        //TODO Document these functions
        data.setX((short) (xl | (xh << 8)));
        data.setY((short) (yl | (yh << 8)));
        data.setZ((short) (zl | (zh << 8)));

        switch (measurementRange) {
            case 250:
                data.setX(data.getX() * 250);
                data.setY(data.getY() * 250);
                data.setZ(data.getZ() * 250);
                break;
            case 500:
                data.setX(data.getX() * 500);
                data.setY(data.getY() * 500);
                data.setZ(data.getZ() * 500);
                break;
            case 2000:
                data.setX(data.getX() * 2000);
                data.setY(data.getY() * 2000);
                data.setZ(data.getZ() * 2000);
                break;
         }

        }
    //TODO Finish Javadoc for read8()
    private void write8(byte reg, byte val) {
        gyroWire.beginWrite(GYRO_ADDRESS);
        gyroWire.write((byte) reg);
        gyroWire.write(val);
        gyroWire.endWrite();
    }

    //TODO Finish Javadoc for read8()
    private int read8(byte reg) {
        int value;
        gyroWire.beginWrite(GYRO_ADDRESS);
        gyroWire.write((byte)reg);
        gyroWire.endWrite();
        gyroWire.requestFrom(GYRO_ADDRESS, (byte)1);
        value = gyroWire.read();
        return value;
    }

    public float getX() {
        return data.getX();
    }

    public float getY() {
        return data.getY();
    }

    public float getZ() {
        return data.getZ();
    }

}

