package com.caitou.socket;

import com.caitou.protocol.Protocol;

import java.io.Serializable;

/**
 * Transfer bean
 * @author swallow
 * @since 2016.6.2
 */
public class TransBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public byte[] data;

    public Protocol.Frame frame;
}
