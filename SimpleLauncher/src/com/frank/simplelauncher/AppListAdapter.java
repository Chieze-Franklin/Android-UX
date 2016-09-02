package com.frank.simplelauncher;

import java.util.ArrayList;
import java.util.Collection;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppListAdapter extends ArrayAdapter<AppInfo> {

	private final LayoutInflater mInflater;

    public AppListAdapter (Context context) {
        super(context, android.R.layout.simple_list_item_2);

        mInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<AppInfo> data) {
        clear();
        if (data != null) {
            addAll(data);
        }
    }
    
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void addAll(Collection<? extends AppInfo> items) {
        //If the platform supports it, use addAll, otherwise add in loop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            super.addAll(items);
        }else{
            for(AppInfo item: items){
                super.add(item);
            }
        }
    }
    
    /**
     * Populate new items in the list.
     */
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_items, parent, false);
        } else {
            view = convertView;
        }

        AppInfo item = getItem(position);
        ((ImageView)view.findViewById(R.id.item_app_icon)).setImageDrawable(item.getIcon());
        ((TextView)view.findViewById(R.id.item_app_label)).setText(item.getAppName());
        ((TextView)view.findViewById(R.id.item_app_name)).setText(item.getPackageName());

        return view;
    }
}