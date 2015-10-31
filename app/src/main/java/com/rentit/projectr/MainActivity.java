package com.rentit.projectr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends Activity {
    private ArrayList<Product> mProductList;
    private JsonParser mJsonParser;
    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRv = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        mRv.setLayoutManager(llm);
        mRv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mJsonParser = new JsonParser();
        getProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRv.addOnItemTouchListener(new RecylerViewTouchListener(this, new RecylerViewTouchListener.onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this,"clicked:"+position,Toast.LENGTH_SHORT).show();
                String pidOfProduct=mProductList.get(position).getmProductId();
                Intent intent = new Intent(MainActivity.this,ProdcutDetailView.class);
                intent.putExtra("productId",pidOfProduct);
                startActivity(intent);
            }
        }));
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

    private void getProducts(){
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        if(mProductList != null) return;// DO THIS PROPERLY LATER
        builder.setUrl(URLConstants.getmInstance().GET_ALL_PRODUCTS())
                .setMethodType(HttpRequest.HttpMethod.GET)
                .setCallback(new HttpRequest.IResponseCallBack() {
                    @Override
                    public void onFailure(String msg) {
                        Log.d(TAG,"Some error occured in fetching all prodxuts list");
                    }

                    @Override
                    public void onSuccess(HashMap responseObjects) {
                        JSONArray jsonObj = (JSONArray)responseObjects.get("result");
                        if(jsonObj != null){
                            Log.d(TAG,"jsonAraayToString:"+jsonObj.toString());
                            //String products = "products:"+jsonObj.toString();
                           // mProductList = mJsonParser.getProductArrayFromJson(jsonObj);
                            if(mProductList == null) mProductList = new ArrayList<Product>();
                            for(int i =0;i<jsonObj.length();i++){
                                try {
                                    JSONObject obj = jsonObj.getJSONObject(i);
                                    Log.d(TAG,"product before conversions:"+obj.toString());
                                   // String products = "products:"+obj.toString();
                                    Product pr = mJsonParser.getProductFromJson(obj);
                                   // Log.d(TAG,"product after conversions:"+pr.toString()+" "+pr.getmPerDayRent());
                                    mProductList.add(pr);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            Log.d(TAG,"Products fetched:"+mProductList.size());
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(mProductList,MainActivity.this);
                            mRv.setAdapter(adapter);
                        }
                    }
                });
        NetworkConnection.getInstance(this).addHttpJsonArrayRequest(builder.getNewHttpRequest());
    }
}
