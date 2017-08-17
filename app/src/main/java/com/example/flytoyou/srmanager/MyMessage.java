package com.example.flytoyou.srmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.flytoyou.srmanager.Util.App;
import com.example.flytoyou.srmanager.Util.HttpUtil;
import com.example.flytoyou.srmanager.Util.QiniuUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by flytoyou on 2017/3/1.
 */

public class MyMessage extends AppCompatActivity implements OnClickListener {

    private EditText edtname, edtroom, edtadress, edtage;
    private ImageView tx;
    private RadioButton sex1, sex2;
    private Button yes, jobbtn;
    private String name, room, updateStr, nameStr, deleteStr, adress, sex, getjob;
    private int age;
    private List<JSONObject> list;
    private String[] strings;
    private Button submitbtn;
    private ImageView picbtn;
    private EditText content, money;
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String imgurl = null;//图片的路径
    private File tempFile;
    private String token = null;
    private String pageImg = null;
    private byte[] bytes;

    /* 头像名称 */
    private String PHOTO_FILE_NAME = "temp_photo.jpg";
    private boolean persionFlag = false;
    private String picpath;
    private String htmlUrl = App.user.getUserImg();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage);

        //设置虚拟按键颜色
        //getWindow().setNavigationBarColor(Color.parseColor("#3F51B5"));

        FindView();

        list = new ArrayList<JSONObject>();

        String[] strings = new String[]{};

        GetJob();

        //将原本的信息输入输入框
        edtage.setHint(App.user.getUserAge() + "");
        if (App.user.getUserAddress().length() == 0) {

        } else {
            edtadress.setHint(App.user.getUserAddress() + "");
        }

        //默认选中用户目前的性别
        if (App.user.getUserSex().equals("男")) {
            sex1.setChecked(true);
        } else if ((App.user.getUserSex().equals("女"))) {
            sex2.setChecked(true);
        }

        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<String, Object>();
                token = HttpUtil.doPost(HttpUtil.path + "GetTokenServlet", map);
            }
        }.start();

    }

    //FindView
    public void FindView() {
        edtadress = (EditText) findViewById(R.id.edt_adress);
        sex1 = (RadioButton) findViewById(R.id.sex1);
        sex2 = (RadioButton) findViewById(R.id.sex2);
        yes = (Button) findViewById(R.id.btn_mymessage);
        jobbtn = (Button) findViewById(R.id.btn_job);
        edtage = (EditText) findViewById(R.id.edt_age);
        tx = (ImageView) findViewById(R.id.my_tx_iv);

        tx.setOnClickListener(this);
        yes.setOnClickListener(this);
    }

    //获取职业
    public void GetJob() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<String, Object>();
                getjob = HttpUtil.doPost(HttpUtil.path + "SelectJobServlet", map);
                Log.e("job",getjob);
                try {
                    JSONArray jsonArray = new JSONArray(getjob);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        list.add((JSONObject) jsonArray.get(i));
                    }
                    for (int i = 0; i < list.size(); i++) {
                        //strings[i] = list.get(i).getString("jobName");
                    }
                } catch (JSONException e) {
                    handler.sendEmptyMessage(0x000);
                }

            }
        }.start();
    }

    //判断年龄
    public void CheckAge() {
        if (edtage.getText().toString().length() == 0) {
            age = App.user.getUserAge();
        } else {
            age = Integer.parseInt(edtage.getText().toString());
        }
    }

    //判断性别
    public void CheckSex() {
        if (sex1.isChecked()) {
            sex = "男";
        } else if (sex2.isChecked()) {
            sex = "女";
        }
    }

    //判断地址
    public void CheckAdress() {
        if (edtadress.getText().toString().length() == 0) {
            adress = App.user.getUserAddress();
        } else {
            adress = edtadress.getText().toString();
        }
    }

    //提交事件
    public void TiJiao() {
        CheckSex();
        CheckAge();
        CheckAdress();
        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("userId", App.user.getUserId());
                map.put("userSex", sex);
                map.put("userAge", age);
                map.put("jobId", 1);
                map.put("userAddress", adress);
                map.put("userImg", htmlUrl);
                Log.e("htmlUrl",htmlUrl);
                deleteStr = HttpUtil.doPost(HttpUtil.path + "DeleteUserImgServlet", map);
                Log.e("delete",deleteStr);
                updateStr = HttpUtil.doPost(HttpUtil.path + "UpdateUserServlet", map);
                if (updateStr.equals("true")) {
                    handler.sendEmptyMessage(0x123);
                } else {
                    handler.sendEmptyMessage(0x124);
                }
            }
        }.start();
    }

    //提交图片
    public boolean ImgCheck() {
        if (imgurl == null) {
            return true;
        } else {
            File file = new File(imgurl);
            return true;
        }
    }

    //剪切图片
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //判断sdcard是否被挂载
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    //从相册获取
    private void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    //从相机获取
    private void camera() {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    PHOTO_FILE_NAME);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mymessage:
                ImgCheck();
                TiJiao();
                break;
            case R.id.my_tx_iv:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setItems(new String[]{"相册", "拍照"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                gallery();
                                break;
                            case 1:
                                camera();
                                break;
                        }
                    }
                });
                builder.show();
                break;
            case R.id.btn_job:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:

                                break;
                            case 1:

                                break;
                        }
                    }
                });
                builder1.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                imgurl = getImagePath(uri, null);

                /*//好像是Android多媒体数据库的封装接口，具体的看Android文档

                Cursor cursor = getActivity().managedQuery(uri, proj, null, null, null);

                //按我个人理解 这个是获得用户选择的图片的索引值

                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                //将光标移至开头 ，这个很重要，不小心很容易引起越界
                cursor.moveToFirst();
                //最后根据索引值获取图片路径
                imgurl = cursor.getString(column_index);*/
                Log.e("TAG", imgurl);
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(MyMessage.this, "未找到存储卡", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                tx.setImageBitmap(bitmap);

                //生成需要存至七牛的文件名 当前时间加_user(精确到毫秒)
                //获取文件名   /   后缀类型
                String fileType = imgurl.substring(imgurl.lastIndexOf("."));

                //将bitmap对象转化为七牛云byte数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if ("png".equals(fileType) || "Png".equals(fileType)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                } else {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                }
                bytes = baos.toByteArray();

                //获取当前系统时间
                Date date = new Date();
                DateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
                String time = format.format(date);
                //返回给七牛的文件名称
                pageImg = "user_" + time + fileType;
                Log.e("pageImg", pageImg);
                //返回给服务器的网络图片地址
                htmlUrl = "http://qiniu.marvel.ac.cn/" + pageImg;
                Log.e("htmlUrl", htmlUrl);

                if (imgurl != null) {
                    QiniuUtil qiniuUtil = new QiniuUtil();
                    boolean up = qiniuUtil.qiniuUpload(token, pageImg, bytes);
                    if (up) {
                        Toast.makeText(this, "图片上传成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getImagePath(Uri uri, String seletion) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, seletion, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();

        }
        return path;

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(MyMessage.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Toast.makeText(MyMessage.this, "修改失败,请重试", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 0x000) {
                Toast.makeText(MyMessage.this, "网络链接失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }
    };

}
