package com.rentit.projectr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.schema.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ProdcutDetailView extends AppCompatActivity {
    private String mProductId;
   // private Product mProduct;
    private JsonParser mParser;
    private final String TAG = ProdcutDetailView.class.getSimpleName();

    private TextView mTvProductName;
    private NetworkImageView mNivProductImage;
    private TextView mTvRent;
    private TextView mTvAvailability;
    private TextView mTvDepositAmount;
    private TextView mTvPurchaseDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prdocut_detail_view);
        Intent intent = getIntent();
        mProductId = intent.getStringExtra("productId");
        Log.d(TAG, "pid:" + mProductId);
        mParser = new JsonParser();

        mTvProductName = (TextView) findViewById(R.id.tv_row_product_name);
        mNivProductImage = (NetworkImageView) findViewById(R.id.iv_prouduct_image);
        mTvRent = (TextView) findViewById(R.id.tv_per_month_rent);
        mTvAvailability = (TextView) findViewById(R.id.tv_available);
        mTvDepositAmount = (TextView) findViewById(R.id.tv_deposit);
        mTvPurchaseDate = (TextView) findViewById(R.id.tv_age);
        getProductDetail();
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
                        Log.d(TAG,"product clicked = "+product.getmUnit().getmUnitName());
                        updateViews(product);
                    }
                });
        NetworkConnection.getInstance(this).addHttpJsonRequest(builder.getNewHttpRequest());
    }
    @Override
    protected void onResume() {
        super.onResume();


    }

    private void updateViews(Product product){
        if(product != null){
            mTvProductName.setText(product.getmUnit().getmUnitName());
            mNivProductImage.setImageUrl(URLConstants.getmInstance().IMAGES()+product.getmUnit().getmImageLink(), NetworkConnection.getInstance(this).getImageLoader());
            mTvRent.setText(product.getmPerDayRent());
            mTvAvailability.setText(product.getmIsAvailable());
            mTvDepositAmount.setText(product.getmSecurityDeposit());
            mTvPurchaseDate.setText(product.getmPurchaseDate());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prdocut_detail_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
