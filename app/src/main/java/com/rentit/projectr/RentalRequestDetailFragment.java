package com.rentit.projectr;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.schema.Product;
import com.rentit.schema.RentalRequest;

/**
 * Created by AKhare on 06-Aug-15.
 */
public class RentalRequestDetailFragment extends BaseFragment {
    private RentalRequest mRequest;
    private Button mBtnCancel,
                   mBtnApprove,
                   mBtnReject;
    private OnClickListerner mClickListener;
    private  IonFragmentWorkDone mParentActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mParentActivity = (IonFragmentWorkDone) activity;
    }

    public RentalRequestDetailFragment(){
        mClickListener = new OnClickListerner();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_rental_request_detail, container, false);
        Bundle args = getArguments();
        mRequest = args.getParcelable("request");
        NetworkImageView iv = (NetworkImageView) view.findViewById(R.id.iv_prouduct_image_rental_request);
        iv.setImageUrl(URLConstants.getmInstance().IMAGES()+mRequest.getmProduct().getmUnit().getmImageLink(), NetworkConnection.getInstance(getActivity()).getImageLoader());
         mBtnCancel = (Button) view.findViewById(R.id.btn_cancel);
         mBtnReject = (Button) view.findViewById(R.id.btn_reject);
         mBtnApprove = (Button) view.findViewById(R.id.btn_approve);

        mBtnCancel.setOnClickListener(mClickListener);
        mBtnReject.setOnClickListener(mClickListener);
        mBtnApprove.setOnClickListener(mClickListener);

        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        RentalRequest.RequestStatus reqStatus =null;
        for(RentalRequest.RequestStatus status :RentalRequest.RequestStatus.values()){
            if(status.toString().equals(mRequest.getmState())) {
                reqStatus = status;
                break;
            }
        }
        switch(mRequest.getmRequestType()){
            case REQUEST_PENDING_ON_ME:
                String text = "<font color='red'>%s</font> would like to rent your product <font color='red'>%s</font>";
                String text2= String.format(text, mRequest.getmTenantId(), mRequest.getmProduct().getmUnit().getmUnitName());
                tvInfo.setText(Html.fromHtml(text2), TextView.BufferType.SPANNABLE);
                mBtnCancel.setVisibility(View.GONE);
                mBtnReject.setVisibility(View.VISIBLE);
                mBtnApprove.setVisibility(View.VISIBLE);

                break;

            case REQUEST_SENT_BY_ME:
                if(!mRequest.getmState().equalsIgnoreCase("rented"))
                    text = "Your request for <font color='red'>%s</font> is pending on <font color='red'>%s</font>";
                else
                    text = "<font color='red'>%s</font> has already approved your request for <font color='red'>%s</font>";
                 text2=String.format(text,mRequest.getmProduct().getmUnit().getmUnitName(),mRequest.getmOwnerId());
                tvInfo.setText(Html.fromHtml(text2), TextView.BufferType.SPANNABLE);

                mBtnCancel.setVisibility(View.VISIBLE);
                mBtnReject.setVisibility(View.GONE);
                mBtnApprove.setVisibility(View.GONE);
                break;
        }

        return view;
    }

    private class OnClickListerner implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            RentalRequest.RequestStatus status = null;
            switch(v.getId()){
                case R.id.btn_cancel:
                    status  = RentalRequest.RequestStatus.CANCEL;
                    break;

                case R.id.btn_approve:
                    status  = RentalRequest.RequestStatus.APPROVED;
                    break;

                case R.id.btn_reject:
                    status =  RentalRequest.RequestStatus.REJECTED;
                    break;
            }
            RentalRequest.updateRequestStatus(getActivity(),status,mRequest.getmProduct(),mParentActivity);
        }
    }
}
