package com.finger;


import static java.lang.Thread.sleep;

/**
 * 应答包
 * 主要功能：从应答包获取数据
 */
public class ResponsePacket {


    //验证回应码和命令是否匹配
    private static boolean isSame(String hexRespond ,String cmd){

        String getCmd = "0x";
        getCmd += hexRespond.substring(26,28);
        getCmd += hexRespond.substring(24,26);

        //System.out.println("getcmd= "+getCmd);

        if(getCmd.equals(cmd))
            return true;
        else
            return false;
    }



    /**
     * 得到应答包的确认码
     * @param hexRespond
     * @return 16进制的确认码 String
     */
    public static String getConfirmCode(String hexRespond){

        String confirmCode="0x";

        confirmCode += hexRespond.substring(36,38);
        //System.out.println(confirmCode);
        confirmCode += hexRespond.substring(34, 36);
        System.out.println("应答码："+confirmCode);

        return confirmCode;
    }




    /**
     * 得到应答包的数据长度 10进制
     * @param hexRespond
     * @return
     */
    private static int getDataLen(String hexRespond){

        String len="";

        len = hexRespond.substring(32,34);
        len += hexRespond.substring(30, 32);


        return Integer.parseInt(len,16);
    }


    /**
     * 得到应答包的数据
     * @return byte[]
     */
    private static byte[] getData(String hexRespond){

        int dataLen = getDataLen(hexRespond);
        int stringLen= hexRespond.length();
        if(dataLen>1024)
            return null;

        StringBuilder sb = new StringBuilder();


        for(int i=stringLen-4;i-2>=stringLen-dataLen*2;i-=2) {

            sb.append(hexRespond.substring(i-2, i));
            //System.out.println("test"+hexRespond.substring(i-2, i));
        }

        return HexStringBytesUtil.hex2byte(sb.toString());
    }

}
