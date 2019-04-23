package com.example.lenovo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Main3Activity extends AppCompatActivity {
    TextView score;
    TextView score2;
    public final String TAG="Main3Activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        score=(TextView) findViewById(R.id.Socre);
        score2=(TextView) findViewById(R.id.Socre2);
    }

    @Override
    protected void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("teama_score",score.getText().toString());
        outState.putString("teamb_score",score2.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String teama_score=savedInstanceState.get("teama_score").toString();
        String teamb_score=savedInstanceState.get("teamb_score").toString();
        score.setText(teama_score);
        score2.setText(teamb_score);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onRestart() {
        Log.i(TAG, "onRestart: ");
        super.onRestart();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

    public void btnAdd3(View btn){
        if(btn.getId()==R.id.btn_3){
            showscore(3);
        }else {
            showscore2(3);
        }
    }
    public void btnAdd2(View btn){
        if(btn.getId()==R.id.btn_2){
            showscore(2);
        }else {
            showscore2(2);
        }
    }
    public void btnAdd1(View btn){
        if(btn.getId()==R.id.btn_1){
            showscore(1);
        }else {
            showscore2(1);
        }
    }
    public void btnReset(View btn){
        score.setText("0");
        score2.setText("0");
    }
    public void showscore(int inc){
        Log.i("show","inc="+inc);
        int oldscore=Integer.parseInt(score.getText().toString());
        int newscore=oldscore+inc;
        score.setText(""+newscore);
    }
    public void showscore2(int inc){
        Log.i("show","inc="+inc);
        int oldscore=Integer.parseInt(score2.getText().toString());
        int newscore=oldscore+inc;
        score2.setText(""+newscore);
    }
}
