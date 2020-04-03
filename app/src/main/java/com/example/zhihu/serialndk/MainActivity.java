package com.example.zhihu.serialndk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.finger.CmdPacket;
import com.finger.CmdRtState;
import com.finger.Finger;
import com.finger.ResponsePacket;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortUtil;
import android_serialport_api.callback.SerialCallBack;
import android_serialport_api.callback.SerialPortCallBackUtils;
import android_serialport_api.util.*;

import static java.lang.Thread.*;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button btn_register;
    private Button btn_serach;
    private Button btn_deleteOne;
    private Button btn_deleteALL;
    private Button btn_testSend;
    private Button btn_testSend1;
    private Button btn_testSend2;


    //在需要用到指纹仪的活动里，把它作为成员变量
    private Finger finger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //创建指纹仪类
        finger=new Finger("/dev/ttyS2",115200,0);

    }
    private void initView() {
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_serach = (Button) findViewById(R.id.btn_serach);
        btn_deleteOne = (Button) findViewById(R.id.btn_deleteOne);
        btn_deleteALL = (Button) findViewById(R.id.btn_deleteALL);
        btn_testSend = (Button) findViewById(R.id.btn_testSend);
        btn_testSend1 = (Button) findViewById(R.id.btn_testSend1);
        btn_testSend2 = (Button) findViewById(R.id.btn_testSend2);



        btn_register.setOnClickListener(this);
        btn_serach.setOnClickListener(this);
        btn_deleteOne.setOnClickListener(this);
        btn_deleteALL.setOnClickListener(this);
        btn_testSend.setOnClickListener(this);
        btn_testSend1.setOnClickListener(this);
        btn_testSend2.setOnClickListener(this);


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_register:
                finger.registerFinger(15);
                break;

            case R.id.btn_serach:
                finger.serachFinger(5);
                break;


            case R.id.btn_deleteOne:
                finger.deleteOneFinger(0);
                break;

            case R.id.btn_deleteALL:
                finger.deleteAllFingers();

                break;
            case R.id.btn_testSend:
                final int COUNT = 10;
                boolean getRedressImageOK = false;
                boolean genCharOK = false;
                boolean serachOK = false;
                for(int i=0;i<COUNT;i++){
                    finger.mySerialport.sendString(CmdPacket.buildCmdPacket("0x0323",1,new byte[]{0x00}));
                    finger.mySerialport.receive();
                    if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK)){
                        getRedressImageOK  = true;
                        Log.d("采集矫正图像：","成功");
                    }
                    if(getRedressImageOK)
                        break;

                }



                for(int i=0;i<COUNT;i++){
                    finger.mySerialport.sendString(CmdPacket.buildCmdPacket("0x0325",4,new byte[]{0x00,0x00,0x00,0x01}));
                    finger.mySerialport.receive();
                    if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                        genCharOK  = true;
                    if(genCharOK)
                        break;

                }
                Log.d("生成特征值：","成功");

                for(int i=0;i<COUNT;i++){
                    finger.mySerialport.sendString(CmdPacket.buildCmdPacket("0x0328",4,new byte[]{0x00,0x00,0x00,0x01}));
                    finger.mySerialport.receive();
                    if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                        serachOK  = true;
                    if(genCharOK)
                        break;

                }
                Log.d("搜索指纹：","成功");




                Log.d("de","点击1");

                break;

            case R.id.btn_testSend1:

                finger.mySerialport.sendString(CmdPacket.buildCmdPacket("0x0344",0,new byte[]{0x00}));
                finger.mySerialport.receive();
                if(ResponsePacket.getConfirmCode(Finger.respondStr).equals(CmdRtState.CMD_RT_OK))
                {

                    Log.d("获取第一个空闲索引：","成功"+Finger.respondStr.substring(40,42)+
                            Finger.respondStr.substring(38,40));

                   ;
                }
                Log.d("de","点击2");

                break;

            case R.id.btn_testSend2:

                finger.mySerialport.close();
                break;

        }
    }

}
