package com.rentit.com.rentit.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rentit.projectr.PreferenceManager;

/**
 * Created by akhare on 08-Jul-15.
 */
public class URLConstants {
    private static URLConstants mInstance;
    private URLConstants(){

    }
    public static URLConstants getmInstance(){
        if(mInstance == null)
            mInstance = new URLConstants();
        return mInstance;
    }
    public  String SERVER_IP = "10.212.225.155";
    public  final String PROTOCOL = "http://";
    public  final String PORT = ":3000";
    public  String SERVER_URL;
    public  void getServerIpFromPreferences(Context context){
        String ip = PreferenceManager.getmInstance(context).getServerIp();
        Log.d("URLConstants:" ,"ip-->"+ ip);
        if(!TextUtils.isEmpty(ip)) {
            SERVER_IP = ip;
        }
        SERVER_URL = PROTOCOL+SERVER_IP/*+PORT*/;
    }


    public  String LOGIN_URL(Context context) {
        getServerIpFromPreferences(context);
        return SERVER_URL + "/tryLogin";
    }
    public  String LOGOUT_URL() { return SERVER_URL+"/logout";}
    public   String USER_INFO_URL() { return SERVER_URL+"/users/akhare";}
    public   String IMAGES(){ return SERVER_URL+"/public/images/";}
    public   String GET_ALL_PRODUCTS(){ return SERVER_URL+"/listAllProducts";}
    public   String GET_AVAIALBLE_PRODUCTS(){ return SERVER_URL+"/getProductListExceptRentals";}
    public   String GET_PRODUCT_BY_ID(){ return SERVER_URL+"/product/getProductById";}
    public   String CREATE_PRODUCT(){ return SERVER_URL+"/product/createProduct";}
    public   String MY_REQUESTS() { return SERVER_URL+"/getRentalRequestForTenant";}
    public   String My_APPROVALS(){ return SERVER_URL+"/getRentalRequestForOwner";}
    public   String REQUEST_RENTAL(){ return SERVER_URL+"/product/requestRental";}
    public   String UPDATE_REQUEST_STATUS(){ return SERVER_URL+"/updateReuqestStatus";}
    public   String UPLOAD_PHOTO(){return SERVER_URL+"/api/photo";}

}
