package com.finger;

public  class CmdRtState {

    public static final String CMD_RT_OK                        = "0x0000";   //指令执行完毕或OK
    public static final String CMD_RT_PACKGE_ERR                = "0x0001";   //数据包接收错误
    public static final String CMD_RT_DEVICE_ADDRESS_ERR        = "0x0002";   //设备地址错误
    public static final String CMD_RT_COM_PASSWORD_ERR          = "0x0003";   //通信密码错误

    public static final String CMD_RT_NO_FINGER                 = "0x0004";   //传感器上没有手指
    public static final String CMD_RT_GET_IMAGE_FAILE           = "0x0005";   //从传感器上获取图像失败
    public static final String CMD_RT_GEN_CHAR_ERR              = "0x0006";   //生成特征失败
    public static final String CMD_RT_FINGER_MATCH_ERR          = "0x0007";   //指纹不匹配
    public static final String CMD_RT_FINGER_SEARCH_FAILE       = "0x0008";   //没搜索到指纹
    public static final String CMD_RT_MERGE_TEMPLET_FAILE       = "0x0009";   //特征合并失败
    public static final String CMD_RT_ADDRESS_OVERFLOW          = "0x000A";   //将模板存库时地址序号超出指纹库范围
    public static final String CMD_RT_READ_TEMPLET_ERR          = "0x000B";   //从指纹库读模板出错
    public static final String CMD_RT_UP_TEMPLET_ERR            = "0x000C";   //上传特征失败
    public static final String CMD_RT_UP_IMAGE_FAILE            = "0x000D";   //上传图像失败
    public static final String CMD_RT_DELETE_TEMPLET_ERR        = "0x000E";   //删除模板失败
    public static final String CMD_RT_CLEAR_TEMPLET_LIB_ERR     = "0x000F";   //清空指纹库失败
    public static final String CMD_RT_FINGER_NOT_MOVE           = "0x0010";   //残留指纹或传感器窗口的按指长时间未移开
    public static final String CMD_RT_NO_TEMPLET_IN_ADDRESS     = "0x0011";   //指定位置没有有效模板
    public static final String CMD_RT_CHAR_REPEAT               = "0x0012";   //指纹重复，需要注册的指纹已经在FLASH中注册
    public static final String CMD_RT_MB_NOT_EXIST_IN_ADDRESS   = "0x0013";   //该地址中不存在指纹模板
    public static final String CMD_RT_GET_MBINDEX_OVERFLOW      = "0x0014";   //获取模板索引长度溢出

    public static final String CMD_RT_SET_BAUD_RATE_FAILE       = "0x0015";   //设置波特率失败
    public static final String CMD_RT_ERASE_FLAG_FAILE          = "0x0016";   //擦除程序标志失败
    public static final String CMD_RT_SYSTEM_RESET_FAILE        = "0x0017";   //系统复位失败
    public static final String CMD_RT_OPERATION_FLASH_ERR       = "0x0018";   //操作FLASH出错
    public static final String CMD_RT_NOTE_BOOK_ADDRESS_OVERFLOW= "0x0019";   //记事本地址溢出
    public static final String CMD_RT_PARA_ERR                  = "0x001A";   //设置参数错误
    public static final String CMD_RT_NO_CMD                    = "0x001B";   //命令不存在



    public static final String CMD_RT_TERMINATE_OK              = "0x001C";   //终止成功
    public static final String CMD_RT_AUTOLOGIN_OK1             = "0x001D";   //第一次采集指纹成功
    public static final String CMD_RT_AUTOLOGIN_OK2             = "0x001E";   //第一次采集指纹成功
}
