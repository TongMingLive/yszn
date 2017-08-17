package com.example.flytoyou.srmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences spf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        //设置全屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置虚拟按键透明
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        spf = this.getSharedPreferences("user",MODE_PRIVATE);

        //判断用户是否登陆过
        if (spf.getString("username",null) == null){
            //未登陆过
            //定时器跳转
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }
            },2000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            Map<String,Object> map = new HashMap<>();
                            map.put("userName",spf.getString("username",null));
                            map.put("password",spf.getString("password",null));
                            String str = HttpUtil.doPost(HttpUtil.path+"AndroidLoginServlet",map);
                            if (str.equals("error")){
                                handler.sendEmptyMessage(0x000);
                            }else {
                                try {
                                    JSONObject jsonObject = new JSONObject(str);
                                    if (jsonObject.getInt("userId")>0){
                                        //创建实体类对象存储账号信息
                                        User user = new User();
                                        user.setUserId(jsonObject.getInt("userId"));
                                        user.setUserType(jsonObject.getInt("userType"));
                                        user.setUserName(jsonObject.getString("userName"));
                                        user.setUserPassword(jsonObject.getString("userPassword"));
                                        user.setUserSex(jsonObject.getString("userSex"));
                                        user.setUserAge(jsonObject.getInt("userAge"));
                                        user.setJobId(jsonObject.getInt("jobId"));
                                        user.setUserAddress(jsonObject.getString("userAddress"));
                                        user.setUserImg(jsonObject.getString("userImg"));
                                        App.user = user;
                                        handler.sendEmptyMessage(0x123);
                                    }else {
                                        handler.sendEmptyMessage(0x124);
                                    }
                                } catch (JSONException e) {
                                    handler.sendEmptyMessage(0x000);
                                }
                            }
                        }
                    }.start();
                }
            },2000);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                startActivity(new Intent(MainActivity.this, Index.class));
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(MainActivity.this, "登陆失败，请重新登陆", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }else if (msg.what == 0x000){
                Toast.makeText(MainActivity.this, "网络链接失败，请重试", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
                finish();
            }
        }
    };

}
