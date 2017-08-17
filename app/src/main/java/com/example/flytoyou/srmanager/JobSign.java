package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flytoyou on 2017/3/6.
 */

public class JobSign extends AppCompatActivity implements OnClickListener {

    private GridView gv;

    private Button tg;

    private List<JSONObject> list;

    private List<Map<String,Object>> BaseList = new ArrayList();

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
        setContentView(R.layout.jobsign);

        list = new ArrayList<JSONObject>();

        FindView();

        //开启线程接收职业信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<String, Object>();
                String str = HttpUtil.doPost(HttpUtil.path+"SelectJobServlet",map);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i=0;i<jsonArray.length();i++){
                        list.add((JSONObject) jsonArray.get(i));
                    }
                    handler.sendEmptyMessage(0x123);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        //注册点击事件
        tg.setOnClickListener(this);

    }

    //设置适配器
    public void setAdapter(){
        gv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return BaseList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater layoutInflater = LayoutInflater.from(JobSign.this);
                View v = layoutInflater.inflate(R.layout.job_myadapter,null);

                TextView job = (TextView) v.findViewById(R.id.job_tex);
                job.setText(BaseList.get(position).get("jobname")+"");

                return v;
            }
        });

        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前item的map
                Map<String,Object> map  = BaseList.get(position);

                setJob((map.get("username")+""),(Integer) map.get("jobid"),map.get("jobname")+"");

                //跳转
                startActivity(new Intent(JobSign.this, Login.class));
                finish();
            }
        });
    }

    //插入职业
    public void setJob(final String username, final int jobid,final String jobname){
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("userName",username);
                map.put("jobId",jobid);
                String str = HttpUtil.doPost(HttpUtil.path+"InsertJobServlet",map);
                if (str.equals("error")){
                    handler.sendEmptyMessage(0x000);
                }else if (str.equals("true")){
                    App.user.setJobName(jobname);
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    //FindView
    public void FindView(){
        gv = (GridView) findViewById(R.id.job_tab);
        tg = (Button) findViewById(R.id.jog_tg);
    }

    //点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.jog_tg:
                startActivity(new Intent(JobSign.this, Login.class));
                finish();
                break;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                try {
                    for (int i=0;i<list.size();i++){
                        Map<String,Object> map = new HashMap<String,Object>();
                        map.put("username", App.user.getUserName());
                        map.put("jobid",list.get(i).getInt("jobId"));
                        map.put("jobname",list.get(i).getString("jobName"));
                        BaseList.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setAdapter();
            }else if (msg.what == 0x124){
                startActivity(new Intent(JobSign.this, Login.class));
            }else if (msg.what == 0x000){
                showToast(JobSign.this, "网络链接失败，请重试", 1000);
            }
        }
    };

}
