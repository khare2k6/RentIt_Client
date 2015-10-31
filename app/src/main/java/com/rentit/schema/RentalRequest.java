package com.rentit.schema;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.projectr.BaseFragment;
import com.rentit.projectr.ListRentalRequestsFragment;
import com.rentit.projectr.PreferenceManager;
import com.rentit.projectr.RentalRequestAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akhare on 31-Jul-15.
 */
public class RentalRequest  implements Parcelable{
    @SerializedName("ownerId")private String mOwnerId;
    @SerializedName("productId") private Product mProduct;
    @SerializedName("tenantId") private String mTenantId;
    @SerializedName("state") private String mState;
    @SerializedName("rentalPeriod")private String mRentalPeriod;
    private ItemType mRequestType;
    private static final String TAG = RentalRequest.class.getSimpleName();

    protected RentalRequest(Parcel in) {
        mOwnerId = in.readString();
        mProduct = in.readParcelable(Product.class.getClassLoader());
        mTenantId = in.readString();
        mState = in.readString();
        mRentalPeriod = in.readString();
        mRequestType = (ItemType) in.readSerializable();
    }

    public enum RequestStatus{
        REQUEST_SENT,
        APPROVED,
        REJECTED,
        RENTED,
        CANCEL
    }
    public static final Creator<RentalRequest> CREATOR = new Creator<RentalRequest>() {
        @Override
        public RentalRequest createFromParcel(Parcel in) {
            return new RentalRequest(in);
        }

        @Override
        public RentalRequest[] newArray(int size) {
            return new RentalRequest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOwnerId);
        dest.writeParcelable(mProduct, flags);
        dest.writeString(mTenantId);
        dest.writeString(mState);
        dest.writeString(mRentalPeriod);
        dest.writeSerializable(mRequestType);
    }

    public enum ItemType{
        REQUEST_SENT_BY_ME("Requests"),
        REQUEST_PENDING_ON_ME("Approvals"),
        CREATE_PRODUCT("Create Product");

        private String mItemName;
        private ItemType(String name){
            mItemName = name;
        }
        public String getName(){
            return mItemName;
        }
    }


    public String getmState() {
        return mState;
    }

    public String getmRentalPeriod() {
        return mRentalPeriod;
    }

    public RentalRequest(String ownerId,Product pId,String tenantId,String state,ItemType reqType,String rentalPeriod){
        mOwnerId = ownerId;
        mProduct = pId;
        mTenantId = tenantId;
        mState = state;
        mRequestType = reqType;
        mRentalPeriod = rentalPeriod;
    }

    public String getmOwnerId() {
        return mOwnerId;
    }
    public ItemType getmRequestType() {
        return mRequestType;
    }
    public Product getmProduct() {
        return mProduct;
    }

    public String getmTenantId() {
        return mTenantId;
    }


    public static void getPendingApprovals(Context context , final IonRequestFetchStatusListener listener){
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        final JsonParser parser = new JsonParser();
        Log.d(RentalRequest.class.getSimpleName(),"from RetnalRequest");
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("ownerId",PreferenceManager.getmInstance(context).getUserName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setUrl(URLConstants.getmInstance().My_APPROVALS());
        builder.setJsonRequestParams(jsonObj);
        builder.setMethodType(HttpRequest.HttpMethod.POST);
        builder.setCallback(new HttpRequest.IResponseCallBack() {
            @Override
            public void onFailure(String msg) {
                Log.d(RentalRequest.class.getSimpleName(), "onFailure:" + msg);
                listener.onRequestsFetched(null);
            }

            @Override
            public void onSuccess(HashMap responseObjects) {
                JSONArray resultJson = (JSONArray) responseObjects.get("result");
                ArrayList<RentalRequest> rentalRequestsList = new ArrayList<RentalRequest>();
                ItemType reqType = ItemType.REQUEST_PENDING_ON_ME;
                // updateNavigationalDrawerItems(reqType.getName(),resultJson.length());
                for (int i = 0; i < resultJson.length(); i++) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = resultJson.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rentalRequestsList.add(parser.getRentalRequestFromJson(jsonObj,reqType));
                }

                listener.onApprovalsFetched(rentalRequestsList);
          }
        });
        NetworkConnection.getInstance(context).addHttpJsonArrayRequest(builder.getNewHttpRequest());
    }

