package com.rentit.com.rentit.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.rentit.projectr.MultipartRequest;
import com.rentit.projectr.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akhare on 08-Jul-15.
 */
public class NetworkConnection {
    /*singleton instance*/
    private static NetworkConnection mInstance;
    /* volley request queue*/
    private RequestQueue mRequestQueue;
    private Context mContext;
    private String TAG = NetworkConnection.class.getSimpleName();
    private ImageLoader mImageLoader;

    public static NetworkConnection getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkConnection(context);
        }
        return mInstance;
    }

    private NetworkConnection(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
        mContext = context;
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(20);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }

    public int getMethod(HttpRequest.HttpMethod methodType) {
        switch (methodType) {
            case GET:
                return Request.Method.GET;
            case POST:
                return Request.Method.POST;
            case PUT:
                return Request.Method.PUT;
            default:
                return Request.Method.GET;
        }
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
    public void addHttpStringRequest(final HttpRequest request) {
        StringRequest newVolleyReqest = new StringRequest(
                //Method type
                getMethod(request.getmMethodType()),
                //url for http request
                request.getmUrl(),
                // success response listener
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse " + response);
                        request.mCallback.onSuccess(new HashMap());
                    }
                },
                //error response listener
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onError" + error);
                    }
                }
        );
        mRequestQueue.add(newVolleyReqest);
    }

    public void addHttpJsonRequest(final HttpRequest request){
        JsonObjectRequest newVolleyReqest = new JsonObjectRequest(
                //method type of request
                getMethod(request.getmMethodType()),
                //url of request
                request.getmUrl(),
                //json request params
                request.getmJsonRequestParams(),
                //success listner
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"on response received.."+response);
                        HashMap<String,JSONObject>resultMap = new HashMap<>();
                        resultMap.put("result", response);
                        request.mCallback.onSuccess(resultMap);
                    }
                },
                //error listener
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"on error received.."+error);
                        request.mCallback.onFailure(error.toString());
                    }
                }
        );
        mRequestQueue.add(newVolleyReqest);
    }

    public void addHttpJsonArrayRequest(final HttpRequest request){
        JsonArrayRequest newVolleyRequest = new JsonArrayRequest(
                //method type of request
                getMethod(request.getmMethodType()),
                //url of request
                request.getmUrl(),
                //json request params
                request.getmJsonRequestParams(),
                //success listner
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG,"on response received.."+response);
                        HashMap<String,JSONArray>resultMap = new HashMap<>();
                        resultMap.put("result", response);
                        request.mCallback.onSuccess(resultMap);
                    }
                },
                //error listener
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "on error received.." + error);
                    }
                }
        );
        mRequestQueue.add(newVolleyRequest);
    }

    public void addHttpImageRequest(final String url, final ImageView targetImageView){
        ImageRequest imgRequest = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        targetImageView.setImageBitmap(response);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"unable to fetch image for url:"+url+" error:"+error);
                        targetImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.red_cross));
            }
        });
    }

    public void addMultiPartRequest(String url,Response.ErrorListener errorListener, Response.Listener<String> listener,
                                    File file,
                                    Map<String, String> mStringPart){
        MultipartRequest request = new MultipartRequest(url,errorListener,listener,file,mStringPart);
        mRequestQueue.add(request);

    }

}
