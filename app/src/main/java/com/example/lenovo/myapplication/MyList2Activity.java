package com.example.lenovo.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    Handler handler;
    private List<HashMap<String,String>> listItems; // 存放文字、图片信息
    private SimpleAdapter listItemAdapter;//适配器
    private  static final String TAG ="MyList2Activity";
    SimpleAdapter adapter;

    //定义初始化方法
    private void initListView(){
        //初始化列表
        listItems=new ArrayList<HashMap<String, String>>();
        for(int i=0;i<10;i++){
            HashMap<String,String> map=new HashMap<String,String>();
            map.put("ItemTitle","Rate:"+i);
            map.put("ItemDetail","detail:"+i);
            listItems.add(map);
        }
        //使用适配器让列表元素和视图控件一一对应
        listItemAdapter=new SimpleAdapter(this,listItems,//数据源
                R.layout.list_item,//布局
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_item);
//        initListView();
//        this.setListAdapter(listItemAdapter);//使用Android studio提供的适配器
        Thread thread =new Thread(this);
        thread.start();

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==7){
                    listItems= (List<HashMap<String, String>>) msg.obj;
                    adapter=new SimpleAdapter(MyList2Activity.this,listItems,
                            R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail});
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };

        getListView().setOnItemClickListener(this);//列表点击监听器,使用this需要继承接口
        getListView().setOnItemLongClickListener(this);
//        MyAdapter myAdapter =new MyAdapter(this,R.layout.list_item,listItems);
//        this.setListAdapter(myAdapter);//使用自己定义的适配器
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<HashMap<String,String>> retList =new ArrayList<HashMap<String, String>>();
        boolean marker=false;

        Bundle bundle =new Bundle();
        Document doc = null;
        try {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables =doc.getElementsByTag("table");
            Element table1=tables.get(0);

            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 =tds.get(i);
                Element td2 =tds.get(i+5);
                Log.i(TAG, "run: textname:"+td1.text());
                Log.i(TAG, "run: texthl:"+td2.text());
                String str =td1.text();
                String val =td2.text();

                Log.i(TAG, "run: "+str+"==>"+val);

                HashMap<String,String> map=new HashMap<String, String>();
                map.put("ItemTitle",str);
                map.put("ItemDetail",val);
                retList.add(map);
            }
            marker=true;//表示以上try中的代码没有错误，已成功运行
        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg =handler.obtainMessage(7);
        //使用msg中的arg1属性标记操作是否成功
        if(marker){
            msg.arg1=1;
        }else {
            msg.arg1=0;
        }
        msg.obj=retList;
        handler.sendMessage(msg);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //通过点击监听器，获取位置，再用这个位置进行定位，来提取这个位置控件的map，接着从map中提取出value
        Log.i(TAG, "onItemClick: parent："+parent);
        Log.i(TAG, "onItemClick: view："+view);
        Log.i(TAG, "onItemClick: position："+position);
        Log.i(TAG, "onItemClick: id："+id);
        HashMap<String,String> map=(HashMap<String, String>)getListView().getItemAtPosition(position);
        String titleStr=map.get("ItemTitle");
        String detailStr=map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr："+titleStr);
        Log.i(TAG, "onItemClick: detailStr："+detailStr);

        //直接通过控件来获取value，前提是这个数据没有被处理过
        TextView title=view.findViewById(R.id.itemTitle);
        TextView detail=view.findViewById(R.id.itemDetail);
        String title2=title.getText().toString();
        String detail2=detail.getText().toString();
        Log.i(TAG, "onItemClick: title2："+title2);
        Log.i(TAG, "onItemClick: detail2："+detail2);

        Intent rateCalc =new Intent(this,RateCalcActivity.class);
        rateCalc.putExtra("title",titleStr);
        rateCalc.putExtra("rate",Float.parseFloat(detailStr));
        startActivity(rateCalc);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按");
        //删除操作
//        listItems.remove(position);
//        adapter.notifyDataSetChanged();//注意是适配器刷新
        //构造对话框进行确认操作
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认是否删除").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "onClick: 对话框事件处理");
                listItems.remove(position);
                adapter.notifyDataSetChanged();//注意是适配器刷新
            }
        })
                .setNegativeButton("否",null);
        builder.create().show();
        Log.i(TAG, "onItemLongClick: size:"+listItems.size());
        return true;//true表示屏蔽短按事件
    }
}
