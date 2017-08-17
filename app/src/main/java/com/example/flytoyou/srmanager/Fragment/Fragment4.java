package com.example.flytoyou.srmanager.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.MainActivity;
import com.example.flytoyou.srmanager.MyMessage;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.Util.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment4 extends Fragment {

    private TextView roomnum,name,id,out;
    private ImageView tx;
    private LinearLayout mymessage,outlogin,shoucang,qianbao,kabao;
    private SharedPreferences spf;
    private Editor editor;
    private RelativeLayout my;
    private Bitmap bitmap;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.wo,null);

        spf = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);

        shoucang = (LinearLayout) view.findViewById(R.id.shoucang);
        qianbao = (LinearLayout) view.findViewById(R.id.qianbao);
        kabao = (LinearLayout) view.findViewById(R.id.kabao);
        roomnum = (TextView) view.findViewById(R.id.tex_roomnum);
        name = (TextView) view.findViewById(R.id.w_name);
        id = (TextView) view.findViewById(R.id.w_id);
        out = (TextView) view.findViewById(R.id.out_tex);
        tx = (ImageView) view.findViewById(R.id.w_img);
        my = (RelativeLayout) view.findViewById(R.id.w_message);
        mymessage = (LinearLayout) view.findViewById(R.id.mymessage);
        outlogin = (LinearLayout) view.findViewById(R.id.outlogin);

        if (App.user.getUserType()==-1){
            name.setText("请登陆");
            id.setText("登陆后享受更多特权");
            out.setText("立即登陆");
        }else {
            roomnum.setText(App.user.getUserAddress());
            name.setText(App.user.getUserName());
            id.setText("ID:"+App.user.getUserId()+"");
            getdata(App.user.getUserImg());
        }

        //头像按钮
        tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.user.getUserType()==-1){
                    startActivity(new Intent(getActivity(), Login.class));
                    getActivity().finish();
                }else {
                    startActivity(new Intent(getActivity(), MyMessage.class));
                }
            }
        });

        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        qianbao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        kabao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        //修改个人信息按钮
        mymessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.user.getUserType()==-1){
                    showToast(getActivity(), "登陆后可更改个人信息", 300);
                }else {
                    startActivity(new Intent(getActivity(), MyMessage.class));
                }
            }
        });

        //退出按钮
        outlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (App.user.getUserType()==-1){
                    startActivity(new Intent(getActivity(), Login.class));
                }else {
                    editor = spf.edit();
                    editor.clear().commit();
                    App.user = null;
                    showToast(getActivity(), "退出成功", 300);
                    startActivity(new Intent(getActivity(), Login.class));
                    getActivity().finish();
                }
            }
        });

        return view;
    }

    //获取网络数据
    private void getdata(final String urlPath){
        //开启子线程
        new Thread(){
            public void run(){
                //url地址
                //String urlPath = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
                try {
                    //获取url对象
                    URL url = new URL(urlPath);
                    //获取HttpURLConnection连接对象
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //设置连接超时事件 毫秒
                    connection.setConnectTimeout(5000);
                    //设置请求方式 常见方式GET,POST,HEAD
                    connection.setRequestMethod("GET");
                    //获取响应码，如果是200，请求成功。
                    if (connection.getResponseCode()==200){
                        //请求成功
                        //获取输入流
                        InputStream inputStream = connection.getInputStream();
                        //将输入流转化为bitmap对象
                        bitmap = BitmapFactory.decodeStream(inputStream);
                        //发送一个空消息，标识为0x123
                        handler.sendEmptyMessage(0x123);
                        //定义一个byte字节数组，用于接受每次读取的内容
                        //每次读取1024个byte
                        /*byte [] b = new byte[1024];
                        //用于每次读取的长度
                        int len;
                        //循环读取输入流内容
                        while ((len = inputStream.read(b))!=-1){
                            //将获取的字节流转换为字符流
                            String str = new String(b,0,len);
                            Log.e("TAG",str);
                        }
                        inputStream.close();//关闭输入流
                        connection.disconnect();//断开连接*/

                    }else {
                        Log.e("TAG","请求失败，请检查网络");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            if (msg.what == 0x123){
                tx.setImageBitmap(bitmap);
            }
        };
    };

}
