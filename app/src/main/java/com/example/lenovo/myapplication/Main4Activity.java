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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Main4Activity extends AppCompatActivity implements Runnable {
    EditText inp;
    TextView oup;
    float dollorrate;
    float erorate;
    float hyrate;
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
        Log.i(TAG, "onCreate: sharedPreferences获取成功");

        //开启子线程
        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what==5){
                    String str =(String)msg.obj;
                    Log.i(TAG, "handleMessage: str:"+str);
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
                ff=f*=(1/dollorrate);
            }else if (btn.getId()==R.id.hl_ero){
                ff=f*=(1/erorate);
            }else {
                ff=f*=(1/hyrate);
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

        //创建消息，用来告知主进程
        Message msg =handler.obtainMessage(5);
        //上面括号里的5等价于msg.what(5);
        msg.obj="我的小obj!";
        handler.sendMessage(msg);

        //获取网页
        URL url = null;
        try {
            url = new URL("www.usd-cny.com/icbc.htm");
            HttpURLConnection http =(HttpURLConnection)url.openConnection();
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html:"+html);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
