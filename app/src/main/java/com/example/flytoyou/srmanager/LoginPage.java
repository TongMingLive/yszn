package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2017/3/2.
 */

public class LoginPage extends AppCompatActivity {

    private TextView name,time;

    private EditText liuyan;

    private Button yes,fh;

    private String resname,login,restime;

    private int resid;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        FindView();

        //留言按钮
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liuyan.getText().toString().length()==0){
                    Toast.makeText(LoginPage.this, "请输入留言", Toast.LENGTH_SHORT).show();
                }else {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map =new HashMap<String, Object>();
                            map.put("resId", resid);
                            map.put("resMessger",liuyan.getText().toString());
                            String str = HttpUtil.doPost(HttpUtil.path+"UpdateResMessgerServlet",map);
                            if (str.equals("true")){
                                handler.sendEmptyMessage(0x123);
                            }else {
                                handler.sendEmptyMessage(0x124);
                            }
                        }
                    }.start();
                }
            }
        });

        //返回按钮
        fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //FindView
    public void FindView(){
        name = (TextView) findViewById(R.id.tex_resname);
        time = (TextView) findViewById(R.id.tex_restime);
        liuyan = (EditText) findViewById(R.id.edt_liuyan);
        yes = (Button) findViewById(R.id.btn_liuyan);
        fh = (Button) findViewById(R.id.lp_btn_fh);

        //获取上个页面的信息
        Intent mesg = getIntent();
        resname = mesg.getStringExtra("resname");
        login = mesg.getStringExtra("login");
        restime = mesg.getStringExtra("time");
        resid = Integer.parseInt(mesg.getStringExtra("resid"));
        //将信息插入控件
        name.setText(resname);
        time.setText(restime);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Toast.makeText(LoginPage.this, "留言成功", Toast.LENGTH_SHORT).show();
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(LoginPage.this, "留言失败，发生未知错误", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
