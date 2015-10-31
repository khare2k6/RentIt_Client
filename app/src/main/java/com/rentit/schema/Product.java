package com.rentit.schema;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akhare on 03-Jul-15.
 */
public class Product implements Parcelable {
    @SerializedName("unit")private Unit mUnit;
    @SerializedName("productId")private String mProductId;
    @SerializedName("purchaseDate")private String mPurchaseDate;
    @SerializedName("isAvailable")private String mIsAvailable;
    @SerializedName("perDayRent")private String mPerDayRent;
    @SerializedName("securityDeposit")private String mSecurityDeposit;
    @SerializedName("minimumRentalPeriod")private String mMinimumRentalPeriod;
    @SerializedName("tenant")private String mTenant;
    @SerializedName("availableState")private String mAvailableState;
    @SerializedName("productOwner")private String mProductOwner;

    protected Product(Parcel in) {
        mProductId = in.readString();
        mPurchaseDate = in.readString();
        mIsAvailable = in.readString();
        mPerDayRent = in.readString();
        mSecurityDeposit = in.readString();
        mMinimumRentalPeriod = in.readString();
        mTenant = in.readString();
        mAvailableState = in.readString();
        mProductOwner = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public Unit getmUnit() {
        return mUnit;
    }

    public String getmProductId() {
        return mProductId;
    }

    public String getmPurchaseDate() {
        return mPurchaseDate;
    }

    public String getmIsAvailable() {
        return mIsAvailable;
    }

    public String getmPerDayRent() {
        return mPerDayRent;
    }

    public String getmSecurityDeposit() {
        return mSecurityDeposit;
    }

    public String getmMinimumRentalPeriod() {
        return mMinimumRentalPeriod;
    }

    public String getmTenant() {
        return mTenant;
    }

    public String getmAvailableState() {
        return mAvailableState;
    }

    public String getmProductOwner() {
        return mProductOwner;
    }

    private Product(ProductBuilder productBuilder) {
                mUnit =  productBuilder.unit;
                mProductId=productBuilder.ProductId;
                mPurchaseDate=productBuilder.PurchaseDate;
                mIsAvailable=productBuilder.IsAvailable;
                mPerDayRent=productBuilder.PerDayRent;
                mSecurityDeposit=productBuilder.SecurityDeposit;
                mMinimumRentalPeriod=productBuilder.MinimumRentalPeriod;
                mTenant=productBuilder.Tenant;
                mAvailableState=productBuilder.AvailableState;
                mProductOwner=productBuilder.ProductOwner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mProductId);
        dest.writeString(mPurchaseDate);
        dest.writeString(mIsAvailable);
        dest.writeString(mPerDayRent);
        dest.writeString(mSecurityDeposit);
        dest.writeString(mMinimumRentalPeriod);
        dest.writeString(mTenant);
        dest.writeString(mAvailableState);
        dest.writeString(mProductOwner);
    }

    /**
     * Builder class for product
     */
    public static class ProductBuilder{
        Unit unit;
        private String
                Unit,
                ProductId,
                PurchaseDate,
                IsAvailable,
                PerDayRent,
                SecurityDeposit,
                MinimumRentalPeriod,
                Tenant,
                AvailableState,
                ProductOwner;

        public ProductBuilder setUnit(Unit unit) {
            this.unit = unit;
            return this;
        }

        public ProductBuilder setProductId(String productId) {
            ProductId = productId;
            return this;
        }

        public ProductBuilder setPurchaseDate(String purchaseDate) {
            PurchaseDate = purchaseDate;
            return this;
        }

        public ProductBuilder setIsAvailable(String isAvailable) {
            IsAvailable = isAvailable;
            return this;
        }

        public ProductBuilder setPerDayRent(String perDayRent) {
            PerDayRent = perDayRent;
            return this;
        }

        public ProductBuilder setSecurityDeposit(String securityDeposit) {
            SecurityDeposit = securityDeposit;
            return this;
        }

        public ProductBuilder setMinimumRentalPeriod(String minimumRentalPeriod) {
            MinimumRentalPeriod = minimumRentalPeriod;
            return this;
        }

        public ProductBuilder setTenant(String tenant) {
            Tenant = tenant;
            return this;
        }

        public ProductBuilder setAvailableState(String availableState) {
            AvailableState = availableState;
            return this;
        }

        public ProductBuilder setProductOwner(String productOwner) {
            ProductOwner = productOwner;
            return this;
        }

        public Product getNewProduct(){
            return new Product(this);
        }
    }
}
