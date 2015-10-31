package com.rentit.projectr;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;

import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.schema.Product;
import com.rentit.schema.RentalRequest;
import com.rentit.schema.RentalRequest.RequestStatus;

import org.json.JSONException;
import org.json.JSONObject;
import android.support.design.widget.FloatingActionButton;
import java.util.HashMap;

/**
 * Created by akhare on 31-Jul-15.
 */
public class ProductDetailViewFragment extends BaseFragment {
    private String mProductId;
    private Product mProduct;
    private JsonParser mParser;
    private final String TAG = ProductDetailViewFragment.class.getSimpleName();

    private TextView mTvProductName;
    private NetworkImageView mNivProductImage;
    private TextView mTvRent;
    private TextView mTvOwnerId;
    private TextView mTvAvailability;
    private TextView mTvDepositAmount;
    private TextView mTvPurchaseDate;
    private FloatingActionButton mBtnFab;
    private IonFragmentWorkDone mParentActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (IonFragmentWorkDone)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_prdocut_detail_view,container,false);
        Bundle arguments = getArguments();
        mProductId = arguments.getString("productId");
        Log.d(TAG, "pid:" + mProductId);
        mParser = new JsonParser();

        mTvProductName = (TextView) v.findViewById(R.id.tv_row_product_name);
        mNivProductImage = (NetworkImageView) v.findViewById(R.id.iv_prouduct_image);
        mTvRent = (TextView) v.findViewById(R.id.tv_per_month_rent);
        mTvAvailability = (TextView) v.findViewById(R.id.tv_available);
        mTvDepositAmount = (TextView) v.findViewById(R.id.tv_deposit);
        mTvOwnerId = (TextView) v.findViewById(R.id.tv_owner);
        mTvPurchaseDate = (TextView) v.findViewById(R.id.tv_age);
        mBtnFab = (FloatingActionButton)v.findViewById(R.id.fab);
        getProductDetail();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBtnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG,"owner:"+mProduct.getmProductOwner());
                RentalRequest req = new RentalRequest(mProduct.getmProductOwner(), mProduct,
                        PreferenceManager.getmInstance(getActivity()).getUserName(), RequestStatus.REQUEST_SENT.name(),
                        RentalRequest.ItemType.REQUEST_SENT_BY_ME,"1 month");
                RentalRequest.sendRentalRequest(getActivity(), req,mParentActivity);
            }
        });
    }

    private void getProductDetail(){
        JSONObject proructId= new JSONObject();
        try {
            proructId.put("productId", mProductId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();

        builder.setJsonRequestParams(proructId)
                .setMethodType(HttpRequest.HttpMethod.POST)
                .setUrl(URLConstants.getmInstance().GET_PRODUCT_BY_ID())
                .setCallback(new HttpRequest.IResponseCallBack() {
                    @Override
                    public void onFailure(String msg) {
                        Log.d(TAG, "some error occured in fetching the product:" + mProductId);
                    }

                    @Override
                    public void onSuccess(HashMap responseObjects) {
                        Log.d(TAG,"product object received successfully:"+responseObjects.get("result").toString());
                        Product product = mParser.getProductFromJson((JSONObject)responseObjects.get("result"));
                        //Log.d(TAG,"product clicked = "+product.getmUnit().getmUnitName());
                        mProduct = product;
                        updateViews(product);
                    }
                });
        NetworkConnection.getInstance(getActivity()).addHttpJsonRequest(builder.getNewHttpRequest());
    }


    private void updateViews(Product product){
        if(product != null){
            mTvProductName.setText(product.getmUnit().getmUnitName());
            mNivProductImage.setImageUrl(URLConstants.getmInstance().IMAGES()+product.getmUnit().getmImageLink(), NetworkConnection.getInstance(getActivity()).getImageLoader());
            mTvRent.setText(product.getmPerDayRent());
            mTvAvailability.setText(product.getmIsAvailable());
            mTvDepositAmount.setText(product.getmSecurityDeposit());
            mTvPurchaseDate.setText(product.getmPurchaseDate());
            mTvOwnerId.setText(product.getmProductOwner());
        }

    }
}
