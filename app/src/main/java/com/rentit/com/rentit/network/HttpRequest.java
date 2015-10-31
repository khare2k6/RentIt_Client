package com.rentit.com.rentit.network;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * HTTP request class.
 * Created by akhare on 08-Jul-15.
 */
public class HttpRequest {

    String mUrl;
    HttpMethod mMethodType;
    IResponseCallBack mCallback;
    JSONObject mJsonRequestParams;

    public IResponseCallBack getmCallback() {
        return mCallback;
    }

    public String getmUrl() {
        return mUrl;
    }

    public HttpMethod getmMethodType() {
        return mMethodType;
    }

    public JSONObject getmJsonRequestParams() { return mJsonRequestParams; }

    /**
     * Specifies type of http method
     */
    public enum HttpMethod{
        GET,
        POST,
        PUT
    };

    /**
     * Each http request will pass its call back when response is received with
     * corresponding object
     * @param <T>
     */
    public  interface IResponseCallBack<T>{
        public void onFailure(String msg);
        public void onSuccess(HashMap<String,T> responseObjects);
    }



    /**
     * Constructor of http request
     */
    public HttpRequest(HttpRequestBuilder builder){
        mUrl = builder.Url;
        mMethodType = builder.MethodType;
        mCallback = builder.Callback;
        mJsonRequestParams = builder.JsonRequestParams;
    }

    public static class HttpRequestBuilder{
        String Url;
        HttpMethod MethodType;
        IResponseCallBack Callback;
        JSONObject JsonRequestParams;

        public HttpRequestBuilder setUrl(String url) {
            Url = url;
            return this;
        }

        public HttpRequestBuilder setMethodType(HttpMethod methodType) {
            MethodType = methodType;
            return this;
        }

        public HttpRequestBuilder setCallback(IResponseCallBack callback) {
            Callback = callback;
            return this;
        }

        public HttpRequestBuilder setJsonRequestParams(JSONObject jsonRequestParams) {
            JsonRequestParams = jsonRequestParams;
            return this;
        }

        public HttpRequest getNewHttpRequest(){
            return new HttpRequest(this);
        }


    }
}
