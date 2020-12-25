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

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public void onClick(View v) {
        String inputText = editText.getText().toString();
        String BVID;
        if(inputText!=null){
            BVID=inputText;
        }else {
            BVID = "BV1rV41187HG";
        }
        String url = "http://api.bilibili.com/x/web-interface/view?bvid=" + BVID;
        switch (v.getId()) {
            case R.id.search:
                sendRequestWithHttpURLConnection(url);
        }
    }


    private void sendRequestWithOkHttp(String requestUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(requestUrl)
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    showData();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void sendRequestWithHttpURLConnection(String requestUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject jsonObject = null;
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(requestUrl);

                    connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);

                    InputStream inputStream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    jsonObject = JSONObject.fromObject(response.toString());
                    jsonStrToJava(jsonObject);
                    showData();

                } catch (IOException e) {
                    Log.d(TAG, "run: openConnection()");
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
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
                        + "作者：\b\t" + ownerData.getName() + "\n"
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