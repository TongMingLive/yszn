package com.example.flytoyou.srmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Bean.User;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.apache.commons.logging.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by flytoyou on 2016/12/7.
 */

public class Login extends AppCompatActivity implements OnClickListener {

    private Button login,sign,youke;

    private EditText name,pwd;

    private String qqname,qqpwd;

    private SharedPreferences spf = null;

    private Editor editor = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        spf = this.getSharedPreferences("user",MODE_PRIVATE);
        editor = spf.edit();

        FindView();

        //注册点击事件
        login.setOnClickListener(this);
        sign.setOnClickListener(this);
        youke.setOnClickListener(this);
    }

    //FindView
    public void FindView(){
        login = (Button) findViewById(R.id.btn_login);
        sign = (Button) findViewById(R.id.btn_sign);
        youke = (Button) findViewById(R.id.btn_youke);
        name = (EditText) findViewById(R.id.name);
        pwd = (EditText) findViewById(R.id.psw);
    }

    //登陆按钮
    public void login(){
        //获得用户输入的内容
        qqname = name.getText().toString();
        qqpwd = pwd.getText().toString();
        if (name.length()==0||pwd.length()==0){
            Toast.makeText(Login.this, "账号或密码不能未空", Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Map<String,Object> map = new HashMap<String, Object>();
                    map.put("userName",qqname);
                    map.put("password",qqpwd);
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
                                //将用户数据放入轻量级数据
                                editor.putString("username",jsonObject.getString("userName"));
                                editor.putString("password",jsonObject.getString("userPassword"));
                                editor.commit();
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
    }

    //点击事件
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_sign:
                startActivity(new Intent(Login.this, Sign.class));
                break;
            case R.id.btn_youke:
                User user = new User();
                user.setUserType(-1);
                App.user = user;
                startActivity(new Intent(Login.this, Index.class));
                finish();
                break;
        }

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Toast.makeText(Login.this, "登陆成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, Index.class));
                finish();
            }else if (msg.what == 0x124){
                Toast.makeText(Login.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
            }else if (msg.what == 0x000){
                Toast.makeText(Login.this, "网络链接失败，请重试", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
