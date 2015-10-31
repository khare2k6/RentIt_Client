package com.rentit.projectr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.schema.Product;

import java.util.ArrayList;

/**
 * Created by AKhare on 26-Jul-15.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ProductViewHolder> {
    private ArrayList<Product> mProductList;
    private ImageLoader mImageLoader;
    private final String TAG = RecyclerViewAdapter.class.getSimpleName();
    private int RED_TEXT_COLOR;
    private int GREEN_TEXT_COLOR;

    public RecyclerViewAdapter(ArrayList<Product> products, Context context){
        mProductList = products;
        mImageLoader = NetworkConnection.getInstance(context).getImageLoader();
        RED_TEXT_COLOR = context.getResources().getColor(R.color.ColorRed);
        GREEN_TEXT_COLOR = context.getResources().getColor(R.color.accent_material_light);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_product_list,viewGroup,false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder productViewHolder, int i) {
        Log.d(TAG, "image link for " + URLConstants.getmInstance().IMAGES() + "/" + mProductList.get(i).getmUnit().getmImageLink());
        productViewHolder.productImage.setImageUrl(URLConstants.getmInstance().IMAGES() + "/" + mProductList.get(i).getmUnit().getmImageLink(), mImageLoader);
        productViewHolder.productName.setText(mProductList.get(i).getmUnit().getmUnitName());
        productViewHolder.rent.setText(mProductList.get(i).getmPerDayRent());
        productViewHolder.isavaialble.setText(mProductList.get(i).getmIsAvailable());
        productViewHolder.isavaialble.setTextColor(GREEN_TEXT_COLOR);
        if(mProductList.get(i).getmIsAvailable().equalsIgnoreCase("false"))
            productViewHolder.isavaialble.setTextColor(RED_TEXT_COLOR);

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        NetworkImageView productImage;
        TextView productName,
                 rent,
                isavaialble;
         ProductViewHolder(View itemView){
             super(itemView);
             productImage = (NetworkImageView)itemView.findViewById(R.id.iv_row_product_image);
             productName = (TextView) itemView.findViewById(R.id.tv_row_product_name);
             rent = (TextView) itemView.findViewById(R.id.tv_row_product_rent);
             isavaialble = (TextView) itemView.findViewById(R.id.tv_row_product_avaialbility);
         }
    }
}
