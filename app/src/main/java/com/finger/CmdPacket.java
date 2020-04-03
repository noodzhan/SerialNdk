package com.finger;


/**
 * 命令包
 * 主要功能：根据指令码生成命令
 */
public class CmdPacket {


    //设备地址
    static String devAddress = "FFFFFFFF";
    //连机密码
    static String connectPwd = "FFFFFFFF";
    //包标号
    static String Rsvd = "0000";
    //包标识
    static String packetId = "01";


    /**
     *  生成命令包
     * @param cmd  16进制的指令码
     * @param dataLength 10进制数据长度 <=1024
     * @param data  数据
     */
    public static String buildCmdPacket(String cmd,int dataLength,byte[] data){

        //去除0x
        cmd = cmd.substring(2);

        if(dataLength>1024||dataLength<0) {
            System.out.println("数据长度错误");
            return null;
        }

        StringBuilder re = new StringBuilder();

        //包头
        re.append("EF");
        re.append("02");

        //设备地址
        re.append(devAddress);

        //连机密码
        re.append(connectPwd);

        //包的标号
        re.append(Rsvd);

        //包的命令

        re.append(cmd.substring(2,4));//低8位
        re.append(cmd.substring(0,2));//高8位

        //包标识
        re.append(packetId);

        //数据长度
        String hex_dataLen = Integer.toHexString(dataLength);
        if(hex_dataLen.length()<4) {
            StringBuilder temp = new StringBuilder();
            for(int i=0;i<4-hex_dataLen.length();i++)
                temp.append("0");
            temp.append(hex_dataLen);
            hex_dataLen = temp.toString();
        }
        //System.out.println(hex_dataLen);
        re.append(hex_dataLen.substring(2, 4));
        re.append(hex_dataLen.substring(0, 2));

        //数据
        if(dataLength>0) {
            String convert=HexStringBytesUtil.bytes2HexString(data);
            //System.out.println("test:"+convert);
            //System.out.println(convert.length());
            for(int i=convert.length();i>0;i-=2) {
                //System.out.println("sub:"+convert.substring(i-2, i));
                re.append(convert.substring(i-2, i));
            }

        }

        //生成CRC校验位

        byte[] crc = HexStringBytesUtil.hex2byte(re.toString());


        //System.out.println(CRCUtil.CRC_16_XMODEM(crc, crc.length));
        re.append(CRCUtil.CRC_16_XMODEM(crc, crc.length).substring(2, 4));
        re.append(CRCUtil.CRC_16_XMODEM(crc, crc.length).substring(0, 2));


        return  re.toString();
    }

}
