package com.finger;


import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Finger{

    public  MySerialPort mySerialport;
    //串口接受到的数据
    public static String respondStr = null;
    public boolean serialIsOpen = false;


    public  Finger(String dev,int baudrate ,int flag){
        mySerialport =new MySerialPort();
        this.init(dev,baudrate,flag);
    }

    /**
     *  初始化
     * @param dev 串口设备
     * @param baudrate 波特率
     * @param flag  默认 0
     */
    public void init(String dev,int baudrate ,int flag){
        if(!mySerialport.open(dev,baudrate,flag)){
            return ;
        }
        serialIsOpen = true;
    }


    /**
     * 注册指纹
     * @param seconds 最大注册时间
     * @return int
     *  return -1 操作执行失败
     *  return -2 模板库里含有该人指纹
     *  return  >=0  注册成功，该值便是指纹的索引
     */
    public int registerFinger(int seconds){

        int re = -1;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //创建搜索指纹任务
        MyRegiterTask task = new MyRegiterTask();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        executor.execute(futureTask);
        try {
            re = futureTask.get(seconds,TimeUnit.SECONDS);

        }catch (Exception e) {
            System.out.println("超时");
            task.isRunning = false;
            re = -2;
        }finally {
            futureTask.cancel(true);
            executor.shutdown();
        }
        Log.d("索引：",re+"");

        return re;
    }

    /**
     * 搜索指纹
     * @param seconds 允许最大的搜索时间
     * @return  int类型
     * 成功 ，返回指纹索引地址 >=0
     * 失败 ，返回 -1 （没有指纹）
     * 超时 ，返回 -2 （操作失败）
     */
    public int serachFinger(final int seconds){

        int re = -1;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        //创建搜索指纹任务
        MySerachTask task = new MySerachTask();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        executor.execute(futureTask);
        try {
            re = futureTask.get(seconds,TimeUnit.SECONDS);

        }catch (Exception e) {
            System.out.println("超时");
            task.isRunning = false;
            re = -2;
        }finally {
            futureTask.cancel(true);
            executor.shutdown();
        }
        Log.d("索引：",re+"");

        return re;
    }


    /**
     * 删除指定索引的指纹
     * @param index
     * @return
     */
    public boolean deleteOneFinger(int index){

        boolean re = false;

        //十进制的数转化成byte[]
        byte[] convert = new byte[2];
        convert[0] = (byte)((index >> 8) & 0xFF);
        convert[1] = (byte)(index & 0xFF);


        mySerialport.sendString(CmdPacket.buildCmdPacket("0x0329",2,convert));

        mySerialport.receive();
        if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
        {
            re  = true;
            Log.d("删除指定指纹：","成功");
        }

        return re;

    };


    /**
     * 删除所有指纹
     * @return
     */
    public boolean deleteAllFingers(){


        boolean re = false;

        mySerialport.sendString(CmdPacket.buildCmdPacket("0x032A",0,new byte[]{0x00}));

        mySerialport.receive();
        if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
        {
            re  = true;
            Log.d("删除所有指纹：","成功");
        }

        return re;

    };


    /**
     * 不用指纹仪的时候要关闭资源
     * 关闭资源 （串口资源）
     * @return
     */
    public boolean close(){
        return mySerialport.close();
    }





















    //用于注册指纹任务
    class MyRegiterTask implements Callable<Integer>{

        boolean isRunning = true;

        boolean getRedressImage1 = false;
        boolean getRedressImage2= false;
        boolean getRedressImage3 = false;

        boolean genChar1 = false;
        boolean genChar2 = false;
        boolean genChar3 = false;

        boolean mergeChars = false;
        boolean getFreeIndex = false;
        boolean storeChar = false;

        byte [] freeIndex = new byte[4];
        String freeIndex_string = null;


        @Override
        public Integer call() {

            while(!getRedressImage1&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0323",1,new byte[]{0x00}));
                mySerialport.receive();

                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    getRedressImage1  = true;
                    Log.d("采集矫正图像：","A成功");
                }
            }

            while(!genChar1&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0325",4,new byte[]{0x00,0x00,0x00,0x01}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    genChar1  = true;
                    Log.d("生成特征值：","A成功");
                }
            }



            while(!getRedressImage2&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0323",1,new byte[]{0x00}));
                mySerialport.receive();

                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    getRedressImage2  = true;
                    Log.d("采集矫正图像：","B成功");
                }
            }
            while(!genChar2&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0325",4,new byte[]{0x00,0x00,0x00,0x02}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    genChar2  = true;
                    Log.d("生成特征值：","B成功");
                }
            }

            while(!getRedressImage3&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0323",1,new byte[]{0x00}));
                mySerialport.receive();

                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    getRedressImage3  = true;
                    Log.d("采集矫正图像：","C成功");
                }
            }
            while(!genChar3&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0325",4,new byte[]{0x00,0x00,0x00,0x03}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    genChar3  = true;
                    Log.d("生成特征值：","C成功");
                }
            }
            while(!mergeChars&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0336",0,new byte[]{0x00}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK)) {
                    mergeChars  = true;
                    Log.d("生成特征值：","ABC成功");
                }
            }

            //获取第一个空闲索引
            while(!getFreeIndex&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0344",0,new byte[]{0x00}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    getFreeIndex  = true;
                    Log.d("获取第一个空闲索引：","成功");
                    freeIndex_string = Finger.respondStr.substring(40,42)+
                            Finger.respondStr.substring(38,40);
                }

            }



            while(!storeChar&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0327",2,HexStringBytesUtil.hex2byte(freeIndex_string)));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK)) {
                    mergeChars  = true;
                    Log.d("存储模板：","成功");
                    return Integer.parseInt(freeIndex_string,16);
                }else if (ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_CHAR_REPEAT)){

                    //指纹重复
                    Log.d("存储模板：","指纹重复");
                    return -2;
                }
            }

            return -1;
        }
    }




    //主要用于搜索指纹
    class MySerachTask implements Callable<Integer> {
        boolean isRunning = true;
        int index = -2;

        public int getIndex() {

            String index_string = respondStr.substring(40,42)+respondStr.substring(38,40);
            String hava_finger = respondStr.substring(42, 46);

            if (hava_finger.equals("6400")){
                index = Integer.parseInt(index_string,16);
            }

            return index;
        }

        @Override
        public Integer call() throws Exception {

            boolean getRedressImageOK = false;
            boolean genCharOK = false;
            boolean serachOK = false;
            while(!getRedressImageOK&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0323",1,new byte[]{0x00}));
                mySerialport.receive();

                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                    getRedressImageOK  = true;
            }
            //Log.d("采集矫正图像：","成功");
            while(!genCharOK&&isRunning){

                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0325",4,new byte[]{0x00,0x00,0x00,0x01}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                    genCharOK  = true;
            }
            //Log.d("生成特征值：","成功");
            while(!serachOK&&isRunning){
                mySerialport.sendString(CmdPacket.buildCmdPacket("0x0328",4,new byte[]{0x00,0x00,0x00,0x01}));
                mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {
                    serachOK  = true;
                    return getIndex();
                }else if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_FINGER_SEARCH_FAILE)){

                    return -1;
                }
            }
            //Log.d("搜索指纹：","成功");
            return -2;
        }
    }

}
