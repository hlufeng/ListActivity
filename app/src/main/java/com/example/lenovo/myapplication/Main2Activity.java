package com.example.lenovo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.math.BigDecimal;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {
    TextView out;
    EditText inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        out = (TextView) findViewById(R.id.textView2);
        inp =findViewById(R.id.in);
//        Button btn = findViewById(R.id.button1);
//        btn.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        double c=Double.parseDouble(inp.getText().toString());
        double f=c*1.8+32;
//        摄氏温标（C）和华氏温标（F）之间的换算关系为：
//        F=C×1.8+32;
        String f1 =String.valueOf(f);
        //1代表小数点后面的位数, 不足补0。f代表数据是浮点类型。保留2位小数就是“%.2f”。
        out.setText(f1+"°F");
    }
}