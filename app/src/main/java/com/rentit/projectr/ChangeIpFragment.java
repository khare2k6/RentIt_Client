package com.rentit.projectr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by AKhare on 15-Aug-15.
 */
public class ChangeIpFragment extends BaseFragment {

    private Button mSubmitButton;
    private EditText mEtIp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       View view = inflater.inflate(R.layout.layout_set_ip,container,false);
        mSubmitButton = (Button) view.findViewById(R.id.btn_submit);
        mEtIp= (EditText) view.findViewById(R.id.et_ip);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(mEtIp.getText().toString())){
                    PreferenceManager.getmInstance(getActivity()).setIp(mEtIp.getText().toString());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mEtIp !=null){
            mEtIp.setText(PreferenceManager.getmInstance(getActivity()).getServerIp());
        }
    }
}
