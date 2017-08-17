package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
 * Created by flytoyou on 2017/3/1.
 */

public class SelectLogin extends AppCompatActivity {

    private ListView listView;

    //private List<JSONObject> list;

    private ViewPager viewPager;

    private List<View> ViewList = new ArrayList<>();
    private List<Map<String,Object>> BaseList = new ArrayList();

    private myAdapter adapter = new myAdapter();

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
        setContentView(R.layout.selectlogin);

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        listView = (ListView) findViewById(R.id.lg_lv);

       // list = new ArrayList<JSONObject>();

        //开启线程接收维修信息
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<String, Object>();
                map.put("userid", App.user.getUserId());
                String str = HttpUtil.doPost(HttpUtil.path+"SelectUserResServlet",map);
                Log.e("json:",str);
                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i=0;i<jsonArray.length();i++){
                       // list.add(jsonArray.getJSONObject(i));

                        Map<String,Object> map2 = new HashMap<String,Object>();
                        map2.put("user",R.mipmap.shuihu);
                        map2.put("resid",jsonArray.getJSONObject(i).getInt("resid"));
                        map2.put("name",jsonArray.getJSONObject(i).getString("resName"));
                        map2.put("page",jsonArray.getJSONObject(i).getString("login"));
                        map2.put("time",jsonArray.getJSONObject(i).getString("time"));
                        BaseList.add(map2);

                    }


                    handler.sendEmptyMessage(0x123);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        Log.e("ListMessage:", "listSize:"+BaseList.size());

        //将适配器放入listview
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取当前item的map
                Map<String,Object> map  = BaseList.get(position);
                //创建意图将信息传入下一个页面
                Intent intent =new Intent();
                intent.putExtra("resname",map.get("name")+"");
                intent.putExtra("login",map.get("login")+"");
                intent.putExtra("time",map.get("time")+"");
                intent.putExtra("resid",map.get("resid")+"");
                //跳转
                intent.setClass(SelectLogin.this,LoginPage.class);
                startActivity(intent);
            }
        });

    }

    private class myAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return BaseList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(SelectLogin.this);
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
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        }
    };

}
