package com.rentit.projectr;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AKhare on 10-Aug-15.
 */
public class NameValueView extends LinearLayout {
    private TextView mTvLabel;
    private TextView mTvValue;
    private EditText mEtValue;
    private boolean mIsUserInput = false;

    public NameValueView(Context context) {
        super(context);
        initViews(context,null);
    }

    public NameValueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public NameValueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    public void isUserInput(boolean isUserInput){
        mIsUserInput = isUserInput;
        updateView();
    }

    private void updateView() {
        if(mIsUserInput){
            mEtValue.setVisibility(View.VISIBLE);
            mTvValue.setVisibility(View.GONE);
        }else{
            mEtValue.setVisibility(View.GONE);
            mTvValue.setVisibility(View.VISIBLE);
        }
    }

    private void initViews(Context context, AttributeSet attrs) {
        String label;
        int colorId =0;
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NameValueView, 0, 0);
        try {
            label = a.getString(R.styleable.NameValueView_leftLabel);
            colorId = a.getResourceId(R.styleable.NameValueView_leftLabelColor, android.R.style.TextAppearance_DeviceDefault);
        } finally {
            a.recycle();
        }
        LayoutInflater.from(context).inflate(R.layout.layout_name_value, this);
        mTvLabel = (TextView) this.findViewById(R.id.tv_label_view);
        mTvValue = (TextView) this.findViewById(R.id.tv_value_view);
        mEtValue = (EditText) this.findViewById(R.id.et_value_view);

        mTvLabel.setText(label);
        if(colorId != android.R.style.TextAppearance_DeviceDefault)
            mTvLabel.setTextColor(context.getResources().getColor(colorId));
    }

    public void setValueOfLabel(String text){
        mTvValue.setText(text);
    }
    private String getValueFromTextView(){return mTvValue.getText().toString();}
    private String getValueFromEditText(){return mEtValue.getText().toString();}

    public void setLabel(String text){
        mTvLabel.setText(text);
    }
    public String getValue(){
        String ret = null;
        return (mIsUserInput ? getValueFromEditText():getValueFromTextView());
    }
}
