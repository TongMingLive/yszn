package com.example.flytoyou.srmanager.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.flytoyou.srmanager.Index;
import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.ResPost;
import com.example.flytoyou.srmanager.Talk_All;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by flytoyou on 2016/12/6.
 */


public class Fragment1 extends Fragment {

    private ListView listView;

    private List<JSONObject> list;

    private List<Map<String,Object>> BaseList = new ArrayList();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<JSONObject>();

        //开启线程接收公告信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<String, Object>();
                String str = HttpUtil.doPost(HttpUtil.path+"selectAllPage",map);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i=0;i<jsonArray.length();i++){
                            list.add((JSONObject) jsonArray.get(i));
                    }
                    handler.sendEmptyMessage(0x123);
                } catch (JSONException e) {
                    handler.sendEmptyMessage(0x000);
                }
            }
        }.start();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.weixin,null);

        listView = (ListView) view.findViewById(R.id.wx_lv);

        setAdapter();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //将适配器放入listview
    public void setAdapter(){

        listView.setAdapter(new BaseAdapter() {
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
                View v = layoutInflater.inflate(R.layout.myadapte,null);

                ImageView user = (ImageView) v.findViewById(R.id.img_uesr);
                TextView name = (TextView) v.findViewById(R.id.tx_name);
                TextView page = (TextView) v.findViewById(R.id.text_page);
                TextView time = (TextView) v.findViewById(R.id.texv_time);

                user.setImageResource((Integer)BaseList.get(position).get("user"));
                name.setText(BaseList.get(position).get("name")+"");
                page.setText(BaseList.get(position).get("page")+"");
                time.setText(BaseList.get(position).get("time")+"");

                return v;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前item的map
                Map<String,Object> map  = BaseList.get(position);
                //创建意图将信息传入下一个页面
                Intent intent =new Intent();
                intent.putExtra("title",map.get("name")+"");
                intent.putExtra("txt",map.get("page")+"");
                intent.putExtra("time",map.get("time")+"");
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
                        map.put("user",R.mipmap.post_a);
                        map.put("name",list.get(i).getString("pageTitle"));
                        map.put("page",list.get(i).getString("pageTxt"));
                        map.put("time",list.get(i).getString("pageTime").substring(0,10));
                        BaseList.add(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setAdapter();
            }else if (msg.what == 0x000){

            }
        }
    };

}
