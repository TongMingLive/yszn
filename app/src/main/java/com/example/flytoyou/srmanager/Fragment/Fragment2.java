package com.example.flytoyou.srmanager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flytoyou.srmanager.LoginPage;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.ResPost;
import com.example.flytoyou.srmanager.SelectLogin;
import com.example.flytoyou.srmanager.Talk_All;
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
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment2 extends Fragment {

    private GridView gridView;

    private TextView youkepage;

    private List<JSONObject> list;

    private List<Map<String,Object>> BaseList = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<JSONObject>();

        //判断是否是游客，游客不接收个人推荐
        if (App.user.getUserType() == -1){

        }else if (App.user.getJobId() == 1){

        } else {
            //开启线程接收公告信息
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    Map<String,Object> map =new HashMap<String, Object>();
                    map.put("jobId",App.user.getJobId());
                    String str = HttpUtil.doPost(HttpUtil.path+"SelectPageIdPageServlet",map);
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
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.gexingtuijian,null);

        gridView = (GridView) view.findViewById(R.id.gx_lv);

        youkepage = (TextView) view.findViewById(R.id.tex_youkepage);

        if (App.user.getUserType() == -1){
            gridView.setVisibility(View.GONE);
        }else if (App.user.getJobId() == 1){
            gridView.setVisibility(View.GONE);
            youkepage.setText("填写职业后可获得个性推荐");
        }

        setAdapter();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //设置适配器
    public void setAdapter(){

        gridView.setAdapter(new BaseAdapter() {
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
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View v = layoutInflater.inflate(R.layout.myadapter2,null);

                ImageView img = (ImageView) v.findViewById(R.id.mdp2_iv);
                TextView tex = (TextView) v.findViewById(R.id.mdp2_tv);

                img.setBackgroundResource((Integer) BaseList.get(position).get("user"));
                tex.setText(BaseList.get(position).get("pagetitle")+"");

                return v;
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前item的map
                Map<String,Object> map  = BaseList.get(position);
                //创建意图将信息传入下一个页面
                Intent intent =new Intent();
                intent.putExtra("title",map.get("pagetitle")+"");
                intent.putExtra("txt",map.get("pagetxt")+"");
                intent.putExtra("time",map.get("pagetime")+"");
                //跳转
                intent.setClass(getActivity(),Talk_All.class);
                startActivity(intent);
            }
        });

    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                try {
                    for (int i=0;i<list.size();i++){
                        Map<String,Object> map = new HashMap<String,Object>();
                        map.put("user",R.mipmap.sony);
                        map.put("pagetitle",list.get(i).getString("pageTitle"));
                        map.put("pagetxt",list.get(i).getString("pageTxt"));
                        map.put("pagetime",list.get(i).getString("pageTime").substring(0,10));
                        BaseList.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setAdapter();
            }
        }
    };

}
