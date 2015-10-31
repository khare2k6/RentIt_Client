package com.rentit.projectr;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rentit.schema.RentalRequest;

import java.util.ArrayList;

/**
 * Created by akhare on 03-Aug-15.
 */
public class ListRentalRequestsFragment extends BaseFragment {
    private RentalRequestAdapter mAdapter;
    private IonRentalRequestClicked mActivity;

    public interface IonRentalRequestClicked{
        public void onRequestClicked(RentalRequest request);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (IonRentalRequestClicked)activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_rental_request_list, container, false);
        Bundle args = getArguments();
       final ArrayList<RentalRequest> list = args.getParcelableArrayList("rentalRequests");
        RentalRequestAdapter.RentalRequestsFor initiaterIndex = (RentalRequestAdapter.RentalRequestsFor)args.get("initiater");

        mAdapter = new RentalRequestAdapter(getActivity(),R.layout.layout_rental_request_list,list,initiaterIndex);
        ListView lvRentalRequests = (ListView) v.findViewById(R.id.lv_rental_requests);
        lvRentalRequests.setAdapter(mAdapter);
        lvRentalRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActivity != null) {
                    Log.d("ListRetnalRequestFrag",list.get(position).toString());
                    mActivity.onRequestClicked(list.get(position));
                }
            }
        });
        return v;
    }
}
