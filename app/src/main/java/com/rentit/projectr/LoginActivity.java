package com.rentit.projectr;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rentit.com.rentit.network.HttpRequest;

import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.schema.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.HashMap;

public class LoginActivity extends Activity {

    private String TAG = LoginActivity.class.getSimpleName();
    private NetworkConnection mNetwork;
    private Button mLoginButton;
    private TextView mUserId,mPassword;
    private EditText mEtServerIp;
    private URLConstants mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mNetwork = NetworkConnection.getInstance(this);
        mUserId = (TextView) findViewById(R.id.email);
        mPassword = (TextView) findViewById(R.id.password);
        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mEtServerIp = (EditText) findViewById(R.id.et_server_ip);
        mUrl = URLConstants.getmInstance();
    }


    public void doLogin(final String username,String password){
        JSONObject json = new JSONObject();
        URLConstants.getmInstance().getServerIpFromPreferences(this);
        Log.d(TAG, "onResume");


        try {
            json.put("username",username);
            json.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        builder.setMethodType(HttpRequest.HttpMethod.POST)
                .setUrl(mUrl.LOGIN_URL(LoginActivity.this))
                .setJsonRequestParams(json)
                .setCallback(new HttpRequest.IResponseCallBack() {
                    @Override
                    public void onFailure(String msg) {
                        Log.d(TAG, " onFailure received..");
                        Toast.makeText(LoginActivity.this,"Login Failed, try again!",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onSuccess(HashMap responseObjects) {
                    Log.d(TAG, " response received..");
                        JSONObject resultJson = (JSONObject)responseObjects.get("result");
                        Log.d(TAG, " response received..json obj ="+resultJson);
                        PreferenceManager.getmInstance(LoginActivity.this).setLoggedInUser(username);
                        Intent intent = new Intent(LoginActivity.this,MainActivity2.class);
                        Toast.makeText(LoginActivity.this,"Login Success!",Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                });
        Log.d(TAG,"adding request");
        mNetwork.addHttpJsonRequest(builder.getNewHttpRequest());

    }

    public void tryUserInfo(){
        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
        Log.d(TAG, mUrl.USER_INFO_URL());
        builder.setUrl(mUrl.USER_INFO_URL());
        builder.setMethodType(HttpRequest.HttpMethod.GET);
        builder.setCallback(new HttpRequest.IResponseCallBack() {
            @Override
            public void onFailure(String msg) {
                Log.d(TAG, " onFailure received..");
            }

            @Override
            public void onSuccess(HashMap responseObjects) {
                JSONObject resultJson = (JSONObject) responseObjects.get("result");
                Log.d(TAG, " response received..json obj =" + resultJson.toString());
                JsonParser parser = new JsonParser();
                User user = (User) parser.getUserFromJson(resultJson.toString());
                Log.d(TAG, "user name:" + user.getPassword());
            }
        });
        Log.d(TAG,"adding request user info");
        mNetwork.addHttpJsonRequest(builder.getNewHttpRequest());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_l, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //doLogin();
        //tryUserInfo();
        tryImageUpload();
        mEtServerIp.setText(PreferenceManager.getmInstance(this).getServerIp());
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserId.getText().toString();
                String password = mPassword.getText().toString();
                if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                    if(!TextUtils.isEmpty(mEtServerIp.getText())){
                        PreferenceManager.getmInstance(LoginActivity.this).setIp(mEtServerIp.getText().toString());
                    }
                    doLogin(userName, password);
                }

            }
        });
//        Intent intent = new Intent(LoginActivity.this,MainActivity2.class);
//        startActivity(intent);
//        Intent intent = new Intent(LoginActivity.this,testLayout.class);
//        startActivity(intent);

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

    private void tryImageUpload(){
        String imagePath = "/sdcard/Download/no_image.png";
        File file = new File(imagePath);

        NetworkConnection.getInstance(this).addMultiPartRequest(URLConstants.getmInstance().UPLOAD_PHOTO(),new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error received fr image:"+error);
            }
        }, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse received fr image");
            }
        },file,null);
    }
}
