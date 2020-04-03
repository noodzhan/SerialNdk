package com.finger;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;
import android_serialport_api.callback.SerialPortCallBackUtils;
import android_serialport_api.util.ByteUtil;

/**
 * 串口封装
 */

public class MySerialPort {

    public  String TAG = "MySerialPort";

    /**
     * 标记当前串口状态(true:打开,false:关闭)
     **/
    public  static boolean isFlagSerial = false;

    public  SerialPort serialPort = null;
    public  static InputStream inputStream = null;
    public  static OutputStream outputStream = null;
    public  static String strData = "";

    /**
     * 打开串口
     */
    public  boolean open(String pathname, int baudrate, int flags) {
        boolean isopen = false;
        if (isFlagSerial) {
            return false;
        }
        try {
            serialPort = new SerialPort(new File(pathname), baudrate, flags);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            isopen = true;
            isFlagSerial = true;
        } catch (IOException e) {
            e.printStackTrace();
            isopen = false;
        }
        return isopen;
    }

    /**
     * 关闭串口
     */
    public  boolean close() {
        if (isFlagSerial) {
            return false;
        }
        boolean isClose = false;
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            isClose = true;
            isFlagSerial = false;//关闭串口时，连接状态标记为false
        } catch (IOException e) {
            e.printStackTrace();
            isClose = false;
        }
        return isClose;
    }

    /**
     * 发送串口指令
     */
    public  void sendString(String data) {
        if (!isFlagSerial) {
            return;
        }
        try {
            outputStream.write(ByteUtil.hex2byte(data));
            outputStream.flush();
            Log.d("send","发送成功："+data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 接收串口数据的方法
     */
    public  static void receive() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        if (!isFlagSerial) {
            return;
        }
        byte[] readData = new byte[32];
        if (inputStream == null) {
            return;
        }
        int size = 0;
        try {
            size = inputStream.read(readData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (size > 0 && isFlagSerial) {
            strData = ByteUtil.byteToStr(readData, size);
            Finger.respondStr = strData;
            Log.d("串口收到：", Finger.respondStr);
        }
    }
}