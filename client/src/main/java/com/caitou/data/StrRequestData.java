package com.caitou.data;

import com.caitou.protocol.Protocol;
import com.caitou.protocol.RequestProto;

import java.io.Serializable;

/**
 * @className:
 * @classDescription:
 * @Author: Guangzhao Cai
 * @createTime: 2016-07-01.
 */
public class StrRequestData extends BaseFrame implements Serializable{
    public Request request;
    public Response response;

    public StrRequestData(){
        this.request = new Request();
        this.response = new Response();
    }

    public StrRequestData create() {
        StrRequestData strRequestData = new StrRequestData();
        strRequestData.initHead(CTRL_CLIENT_TO_SERVER, FUNC_STRING);
        strRequestData.request.str = "I'm form client!";

        return strRequestData;
    }

    @Override
    public Protocol.Frame toFrame() {
        Protocol.Frame.Builder frameBuilder = Protocol.Frame.newBuilder();
        RequestProto.Request.Builder reqBuilder = RequestProto.Request.newBuilder();
        RequestProto.StringRequest.Builder strReqBuilder = RequestProto.StringRequest.newBuilder();

        reqBuilder.setStringRequest(strReqBuilder);
        frameBuilder.setHeader(getFrameHeader());
        frameBuilder.setRequest(reqBuilder);

        return frameBuilder.build();
    }

    public StrRequestData parseFrom(Protocol.Frame frame){
        StrRequestData strRequestData = new StrRequestData();
        strRequestData.paresFromHeader(frame.getHeader());
        strRequestData.response.rRtr = frame.getResponse().getStringResponse().getStrData();

        return strRequestData;
    }

    public static class Request {
        public String str;
    }

    public static class Response {
        public String rRtr;
    }
}
