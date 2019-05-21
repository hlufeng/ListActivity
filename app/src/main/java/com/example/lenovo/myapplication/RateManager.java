package com.example.lenovo.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RateManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public RateManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME;
    }

    public void add(RateItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("curname", item.getCurName());
        values.put("currate", item.getCurRate());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public void addAll(List<RateItem> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (RateItem item : list) {
            ContentValues values = new ContentValues();
            values.put("curname", item.getCurName());
            values.put("currate", item.getCurRate());
            db.insert(TBNAME, null, values);
        }
        db.close();
    }

    public List<RateItem> listAll() {
        List<RateItem> rateList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        //返回一个光标，光标一开始在标题行
        if (cursor != null) {
            rateList = new ArrayList<RateItem>();
            while (cursor.moveToNext()) {   //将游标移到下一行（返回能否下移，即下面是否有数据）
                RateItem item = new RateItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));//获得光标所在行的整数（“ID”所在的索引）
                item.setCurName(cursor.getString(cursor.getColumnIndex("CURNAME")));//获得字符串
                item.setCurRate(cursor.getString(cursor.getColumnIndex("CURRATE")));
                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;
    }
}
