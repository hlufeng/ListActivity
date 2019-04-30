package com.example.lenovo.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[]={"wait..."};
    Handler handler;
    public final String TAG ="RateListActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_rate_list);父类ListActivity已经有布局了
//        List<String> list1=new ArrayList<String>();
//        for(int i=1;i<100;i++){
//            list1.add("item"+i);
//        }

        ListAdapter listAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(listAdapter);

        Thread t=new Thread(this);
        t.start();

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==7){
                    List<String> list= (List<String>) msg.obj;
                    ListAdapter listAdapter=new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list);
                    setListAdapter(listAdapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<String> retList =new ArrayList<String>();

        Bundle bundle =new Bundle();
        Document doc = null;
        try {
            try {
                Thread.sleep(3000);
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
                retList.add(str+"==>"+val);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        Message msg =handler.obtainMessage(7);
        msg.obj=retList;
        handler.sendMessage(msg);

    }
}
