package com.rentit.com.rentit.network;

import java.util.HashMap;

/**
 * Created by akhare on 06-Jul-15.
 */
public interface INetworkConnection {
    public void sendHttpRequestString(String url,HttpRequest.HttpMethod method, HttpRequest.IResponseCallBack callback);
}
