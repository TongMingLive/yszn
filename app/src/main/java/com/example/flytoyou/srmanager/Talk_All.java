package com.example.flytoyou.srmanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by flytoyou on 2016/12/21.
 */

public class Talk_All extends AppCompatActivity {

    private TextView t_name,t_gonggao,t_time;
    private Button t_fh;

    private String name,page,time;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_all);

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        t_name = (TextView) findViewById(R.id.top_name);
        t_gonggao = (TextView) findViewById(R.id.talk_tex_gonggao);
        t_time = (TextView) findViewById(R.id.talk_texv_time);
        t_fh = (Button) findViewById(R.id.t_fh);

        Intent mesg = getIntent();
        name = mesg.getStringExtra("title");
        page = mesg.getStringExtra("txt");
        time = mesg.getStringExtra("time");

        t_name.setText(name);
        t_gonggao.setText(page);
        t_time.setText(time);

        t_fh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Talk_All.this,Index.class));
                finish();
            }
        });

    }
}
