package com.example.lenovo.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main4Activity extends AppCompatActivity implements Runnable {
    EditText inp;
    TextView oup;
    float dollorrate;
    float erorate;
    float hyrate;
    private String updateDate="";
    public final String TAG ="Main4Activity";
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        inp=(EditText)findViewById(R.id.hl_inp);
        oup=(TextView) findViewById(R.id.hl_oup);

        //读取shareperfercences
        SharedPreferences sharedPreferences = getSharedPreferences("myrate",MODE_PRIVATE);
//        SharedPreferences sp=PreferenceManager.getDefaultSharedPreferences(this);
        dollorrate=sharedPreferences.getFloat("rate_dollor",6.7f);
        erorate=sharedPreferences.getFloat("rate_ero",11f);
        hyrate=sharedPreferences.getFloat("rate_hy",1/500f);
        updateDate=sharedPreferences.getString("update_date","");
        Log.i(TAG, "onCreate: sharedPreferences获取成功");

        //获取当前系统时间
        Date today=Calendar.getInstance().getTime();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        final String todayStr =sdf.format(today);

        if(!todayStr.equals(updateDate)){
            Log.i(TAG, "onCreate: 需要更新");
            //开启子线程
            Thread t = new Thread(this);
            t.start();
        }else {
            Log.i(TAG, "onCreate: 不需要更新");
        }

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    Bundle bdl=(Bundle)msg.obj;
//                    String str =(String)msg.obj;
                    dollorrate=bdl.getFloat("dollor_rate");
                    erorate=bdl.getFloat("ero_rate");
                    hyrate=bdl.getFloat("hy_rate");
                    Log.i(TAG, "handleMessage: dr:"+dollorrate+"er:"+erorate+"hr:"+hyrate);
                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("update_date",todayStr);
                    editor.putFloat("rate_dollor",dollorrate);
                    editor.putFloat("rate_ero",erorate);
                    editor.putFloat("rate_hy",hyrate);
                    editor.apply();
                }
                super.handleMessage(msg);
            }
        };

    }
    public void onClick(View btn){
        String in=inp.getText().toString();
        if(in.length()==0){
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
        }else {
            float f=Float.parseFloat(in);
            float ff=0;
            if(btn.getId()==R.id.hl_dollor){
                ff=f*=(dollorrate);
            }else if (btn.getId()==R.id.hl_ero){
                ff=f*=(erorate);
            }else {
                ff=f*=(hyrate);
            }
            oup.setText(""+Math.round(100*ff)/100f);
        }
    }
    public void open_config(View btn){
        getConfig();
    }

    private void getConfig() {
        Intent config =new Intent(this,ConfigActivity.class);
        config.putExtra("dollor",dollorrate);
        config.putExtra("ero",erorate);
        config.putExtra("hy",hyrate);
        Log.i(TAG, "open_score: dollor="+dollorrate);
        Log.i(TAG, "open_score: ero="+erorate);
        Log.i(TAG, "open_score: hy="+hyrate);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu){
            getConfig();
        }else if (item.getItemId()==R.id.open_list){
            //打开窗口列表
            Intent list =new Intent(this,MyListActivity.class);
            startActivity(list);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==2){
            Bundle bundle=data.getExtras();
            dollorrate=bundle.getFloat("dollorset",0.1f);
            erorate=bundle.getFloat("eroset",0.1f);
            hyrate=bundle.getFloat("hyset",0.1f);
            Log.i(TAG, "onActivityResult: dollorrate"+dollorrate);
            Log.i(TAG, "onActivityResult: erorate"+erorate);
            Log.i(TAG, "onActivityResult: hyrate"+hyrate);

            SharedPreferences sharedPreferences = getSharedPreferences("myrate",MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putFloat("rate_dollor",dollorrate);
            editor.putFloat("rate_ero",erorate);
            editor.putFloat("rate_hy",hyrate);
            editor.commit();
            Log.i(TAG, "onActivityResult: SharedPreferences设置成功");
        }
    }

    @Override
    public void run() {
        //不能debug和没有虚拟机的我用不上这段代码。。。。
//        Log.i(TAG, "run: running...");
//        for (int i=1;i<=6;i++){
//            Log.i(TAG, "run: running:"+i);
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        Bundle bundle =new Bundle();

        //获取网页1.0
//        URL url = null;
//        try {
//            url = new URL("http://www.usd-cny.com/icbc.htm");
//            HttpURLConnection http =(HttpURLConnection)url.openConnection();
//            InputStream in = http.getInputStream();
//            String html = inputStream2String(in);
//            Log.i(TAG, "run: html:"+html);
//            Document doc =Jsoup.parse(html);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        //获取网页2.0
        bundle=getFromBOC();
        //创建消息，用来告知主进程
        Message msg =handler.obtainMessage(5);
//        Log.i(TAG, "run: "+handler);
        //上面括号里的5等价于msg.what(5);
//        msg.obj="我的小obj!";
        msg.obj=bundle;
        handler.sendMessage(msg);

    }

    private Bundle getFromBOC() {
        Bundle bundle = new Bundle();
        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: "+doc.title());
            Elements tables =doc.getElementsByTag("table");
//            遍历table，看所需的内容在第几个table中
//            int i=1;
//            for(Element table:tables){
//                Log.i(TAG, "run: table["+i+"]"+table);
//                i++;
//            }
            Element table1=tables.get(0);

            Elements tds=table1.getElementsByTag("td");
            for(int i=0;i<tds.size();i+=6){
                Element td1 =tds.get(i);
                Element td2 =tds.get(i+5);
                Log.i(TAG, "run: textname:"+td1.text());
                Log.i(TAG, "run: texthl:"+td2.text());
                String str =td1.text();
                String val =td2.text();
                if("美元".equals(str)){
                    bundle.putFloat("dollor_rate",100f/Float.parseFloat(val));
                }else if ("欧元".equals(str)){
                    bundle.putFloat("ero_rate",100f/Float.parseFloat(val));
                }else if ("韩元".equals(str)){
                    bundle.putFloat("hy_rate",100f/Float.parseFloat(val));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }
}
