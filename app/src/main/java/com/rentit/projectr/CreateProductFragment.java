package com.rentit.projectr;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by AKhare on 20-Aug-15.
 */
public class CreateProductFragment extends BaseFragment {
    private EditText mEtUnitName;
    //private EditText mEtUnitId;
    private ImageButton mProductImageButton;
    private File mProductImg;
    private EditText mEtModelNumber;
    private EditText mEtProductLink;
    private EditText mEtPurchaseDate;
    private EditText mEtRent;
    private EditText mEtSecurityDeposit;
    private EditText mEtMinimumRentalPeriod;
    private Button mBtnSubmit;
    private IonFragmentWorkDone mParentActivity;

    private final int CAMERA_PICTURE= 12;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private final String TAG = CreateProductFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_product_create,container,false);
        mEtUnitName = (EditText) view.findViewById(R.id.et_unit_name);
        //mEtUnitId = (EditText) view.findViewById(R.id.et_unit_id);
        mEtModelNumber = (EditText) view.findViewById(R.id.et_model_number);
        mEtProductLink = (EditText) view.findViewById(R.id.et_product_link);
        mEtPurchaseDate = (EditText) view.findViewById(R.id.et_product_date);
        mEtRent = (EditText) view.findViewById(R.id.et_product_rent);
        mEtSecurityDeposit = (EditText) view.findViewById(R.id.et_security_deposit);
        mEtMinimumRentalPeriod = (EditText) view.findViewById(R.id.et_min_rental_period);
        mProductImageButton = (ImageButton)view.findViewById(R.id.img_button);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_create_product);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (IonFragmentWorkDone) activity;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    createNewProduct();
                }
            }
        });
        mProductImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mProductImg = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                Log.d(TAG,"mProductImg = "+mProductImg);
                if (mProductImg != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mProductImg)); // set the image file name
                    startActivityForResult(intent, CAMERA_PICTURE);
                }
            }
        });
    }

    private String getImageName(){
        if(mEtUnitName !=null && !TextUtils.isEmpty(mEtUnitName.getText().toString().trim())){
            return mEtUnitName.getText().toString().trim()+"_"+PreferenceManager.getmInstance(getActivity()).getUserName();
        }
            return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    private void resetImageName(){
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorageDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ getImageName()+ ".jpg");

        Log.d(TAG,"file name changed :"+mProductImg.renameTo(mediaFile));
        Log.d(TAG, "New file getAbsolutePath :" + mProductImg.getAbsolutePath()+" file name:"+mProductImg.getName());

    }

    private void uploadProcutImage(){
        if(mProductImg !=null){
            resetImageName();
            HashMap<String,String>map = new HashMap<String,String>();
            map.put("imageName","IMG_"+getImageName()+".jpg");
            NetworkConnection.getInstance(getActivity()).addMultiPartRequest(
                    URLConstants.getmInstance().UPLOAD_PHOTO(),
                    /*Error response of uploading photo response*/
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, "Error in uploading the photo:"+error);
                        }
                    },
                    /*Success response of uploading photo response*/
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "Photo uploaded successfully:"+response);
                    }
                    },
                    mProductImg,//File
                    map);
        }
    }

    private void createNewProduct(){
        JSONObject jObj = new JSONObject();
        JSONObject unitJsonObj = new JSONObject();

        try {

            unitJsonObj.put("name", mEtUnitName.getText().toString());
            unitJsonObj.put("unitId", "new");
            unitJsonObj.put("modelNumber", mEtModelNumber.getText().toString());
            unitJsonObj.put("productLink", mEtProductLink.getText().toString());
            unitJsonObj.put("image", "IMG_"+getImageName()+".jpg");
            jObj.put("unit",unitJsonObj.toString());
            jObj.put("purchaseDate", mEtPurchaseDate.getText().toString());
            jObj.put("isAvailable", "true");
            jObj.put("perDayRent", mEtRent.getText().toString());
            jObj.put("securityDeposit", mEtSecurityDeposit.getText().toString());
            jObj.put("minimumRentalPeriod", mEtMinimumRentalPeriod.getText().toString());
            jObj.put("ownerId", PreferenceManager.getmInstance(getActivity()).getUserName());
            jObj.put("perDayRent", mEtRent.getText().toString());
            jObj.put("perDayRent", mEtRent.getText().toString());
            Log.d(TAG, "object to be sent:"+jObj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpRequest.HttpRequestBuilder builder = new HttpRequest.HttpRequestBuilder();
            builder.setUrl(URLConstants.getmInstance().CREATE_PRODUCT())
                    .setMethodType(HttpRequest.HttpMethod.POST)
                    .setJsonRequestParams(jObj)
                    .setCallback(new HttpRequest.IResponseCallBack() {
                        @Override
                        public void onFailure(String msg) {
                            mParentActivity.onFragmentWorkDone(ShowDialog.ERROR_OCCURED);
                        }

                        @Override
                        public void onSuccess(HashMap responseObjects) {
                            uploadProcutImage();
                            mParentActivity.onFragmentWorkDone(ShowDialog.PRODUCT_CREATED_SUCCESSFULLY);
                        }
                    });
        NetworkConnection.getInstance(getActivity()).addHttpJsonRequest(builder.getNewHttpRequest());
    }

    /** Create a File for saving an image or video */
    private  File getOutputMediaFileUri(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        File mediaStorageDir = new File(String.valueOf(Environment.getExternalStorageDirectory()));
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());


        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ getImageName() + ".jpg");
            Log.d(TAG,"file name:"+mediaFile.getName());
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,"onActivityResult");
        if(requestCode == CAMERA_PICTURE){
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mProductImageButton.getWidth();
        int targetH = mProductImageButton.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mProductImg.getAbsolutePath(), bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mProductImg.getAbsolutePath(), bmOptions);
        mProductImageButton.setImageBitmap(bitmap);

    }
    private boolean validateInputs(){
        EditText[] editTexts = {mEtUnitName/*,mEtUnitId*/,mEtModelNumber,mEtProductLink,mEtPurchaseDate,mEtRent,mEtSecurityDeposit,mEtMinimumRentalPeriod};
        for(EditText et: editTexts){
            if(et.getText().toString().length() <=0){
                Toast.makeText(getActivity(),et.getHint().toString()+" cannot be empty",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }
}
