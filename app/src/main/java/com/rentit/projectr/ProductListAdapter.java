package com.rentit.projectr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.schema.Product;
import com.rentit.schema.Unit;

/**
 * Created by akhare on 24-Jul-15.
 */
public class ProductListAdapter extends ArrayAdapter<Product>{
    private Context mContext;
    private Product[] mProducts;

    private TextView mTvProductName,mTvRent,mIsAvail;
    private NetworkImageView mProductImage;
    private ImageLoader mImageLoader;

    public ProductListAdapter(Context context,Product[] products){
        super(context,-1,products);
        mContext = context;
        mProducts = products;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_product_list,parent,false);

        mTvProductName = (TextView) view.findViewById(R.id.tv_row_product_name);
        mTvRent = (TextView) view.findViewById(R.id.tv_row_product_rent);
        mIsAvail = (TextView) view.findViewById(R.id.tv_row_product_avaialbility);
        mProductImage = (NetworkImageView) view.findViewById(R.id.iv_row_product_image);
        mImageLoader = NetworkConnection.getInstance(mContext).getImageLoader();

        Unit unit = mProducts[position].getmUnit();
        mTvProductName.setText(unit.getmUnitName());
        mTvRent.setText(mProducts[position].getmPerDayRent());
        mIsAvail.setText(mProducts[position].getmIsAvailable());
        mProductImage.setImageUrl(unit.getmImageLink(),mImageLoader);
        return super.getView(position, convertView, parent);
    }
}
