package cn.tanghz17.bilivideodata;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private EditText editText;
    private Button button;
    private TextView textView;
    private ImageView imageView;
    public static final FormatVideoData VideoData = new FormatVideoData();
    public static final FormatVideoData.owner ownerData = new FormatVideoData.owner();
    public static final FormatVideoData.stat statData = new FormatVideoData.stat();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button) findViewById(R.id.search);
        editText = (EditText) findViewById(R.id.input_editText);
        textView = (TextView) findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.image_video);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)  {
        String inputText = editText.getText().toString();
        String BVID = null;

        switch (v.getId()) {
            case R.id.search:
                String BV="BV";
                if ( null == inputText||"".equals(inputText)){
                    Log.d(TAG, "inputText: 输入框是空的");
                    Toast.makeText(MainActivity.this,"输入框是空的",Toast.LENGTH_SHORT).show();

                }else if ((inputText.substring(0,2)).equals("BV") &&
                         inputText.length()==12){
                    BVID=inputText;
                    String url = "http://api.bilibili.com/x/web-interface/view?bvid=" + BVID;
                    sendRequestWithOkHttp(url);
                }else{
                    Log.d(TAG, String.valueOf(inputText.length())+"   "+inputText.charAt(0)+inputText.charAt(1));
                    Log.d(TAG, "inputText: BV号是以'BV'开头的");
                    Toast.makeText(MainActivity.this, "BV号是以'BV'开头的",Toast.LENGTH_SHORT).show();
                }
        }
    }


    private void sendRequestWithOkHttp(String requestUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = null;

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(requestUrl)
                            .build();
                    Log.d(TAG, "run: new Request.Builder");
                    Response response = client.newCall(request).execute();
                    Log.d(TAG, "run: response from request");
                    String responseData = response.body().string();

                    jsonObject = JSONObject.fromObject(responseData);
                    jsonStrToJava(jsonObject);
                    showData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showData() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageURI(VideoData.getPic());
                textView.setText("标题：\t " + VideoData.getTitle() + "\n"
                        + "BV号：\t " + VideoData.getBvid() + "\n"
                        + "AV号：\t " + VideoData.getAvid() + "\n"
                        //+ "简介：\t " + VideoData.getDesc() + "\n"
                        + "UP主：\t " + ownerData.getName() + "\n\n"
                        + "播放量：\t" + statData.getView() + "\n"
                        + "弹幕数：\t" + statData.getDanmaku() + "\n"
                        + "评论数：\t" + statData.getReply() + "\n"
                        + "点赞数：\t" + statData.getLike() + "\n"
                        + "银币数：\t" + statData.getCoin() + "\n"
                        + "收藏数：\t" + statData.getFavorite() + "\n"
                        + "分享数：\t" + statData.getShare());
                Log.d(TAG, "showData()| run: ");
            }
        });
    }

    public static void jsonStrToJava(JSONObject jsonObject) {
        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
        Log.d(TAG, jsonObjectData + "");
        VideoData.setBvid(jsonObjectData.getString("bvid"));
        VideoData.setAvid(jsonObjectData.getInt("aid"));
        VideoData.setVideos(jsonObjectData.getInt("videos"));
        VideoData.setPic(Uri.parse(jsonObjectData.getString("pic")));
        VideoData.setTitle(jsonObjectData.getString("title"));
        VideoData.setDesc(jsonObjectData.getString("desc"));

        JSONObject jsonObjectOwner = jsonObjectData.getJSONObject("owner");
        Log.d(TAG, "Owner:" + jsonObjectOwner);
        ownerData.setMid(jsonObjectOwner.getInt("mid"));
        ownerData.setName(jsonObjectOwner.getString("name"));
        ownerData.setFace(jsonObjectOwner.getString("face"));

        JSONObject jsonObjectStat = jsonObjectData.getJSONObject("stat");
        Log.d(TAG, "Stat: " + jsonObjectStat);
        statData.setView(jsonObjectStat.getInt("view"));
        statData.setDanmaku(jsonObjectStat.getInt("danmaku"));
        statData.setReply(jsonObjectStat.getInt("reply"));
        statData.setLike(jsonObjectStat.getInt("like"));
        statData.setCoin(jsonObjectStat.getInt("coin"));
        statData.setFavorite(jsonObjectStat.getInt("favorite"));
        statData.setShare(jsonObjectStat.getInt("share"));
    }
}