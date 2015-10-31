package com.rentit.projectr;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akhare on 11-Aug-15.
 */
public class TestCustomView extends LinearLayout {
    private ImageView mIvCollapseIcon;
    private TextView mTitle;
    private LinearLayout mRowContainer;
    private boolean mShowContainer;
    private HashMap<String,String>mNameValueMap;

    public TestCustomView(Context context) {
        super(context);
    }

    public TestCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public TestCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    public void setNameValues(HashMap<String,String>map,boolean isUserInput) {
        mNameValueMap = map;
        for (Map.Entry<String, String> eachEntry : mNameValueMap.entrySet()) {
            NameValueView nmView = new NameValueView(getContext());
            nmView.setId(eachEntry.getKey().hashCode());
            nmView.isUserInput(isUserInput);
            TextView tvLabel = (TextView) nmView.findViewById(R.id.tv_label_view);

            nmView.setLabel(eachEntry.getKey());
            nmView.setValueOfLabel(eachEntry.getValue());
            mRowContainer.addView(nmView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public void setTitle(String title){
        if (mTitle !=null)
            mTitle.setText(title);
    }

    public void initViews(Context context,AttributeSet attrs){
        LayoutInflater.from(context).inflate(R.layout.layout_custom_view, this);
        mIvCollapseIcon = (ImageView) this.findViewById(R.id.iv_custom_view_expand_collapse_icon);
        mRowContainer = (LinearLayout) this.findViewById(R.id.ll_custom_view_rows_container);
        mTitle = (TextView) this.findViewById(R.id.tv_custom_view_title);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        try {
            String title = a.getString(R.styleable.CustomView_Viewtitle);
            mTitle.setText(title);
        } finally {
            a.recycle();
        }

        mIvCollapseIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowContainer = !mShowContainer;
                if(mShowContainer)
                    mRowContainer.setVisibility(View.VISIBLE);
                else
                    mRowContainer.setVisibility(View.GONE);
            }
        });
    }
}
