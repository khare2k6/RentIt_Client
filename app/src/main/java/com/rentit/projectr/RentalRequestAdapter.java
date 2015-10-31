package com.rentit.projectr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.android.volley.toolbox.NetworkImageView;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.schema.RentalRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akhare on 03-Aug-15.
 */
public class RentalRequestAdapter extends ArrayAdapter<RentalRequest> {
    private ArrayList<RentalRequest> mRentalRequestList;
    private RentalRequestsFor mInitiater;
    public enum RentalRequestsFor{
        OWNER,
        TENANT;
    }


    public RentalRequestAdapter(Context context, int resource,  List<RentalRequest> objects) {
        super(context, resource, objects);
        mRentalRequestList = new ArrayList<RentalRequest>(objects);
    }

    public RentalRequestAdapter(Context context, int resource,  List<RentalRequest> objects,RentalRequestsFor initiator) {
        super(context, resource, objects);
        mInitiater = initiator;
        mRentalRequestList = new ArrayList<RentalRequest>(objects);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row_rental_request,parent,false);
        NetworkImageView niv = (NetworkImageView) view.findViewById(R.id.niv_request_status_product_image);
        NameValueView userType = (NameValueView) view.findViewById(R.id.nv_user_name);
        NameValueView productName = (NameValueView) view.findViewById(R.id.nv_product_name);
        NameValueView status = (NameValueView) view.findViewById(R.id.nv_status);


        //TextView userName = (TextView) view.findViewById(R.id.tv_row_user_name);
        switch (mInitiater){
            case TENANT:
                //userName.setText(mRentalRequestList.get(position).getmTenantId());
                userType.setValueOfLabel(mRentalRequestList.get(position).getmOwnerId());
                break;
            case OWNER:
                userType.setLabel("Tenant");
                userType.setValueOfLabel(mRentalRequestList.get(position).getmTenantId());
                break;
        }

      //  TextView productName = (TextView) productName.findViewById(R.id.tv_row_product_name);

        productName.setValueOfLabel(mRentalRequestList.get(position).getmProduct().getmUnit().getmUnitName());
        niv.setImageUrl(URLConstants.getmInstance().IMAGES()+mRentalRequestList.get(position).getmProduct().getmUnit().getmImageLink(),
                NetworkConnection.getInstance(context).getImageLoader());
        //TextView status = (TextView) view.findViewById(R.id.tv_row_status);
        String state = mRentalRequestList.get(position).getmState();
        status.setValueOfLabel(state);
//        status.setTextColor(getContext().getResources().getColor(android.R.color.holo_red_dark));
//        if(state.equals("RENTED")){
//            status.setTextColor(getContext().getResources().getColor(android.R.color.holo_green_dark));
//        }
        return view;
    }
}
