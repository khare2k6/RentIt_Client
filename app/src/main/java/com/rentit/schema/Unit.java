package com.rentit.schema;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akhare on 03-Jul-15.
 */
public class Unit {
    @SerializedName("name")private String mUnitName;
    @SerializedName("unitId")private String mMongoId;
    @SerializedName("modelNumber")private String mModelNumber;
    @SerializedName("productLink")private String mUnitWebLink;
    @SerializedName("image")private String mImageLink;

    public String getmUnitName() {
        return mUnitName;
    }

    public String getmMongoId() {
        return mMongoId;
    }

    public String getmModelNumber() {
        return mModelNumber;
    }

    public String getmUnitWebLink() {
        return mUnitWebLink;
    }

    public String getmImageLink() {
        return mImageLink;
    }



    private Unit(UnitBuilder unitBuilder){
        mUnitName = unitBuilder.UnitName;
        mMongoId = unitBuilder.ModelNumber;
        mModelNumber = unitBuilder.ModelNumber;
        mUnitWebLink = unitBuilder.ProductWebLink;
        mImageLink = unitBuilder.ImageLink;
    }

    /**
     * Builder class for Unit
     */
    public static class UnitBuilder{
        private String
                UnitName,
                MongoId,
                ModelNumber,
                ProductWebLink,
                ImageLink;

        public UnitBuilder setName(String name){
            UnitName = name;
            return this;
        }

        public UnitBuilder setMongoId(String mid){
            MongoId = mid;
            return this;
        }
        public UnitBuilder setModelNumber(String modelNumber){
            ModelNumber = modelNumber;
            return this;
        }
        public UnitBuilder setUnitWeblink(String productWebLink){
            ProductWebLink = productWebLink;
            return this;
        }
        public UnitBuilder setUnitImageLink(String imageLink){
            ImageLink = imageLink;
            return this;
        }

        public Unit getNewUnit(){
            return new Unit(this);
        }
    }
}
