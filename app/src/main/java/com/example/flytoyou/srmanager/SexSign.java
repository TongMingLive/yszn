package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2017/3/6.
 */

public class SexSign extends AppCompatActivity implements OnClickListener {

    TextView man,woman;

    Button tg;

    //自定义吐司时间
    public static void showToast(final Activity activity, final String word, final long time){
        activity.runOnUiThread(new Runnable() {
            public void run() {
                final Toast toast = Toast.makeText(activity, word, Toast.LENGTH_LONG);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        toast.cancel();
                    }
                }, time);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sexsign);

        FindView();

        man.setOnClickListener(this);
        woman.setOnClickListener(this);
        tg.setOnClickListener(this);
    }

    //FindView
    public void FindView(){
        man = (TextView) findViewById(R.id.btn_man);
        woman = (TextView) findViewById(R.id.btn_woman);
        tg = (Button) findViewById(R.id.sex_btn_tg);
    }

    //绅士按钮
    public void man(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("userName",App.user.getUserName());
                map.put("userSex","男");
                String str = HttpUtil.doPost(HttpUtil.path+"InsertSexServlet",map);
                if (str.equals("true")){
                    App.user.setUserSex("男");
                    handler.sendEmptyMessage(0x123);
                }else {
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    //女神按钮
    public void woman(){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("userName",App.user.getUserName());
                map.put("userSex","女");
                String str = HttpUtil.doPost(HttpUtil.path+"InsertSexServlet",map);
                if (str.equals("true")){
                    App.user.setUserSex("女");
                    handler.sendEmptyMessage(0x123);
                }else {
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    //按钮注册点击事件
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btn_man:
                showToast(SexSign.this,"接下来是职业", 1000);
                man();
                break;
            case R.id.btn_woman:
                showToast(SexSign.this,"接下来是职业", 1000);
                woman();
                break;
            case R.id.sex_btn_tg:
                showToast(SexSign.this,"接下来是职业", 1000);
                startActivity(new Intent(SexSign.this, JobSign.class));
                finish();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                showToast(SexSign.this,"接下来是职业", 1000);
                startActivity(new Intent(SexSign.this, JobSign.class));
            }else if (msg.what == 0x124){
                showToast(SexSign.this, "选择失败，请重试！", 1000);
            }
        }
    };

}
