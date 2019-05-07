package com.example.lenovo.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lenovo.myapplication.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyAdapter extends ArrayAdapter {

    private  static final String TAG ="com.example.lenovo.myapplication.MyAdapter";

    public MyAdapter(Context context, int resource, ArrayList<HashMap<String,String>> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView =convertView;//一个map对应的view
        if(itemView==null){   //将xml布局转化为view对象
            itemView=LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Map<String,String> map=(Map<String, String>) getItem(position);//根据位置得到map
        TextView title=(TextView)itemView.findViewById(R.id.itemTitle);//取出map中的title控件
        TextView detail=(TextView)itemView.findViewById(R.id.itemDetail);//取出map中的detail控件

        title.setText("Title:"+map.get("ItemTitle"));
        detail.setText("Detail:"+map.get("ItemDetail"));

        return itemView;
    }
}
