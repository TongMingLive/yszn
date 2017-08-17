package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2016/12/7.
 */

public class Sign extends AppCompatActivity implements OnClickListener {

    EditText qqname,qqpsw;

    String name,pwd,str,str1;

    Button tj,login,youke;

    private SharedPreferences spf = null;

    private SharedPreferences.Editor editor = null;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        spf = this.getSharedPreferences("user",MODE_PRIVATE);
        editor = spf.edit();

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        FindView();

        //注册点击事件
        tj.setOnClickListener(this);
        login.setOnClickListener(this);
        youke.setOnClickListener(this);
    }

    //FindView
    public void FindView(){
        qqname = (EditText) findViewById(R.id.QQname);
        qqpsw = (EditText) findViewById(R.id.qqpsw);
        tj = (Button) findViewById(R.id.btn_tj);
        login = (Button) findViewById(R.id.sign_btn_login);
        youke = (Button) findViewById(R.id.sign_btn_youke);
    }

    //验证用户名
    public boolean reguser(View view){
        if (TextUtils.isEmpty(qqname.getText().toString())){
            showToast(Sign.this, "请输入账号", 1000);
            return false;
        }else{
            return true;
        }
    }

    //验证密码
    public boolean regpwd(View view){
        if (TextUtils.isEmpty(qqpsw.getText().toString())){
            showToast(Sign.this, "请输入密码", 1000);
            return false;
        }else if (!qqpsw.getText().toString().trim().matches("[0-9]*")) {
            showToast(Sign.this,"密码只能输入数字",1000);
            return false;
        }else{
            return true;
        }
    }

    //提交按钮
    public void tijiao(View view){
        //获取用户输入的内容
        name = qqname.getText().toString();
        pwd = qqpsw.getText().toString();
        if (reguser(view)&&regpwd(view)){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("userName",name);
                    map.put("password",pwd);
                    str = HttpUtil.doPost(HttpUtil.path+"SelectNameServlet",map);
                    if (str.equals("error")){
                        handler.sendEmptyMessage(0x000);
                    }else {
                        try {
                            if (str.equals("true")){
                                handler.sendEmptyMessage(0x124);
                            }else {
                                str1 = HttpUtil.doPost(HttpUtil.path+"InsertUserServlet",map);
                                if (str1.equals("true")){
                                    //将用户数据保存
                                    User user = new User();
                                    user.setUserName(name);
                                    user.setUserPassword(pwd);
                                    user.setUserType(0);
                                    App.user = user;
                                    //将用户数据放入轻量级数据
                                    editor.putString("username",name);
                                    editor.putString("password",pwd);
                                    editor.commit();
                                    handler.sendEmptyMessage(0x123);
                                }else {
                                    handler.sendEmptyMessage(0x125);
                                }

                            }
                        } catch (Exception e) {
                            handler.sendEmptyMessage(0x000);
                        }
                    }
                }
            }.start();
        }
    }

    //点击事件
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_tj:
                tijiao(view);
                break;
            case R.id.sign_btn_login:
                startActivity(new Intent(Sign.this, Login.class));
                finish();
                break;
            case R.id.sign_btn_youke:
                User user = new User();
                user.setUserType(-1);
                App.user = user;
                startActivity(new Intent(Sign.this, Index.class));
                finish();
                break;
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                showToast(Sign.this,"注册成功", 1000);
                startActivity(new Intent(Sign.this, SexSign.class));
                finish();
            }else if (msg.what == 0x124){
                showToast(Sign.this, "存在重复用户名", 1000);
            }else if (msg.what == 0x125){
                showToast(Sign.this, "注册超时，请重试", 1000);
            }else if (msg.what == 0x000){
                showToast(Sign.this, "网络链接失败，请重试", 1000);
            }
        }
    };

}
