package com.caitou.data;

import com.caitou.protocol.Protocol;

import java.io.Serializable;

/**
 * @className: BaseFrame
 * @classDescription: 基础帧
 * @Author: Guangzhao Cai
 * @createTime: 2016-06-29.
 */
public abstract class BaseFrame implements Serializable {
    public static final int CTRL_CLIENT_TO_SERVER = 0x01;
    public static final int CTRL_SERVER_TO_CLIENT = 0x02;

    public static final int FUNC_FRAME_1 = 0x03;
    public static final int FUNC_FRAME_2 = 0x04;
    public static final int FUNC_FRAME_3 = 0x05;

    public int head;            //帧头
    public int controlCode;     //控制码
    public int functionCode;    //功能码

    public void initHead(int controlCode, int functionCode){
        this.head = 0x55;
        this.controlCode = controlCode;
        this.functionCode = functionCode;
    }

    protected void paresFromHeader(Protocol.Header header) {
        this.controlCode = header.getControlCode();
        this.functionCode = header.getFunctionCode();
    }

    protected Protocol.Header getFrameHeader() {
        Protocol.Header.Builder headerBuilder = Protocol.Header.newBuilder();
        headerBuilder.setHead(this.head)
                .setControlCode(this.controlCode)
                .setFunctionCode(this.functionCode);
        return headerBuilder.build();
    }

    public abstract Protocol.Frame toFrame();
}
