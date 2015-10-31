package com.rentit.projectr;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.schema.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akhare on 31-Jul-15.
 */
public class ProductsListFragment extends BaseFragment {
    private ArrayList<Product> mProductList;
    private JsonParser mJsonParser;
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRv;
    private Context mContext;
    private IonProductListItemClicked mItemClickListener;

    public interface IonProductListItemClicked {
       public void onProductClicked(String productId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_main,container,false);
        mRv = (RecyclerView) v.findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        mRv.setLayoutManager(llm);
        mRv.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mJsonParser = new JsonParser();

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mItemClickListener = (IonProductListItemClicked)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mItemClickListener = null;
    }

    public ArrayList<Product> getProductList(){
        return mProductList;
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
        mRv.addOnItemTouchListener(new RecylerViewTouchListener(getActivity(), new RecylerViewTouchListener.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(getActivity(), "clicked:" + position, Toast.LENGTH_SHORT).show();
                String pidOfProduct = mProductList.get(position).getmProductId();
                mItemClickListener.onProductClicked(pidOfProduct);
//                Intent intent = new Intent(getActivity(), ProdcutDetailView.class);
//                intent.putExtra("productId", pidOfProduct);
//                startActivity(intent);
            }
        }));
    }

    private void getProducts(){
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        if(mProductList != null) { // DO PROPERLY LATER
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(mProductList,getActivity());
            mRv.setAdapter(adapter);
            return;
        }
        builder.setUrl(URLConstants.getmInstance().GET_AVAIALBLE_PRODUCTS())
                .setMethodType(HttpRequest.HttpMethod.GET)
                .setCallback(new HttpRequest.IResponseCallBack() {
                    @Override
                    public void onFailure(String msg) {
                        Log.d(TAG, "Some error occured in fetching all prodxuts list");
                    }

                    @Override
                    public void onSuccess(HashMap responseObjects) {
                        JSONArray jsonObj = (JSONArray)responseObjects.get("result");
                        if(jsonObj != null){
                            Log.d(TAG,"jsonAraayToString:"+jsonObj.toString());
                            if(mProductList == null) mProductList = new ArrayList<Product>();
                            for(int i =0;i<jsonObj.length();i++){
                                try {
                                    JSONObject obj = jsonObj.getJSONObject(i);
                                    Log.d(TAG,"product before conversions:"+obj.toString());
                                    Product pr = mJsonParser.getProductFromJson(obj);
                                    mProductList.add(pr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.d(TAG,"Products fetched:"+mProductList.size());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(mProductList,getActivity());
                            mRv.setAdapter(adapter);
                        }
                    }
                });
        NetworkConnection.getInstance(getActivity()).addHttpJsonArrayRequest(builder.getNewHttpRequest());
    }
}
