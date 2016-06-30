package com.caitou.data;

import com.caitou.protocol.Protocol;
import com.caitou.protocol.RequestProto;
import com.caitou.protocol.ResponseProto;

import java.io.Serializable;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-06-29.
 */
public class IntRequestData extends BaseFrame implements Serializable {
    public Request request;
    public Response response;

    public IntRequestData() {
        this.request = new Request();
        this.response = new Response();
    }

    public static IntRequestData create() {
        IntRequestData intRequestData = new IntRequestData();

        intRequestData.initHead(CTRL_CLIENT_TO_SERVER, FUNC_FRAME_1);
        intRequestData.request.int32 = 0x11;
        intRequestData.request.int64 = 0xff;

        return intRequestData;
    }

    public static IntRequestData parseFrom(Protocol.Frame frame){
        IntRequestData ird = new IntRequestData();
        ird.paresFromHeader(frame.getHeader());

        ResponseProto.Response response = frame.getResponse();
        ResponseProto.IntResponse intResponse = response.getIntResponse();

        ird.response.rInt32 = intResponse.getInt32Data();
        ird.response.rInt64 = intResponse.getInt64Data();

        return ird;
    }

    @Override
    public Protocol.Frame toFrame() {
        Protocol.Frame.Builder frameBuilder = Protocol.Frame.newBuilder();
        RequestProto.Request.Builder reqBuilder = RequestProto.Request.newBuilder();
        RequestProto.IntRequest.Builder intReqBuilder = RequestProto.IntRequest.newBuilder();

        reqBuilder.setIntRequest(intReqBuilder);
        frameBuilder.setHeader(getFrameHeader());
        frameBuilder.setRequest(reqBuilder);

        return frameBuilder.build();
    }

    public static class Request {
        public int int32;
        public long int64;
    }

    public static class Response {
        public int rInt32;
        public long rInt64;
    }

}
