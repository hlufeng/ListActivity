package com.example.lenovo.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class ConfigActivity extends AppCompatActivity {
    EditText dollorset;
    EditText eroset;
    EditText hyset;
    public final String TAG="ConfigActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent =getIntent();
        float dollor2=intent.getFloatExtra("dollor",0.0f);
        float ero2=intent.getFloatExtra("ero",0.0f);
        float hy2=intent.getFloatExtra("hy",0.0f);

        dollorset =(EditText)findViewById(R.id.config_dollor);
        eroset =(EditText)findViewById(R.id.config_ero);
        hyset =(EditText)findViewById(R.id.config_hy);

        dollorset.setText(String.valueOf(dollor2));
        eroset.setText(String.valueOf(ero2));
        hyset.setText(String.valueOf(hy2));

        Log.i(TAG, "onCreate: dollor2=: "+dollor2);
        Log.i(TAG, "onCreate: ero2=: "+ero2);
        Log.i(TAG, "onCreate: hy2=: "+hy2);
    }
    public void save(View btn){
        float newdollor =Float.parseFloat(dollorset.getText().toString());
        float newero =Float.parseFloat(eroset.getText().toString());
        float newhy =Float.parseFloat(hyset.getText().toString());
        Intent intent=getIntent();
        Bundle bundle=new Bundle();
        bundle.putFloat("dollorset",newdollor);
        bundle.putFloat("eroset",newero);
        bundle.putFloat("hyset",newhy);
        intent.putExtras(bundle);
        setResult(2,intent);
        Log.i(TAG, "save: dollorset=: "+newdollor);
        Log.i(TAG, "save: eroset=: "+newero);
        Log.i(TAG, "save: hyset=: "+newhy);
        finish();
    }

}
