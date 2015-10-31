package com.rentit.parser;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.rentit.schema.Product;
import com.rentit.schema.RentalRequest;
import com.rentit.schema.RentalRequest.ItemType;
import com.rentit.schema.Unit;
import com.rentit.schema.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AKhare on 08-Jul-15.
 */
public class JsonParser {
    private Gson mGson;

    public JsonParser() {
        mGson = new Gson();
    }

    public User getUserFromJson(String jsonString) {
        return mGson.fromJson(jsonString, User.class);
    }

    public Unit getUnitrFromJson(String jsonString) {
        return mGson.fromJson(jsonString, Unit.class);
    }
    public Unit getUnitrFromJson(JSONObject jObj) {
       // return mGson.fromJson(jsonString, Unit.class);
        try {
            String name = jObj.getString("name");
            String unitId = jObj.getString("unitId");
            String modelNumber = jObj.getString("modelNumber");
            String productLink = jObj.getString("productLink");
            String image = jObj.getString("image");

            Unit.UnitBuilder unitBuilder = new Unit.UnitBuilder();
            unitBuilder
                    .setName(name)
                    .setModelNumber(modelNumber)
                    .setUnitImageLink(image)
                    .setUnitWeblink(productLink);
            return unitBuilder.getNewUnit();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Product getProductFromJson(String jsonString) {
        return mGson.fromJson(jsonString, Product.class);
    }

    public RentalRequest getRentalRequestFromJson(JSONObject jsonObj,ItemType requestType){

        try {
            String owner = jsonObj.getString("ownerId");
            String tenant = jsonObj.getString("tenantId");
            String state = jsonObj.getString("state");
            String rentalPeriod = jsonObj.getString("rentalPeriod");
            JSONObject productJson = new JSONObject(jsonObj.getString("productId"));
            Product prd = getProductFromJson(productJson);
            return (new RentalRequest(owner,prd,tenant,state,requestType,rentalPeriod));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
    public Product getProductFromJson(JSONObject jObj) {
        //return mGson.fromJson(jsonString, Product.class);
        try {
            String unit = (String)jObj.getString("unit");
            String productId = jObj.getString("productId");
            String purchaseDate = jObj.getString("purchaseDate");
            String isAvail = jObj.getString("isAvailable");
            String perdayrent = jObj.getString("perDayRent");
            String securityDeposit = jObj.getString("securityDeposit");
            String minimumRentalperiod = jObj.getString("minimumRentalPeriod");
            //String tenant = jObj.getString("tenant");
            //String availableState = jObj.getString("availableState");
            String productOwner = jObj.getString("productOwner");
            Product.ProductBuilder builder = new Product.ProductBuilder();
            Log.d("JsonParser", "untiName:"+getUnitrFromJson(unit).getmUnitName());
                    builder.setUnit(getUnitrFromJson(unit))
                            .setProductId(productId)
                            .setPurchaseDate(purchaseDate)
                            .setIsAvailable(isAvail)
                            .setPerDayRent(perdayrent)
                            .setSecurityDeposit(securityDeposit)
                            .setMinimumRentalPeriod(minimumRentalperiod)
                                    //.setTenant(tenant)
                                    // .setAvailableState(availableState)
                            .setProductOwner(productOwner);
            return builder.getNewProduct();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Product[] getProductArrayFromJson(String productsArray) {
        return mGson.fromJson(productsArray,Product[].class);
    }

    public RentalRequest[] getRentalRequestsFromJson(String rentalRequests) {
        return mGson.fromJson(rentalRequests,RentalRequest[].class);
    }
}