    public static void getMyRequestStatus(Context context, final IonRequestFetchStatusListener listener) {
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        final JsonParser parser = new JsonParser();
        Log.d(RentalRequest.class.getSimpleName(),"from RetnalRequest");
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("tenantId", PreferenceManager.getmInstance(context).getUserName());
            //jsonObj.put("tenantId","thote");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setUrl(URLConstants.getmInstance().MY_REQUESTS());
        builder.setJsonRequestParams(jsonObj);
        builder.setMethodType(HttpRequest.HttpMethod.POST);
        builder.setCallback(new HttpRequest.IResponseCallBack() {
            @Override
            public void onFailure(String msg) {
                Log.d(RentalRequest.class.getSimpleName(), "onFailure:" + msg);
                listener.onRequestsFetched(null);
            }

            @Override
            public void onSuccess(HashMap responseObjects) {
                JSONArray resultJson = (JSONArray) responseObjects.get("result");
                ArrayList<RentalRequest> rentalRequestsList = new ArrayList<RentalRequest>();
                ItemType reqType = ItemType.REQUEST_SENT_BY_ME;
               // updateNavigationalDrawerItems(reqType.getName(),resultJson.length());
                for (int i = 0; i < resultJson.length(); i++) {
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = resultJson.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    rentalRequestsList.add(parser.getRentalRequestFromJson(jsonObj,reqType));
                }

                listener.onRequestsFetched(rentalRequestsList);

            }
        });
        NetworkConnection.getInstance(context).addHttpJsonArrayRequest(builder.getNewHttpRequest());
    }

    public static void updateRequestStatus(final Context context,RequestStatus newStatus,Product product,final BaseFragment.IonFragmentWorkDone callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("requestNewState", newStatus.name());
            jsonObject.put("productId", product.getmProductId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        builder.setJsonRequestParams(jsonObject)
                .setMethodType(HttpRequest.HttpMethod.POST)
                .setUrl(URLConstants.getmInstance().UPDATE_REQUEST_STATUS())
                .setCallback(new HttpRequest.IResponseCallBack() {
                    @Override
                    public void onFailure(String msg) {
                        Log.d(TAG,"error occured in updating status of request"+msg);
                        Toast.makeText(context,"Some error occured..",Toast.LENGTH_SHORT).show();
                        callback.onFragmentWorkDone(BaseFragment.ShowDialog.ERROR_OCCURED);
                    }

                    @Override
                    public void onSuccess(HashMap responseObjects) {
                        Toast.makeText(context,"Success!",Toast.LENGTH_SHORT).show();
                        callback.onFragmentWorkDone(BaseFragment.ShowDialog.REQUEST_STAUTS_UPDATED);
                    }
                });
        NetworkConnection.getInstance(context).addHttpJsonRequest(builder.getNewHttpRequest());
    }

    public static void sendRentalRequest(final Context context,RentalRequest request, final BaseFragment.IonFragmentWorkDone callback){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("productId", request.getmProduct().getmProductId());
            jsonObject.put("tenantId", request.getmTenantId());
            jsonObject.put("ownerId", request.getmOwnerId());
            jsonObject.put("rentalPeriod", request.getmRentalPeriod());
            Log.d(TAG,"sending req:"+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
            HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
            builder.setJsonRequestParams(jsonObject)
                    .setMethodType(HttpRequest.HttpMethod.POST)
                    .setUrl(URLConstants.getmInstance().REQUEST_RENTAL())
                    .setCallback(new HttpRequest.IResponseCallBack() {
                        @Override
                        public void onFailure(String msg) {
                            Log.d(TAG,"unable to request for rental:"+msg);
                            callback.onFragmentWorkDone(BaseFragment.ShowDialog.ERROR_OCCURED);
                        }

                        @Override
                        public void onSuccess(HashMap responseObjects) {
                            JSONObject obj = (JSONObject) responseObjects.get("results");
                            Log.d(TAG,"Rental request sent successfully");
                            Toast.makeText(context,"Request sent..",Toast.LENGTH_SHORT).show();
                            callback.onFragmentWorkDone(BaseFragment.ShowDialog.REQUEST_SENT);
                        }
                    });
        NetworkConnection.getInstance(context).addHttpJsonRequest(builder.getNewHttpRequest());
  }
    public interface IonRequestFetchStatusListener{
        public void onRequestsFetched( ArrayList<RentalRequest> rentalRequestsList);
        public void onApprovalsFetched( ArrayList<RentalRequest> rentalRequestsList);
    }
}
