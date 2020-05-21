package com.swufe.secondapp;

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

public class RateListActivity extends ListActivity implements Runnable{
    Handler handler;
    private String logDate="";
    private final String DATE_SP_KEY = "lastRateDateStr";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        SharedPreferences sp = getSharedPreferences("marate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY,"");
        Log.i("List","lastRateDateStr"+logDate);


        Thread t = new Thread(this);
        t.start();
        //开启子线程

        handler = new Handler(){
            public void  handleMessage(Message msg){
                if(msg.what == 7 ){
                    List<String> list1 = (List<String>) msg.obj;
                    ListAdapter adapter = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list1);
                    setListAdapter(adapter);
                    //adapter 实现数据到列表之间的过渡
                }
                super.handleMessage(msg);
            }
        };
    }
    @Override
    public void run() {
        //获取网络数据
        List<String> ratelist = new ArrayList<>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        Log.i("run","curDateSrt:" + curDateStr + "logDate:" +logDate);
        if(curDateStr.equals(logDate)) {
            Log.i("run", "日期相等，从数据库获取数据");
            RateManager rateManager = new RateManager(RateListActivity.this);
            for (RateItem rateItem : rateManager.listAll()) {
                ratelist.add(rateItem.getCurName() + "=>" + rateItem.getCurRate());
            }
        }
        else{
            Log.i("run", "日期不相等，从网络获取数据");
            Document doc = null;//另一种直接通过网址获得doc文件的方法
            try {
                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i("run","1");
                Elements tables = doc.getElementsByTag("table");
                Element table = tables.get(0);
                Elements tds = table.getElementsByTag("td");

                List<RateItem> rateList = new ArrayList<RateItem>();
                for (int i = 0; i < tds.size(); i += 6) {
                    Element td1 = tds.get(i);
                    Element td2 = tds.get(i + 5);
                    String str1 = td1.text();
                    String str2 = td2.text();
                    ratelist.add(str1 + "==>" + str2);
                    rateList.add(new RateItem(str1,str2));
                }

                RateManager rateManager = new RateManager(RateListActivity.this);
                rateManager.deleteAll();
                rateManager.addAll(rateList);

            } catch (IOException e) {
                e.printStackTrace();
            }

            SharedPreferences sp = getSharedPreferences("myrate",Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY,curDateStr);
            edit.commit();
            Log.i("run","更新日期完毕："+curDateStr);
        }

        Message msg = handler.obtainMessage(7);
        msg.obj = ratelist;
        handler.sendMessage(msg);
    }
}
