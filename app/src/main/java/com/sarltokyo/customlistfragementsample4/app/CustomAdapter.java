package com.sarltokyo.customlistfragementsample4.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by osabe on 14/08/19.
 */
public class CustomAdapter extends ArrayAdapter<Entry> {
    private LayoutInflater mLayoutInflater;

    public CustomAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        mLayoutInflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setData(List<Entry> data) {
        clear();
        if (data != null) {
//            addAll(data); // addAll is usable from API level 11.
            for (Entry entry: data) {
                add(entry);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを取得
        Entry item = (Entry)getItem(position);

        // 同じ行に表示されるViewは使い回しされるため初回だけ生成
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(
                    R.layout.list_item, null);
        }

        // データをViewの各Widgetにセット
        TextView textView = (TextView)convertView.findViewById(R.id.text);
        textView.setText(item.toString());

        return convertView;
    }
}
