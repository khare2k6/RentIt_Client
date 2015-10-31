package com.rentit.projectr;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AKhare on 01-Aug-15.
 */
public class NavigationDrawerItem {
    private Drawable mMenuIcon;

    public Drawable getmMenuIcon() {
        return mMenuIcon;
    }

    public String getmMenuTitle() {
        return mMenuTitle;
    }

    public String getmMenuExtra() {
        return mMenuExtra;
    }



    @Override
    public boolean equals(Object o) {
        if(o instanceof NavigationDrawerItem){
            NavigationDrawerItem obj = (NavigationDrawerItem)o;
            return getmMenuTitle().equalsIgnoreCase(obj.getmMenuTitle());
        }
        return false;
    }

    public void setmMenuExtra(String mMenuExtra) {
        this.mMenuExtra = mMenuExtra;
    }

    private String mMenuTitle;
    private String mMenuExtra;

    public NavigationDrawerItem(Drawable icon,String title,String extra){
        mMenuIcon = icon;
        mMenuTitle = title;
        mMenuExtra = extra;
    }

    public static class NavigationDrawerItemAdapter extends ArrayAdapter<NavigationDrawerItem>{
        private ArrayList<NavigationDrawerItem>mListMenuItems;

        public NavigationDrawerItemAdapter(Context context, int resource, List<NavigationDrawerItem> objects) {
            super(context, resource, objects);
            mListMenuItems = new ArrayList<NavigationDrawerItem>(objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row_drawer_content,parent,false);
            ImageView itemIcon = (ImageView) view.findViewById(R.id.iv_drawer_row);
            TextView itemName = (TextView)view.findViewById(R.id.tv_drawer_row_menu_title);
            itemName.setTextColor(parent.getContext().getResources().getColor(R.color.textColor));
            TextView itemExtra= (TextView)view.findViewById(R.id.tv_drawer_row_menu_extra);
            itemExtra.setTextColor(parent.getContext().getResources().getColor(R.color.textColor));

            NavigationDrawerItem item = mListMenuItems.get(position);
            itemIcon.setImageDrawable(item.getmMenuIcon());
            itemName.setText(item.getmMenuTitle());
            itemExtra.setText(item.getmMenuExtra());
            return view;
        }
    }


}
