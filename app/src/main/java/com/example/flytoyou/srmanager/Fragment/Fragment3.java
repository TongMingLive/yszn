package com.example.flytoyou.srmanager.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Login;
import com.example.flytoyou.srmanager.MainActivity;
import com.example.flytoyou.srmanager.R;
import com.example.flytoyou.srmanager.SelectLogin;
import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by flytoyou on 2016/12/6.
 */

public class Fragment3 extends Fragment {

    private LinearLayout ysq,ly,fk;

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
        final View view = inflater.inflate(R.layout.faxian,null);

        ysq = (LinearLayout) view.findViewById(R.id.faxian_ysq);
        ly = (LinearLayout) view.findViewById(R.id.faxian_ly);
        fk = (LinearLayout) view.findViewById(R.id.faxian_fk);

        ysq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        fk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast(getActivity(), "该功能正在开发中！", 300);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
