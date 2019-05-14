package com.example.lenovo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MyListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    List<String> data1=new ArrayList <String>();
    private String TAG="MyListActivity";
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);

        GridView listView =findViewById(R.id.mylist);
        String data[]={"111","2222"};

        for(int i =0;i<10;i++){
            data1.add("item"+i);
        }
        listView.setEmptyView(findViewById(R.id.nodata));

        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemClick: parent:"+parent);
        Log.i(TAG, "onItemClick: position:"+position);
        adapter.remove(parent.getItemAtPosition(position));//只有ArrayAdapter才有remove方法，否则只能通过listview来remove
//        adapter.notifyDataSetChanged();只有ArrayAdapter会自动执行这个方法（刷新）
    }
}
