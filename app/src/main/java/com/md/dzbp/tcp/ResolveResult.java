package com.md.dzbp.tcp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/11/4.
 */

public class ResolveResult {
    public List<TCPMessage> NewMessagelist = new ArrayList<TCPMessage>();
    public byte[] OldDatagram;
}
