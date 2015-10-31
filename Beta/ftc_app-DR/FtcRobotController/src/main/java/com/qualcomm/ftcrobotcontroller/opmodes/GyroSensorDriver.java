package com.qualcomm.ftcrobotcontroller.opmodes;
import android.location.Address;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceReader;

/**
 * Created by RoboticsUser on 10/31/2015.
 */

public class GyroSensorDriver extends I2cDevice {

    private ArrayQueue<I2cTransfer>transferQueue;
    private final int ADDRESS = 0x29;
    private I2cDevice gyroDevice;
    private int gyroDeviceAddress;

    public AdafruitGyroSensor (HardwareMap hardwareMap, String deviceName)
    {
        transferQueue = new ArrayQueue<I2cTransfer>();
        gyroDevice = hardwareMap.i2cDevice.get(deviceName);
        gyroDeviceAddress = 2*ADDRESS;

        rCache = gyroDevice.getI2cReadCache();
        wCache = gyroDevice.getI2cWriteCache();
        rLock = gyroDevice.getI2cReadCacheLock();
        wLock = gyroDevice.getI2cWriteCacheLock();

        addWriteRequest(ENABLE,)

    }
public void init(){}
public void loop(){}
}