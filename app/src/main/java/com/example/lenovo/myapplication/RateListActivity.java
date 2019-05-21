package com.example.lenovo.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable {
    String data[] = {"wait..."};
    Handler handler;
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";
    public final String TAG = "RateListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List", "lastRateDateStr=" + logDate);
//        setContentView(R.layout.activity_rate_list);父类ListActivity已经有布局了
//        List<String> list1=new ArrayList<String>();
//        for(int i=1;i<100;i++){
//            list1.add("item"+i);
//        }

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(listAdapter);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 7) {
                    List<String> list = (List<String>) msg.obj;
                    ListAdapter listAdapter = new ArrayAdapter<String>(RateListActivity.this, android.R.layout.simple_list_item_1, list);
                    setListAdapter(listAdapter);
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        //获取网络数据，放入list带回到主线程中
        List<String> retList = new ArrayList<String>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run", "curDateStr:" + curDateStr + " logDate:" + logDate);
        if (curDateStr.equals(logDate)) {
            //如果相等，则不从网络中获取数据
            Log.i("run", "日期相等，从数据库中获取数据");
            RateManager manager = new RateManager(this);
            for (RateItem rateItem : manager.listAll()) {
                retList.add(rateItem.getCurName() + "=>" + rateItem.getCurRate());
            }
        } else {
            //从网络获得数据
            Log.i("run", "日期不相等，从网络中获取数据");
            Bundle bundle = new Bundle();
            Document doc = null;
            try {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i(TAG, "run: " + doc.title());
                Elements tables = doc.getElementsByTag("table");
                Element table1 = tables.get(0);

                Elements tds = table1.getElementsByTag("td");
                //更新数据库
                List<RateItem> rateList = new ArrayList<RateItem>();

                for (int i = 0; i < tds.size(); i += 6) {
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i + 5);
                    Log.i(TAG, "run: textname:" + td1.text());
                    Log.i(TAG, "run: texthl:" + td2.text());
                    String str = td1.text();
                    String val = td2.text();

                    Log.i(TAG, "run: " + str + "==>" + val);
                    retList.add(str + "==>" + val);
                    rateList.add(new RateItem(str, val));
                }
                //将数据写入到数据库中
                RateManager manager = new RateManager(this);
                manager.deleteAll();
                manager.addAll(rateList);

                //记录更新日期
                SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sp.edit();
                edit.putString(DATE_SP_KEY, curDateStr);
                edit.commit();
                Log.i("run", "更新日期结束：" + curDateStr);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Message msg = handler.obtainMessage(7);
        msg.obj = retList;
        handler.sendMessage(msg);

    }
}
