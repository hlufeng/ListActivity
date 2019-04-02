package com.example.lenovo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main4Activity extends AppCompatActivity {
    EditText inp;
    TextView oup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        inp=(EditText)findViewById(R.id.hl_inp);
        oup=(TextView) findViewById(R.id.hl_oup);
    }
    public void onClick(View btn){
        String in=inp.getText().toString();
        if(in.length()==0){
            Toast.makeText(this,"请输入金额！",Toast.LENGTH_SHORT).show();
        }else {
            float f=Float.parseFloat(in);
            float ff=0;
            if(btn.getId()==R.id.hl_dollor){
                ff=f*=(1/6.7f);
            }else if (btn.getId()==R.id.hl_ero){
                ff=f*=(1/11f);
            }else {
                ff=f*=500;
            }
            oup.setText(""+Math.round(100*ff)/100f);
        }
    }
}
