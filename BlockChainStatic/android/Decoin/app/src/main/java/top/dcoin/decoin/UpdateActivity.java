package top.dcoin.decoin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateActivity extends Activity {
    //public static final String UPDATE_FILE;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    private TextView tvInfo;
    private TextView tvTotal;
    private TextView tvProgress;
    private int count = 0;
    private int fileLength;
    private int progress = 0;
    private int downLenght = 8 * 1024;
    private double unit = 1024 * 1024;
    //private int
    private Exception downloadex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        File dir = getExternalFilesDir("");
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvProgress = (TextView) findViewById(R.id.tvProgress);
        final String url = getIntent().getStringExtra("url");
        if (url == null || url.trim().equals("")) {
            tvInfo.setText("下载信息不存在");
            return;
        }
        if (url.indexOf('/') == -1 || !url.endsWith(".apk")) {
            tvInfo.setText("下载地址错误");
            return;
        }
        String filename = url.substring(url.lastIndexOf("/") + 1);
        final File savefile = new File(dir, filename);
        final Handler myhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //count++;
                switch (msg.what) {
                    case 1: {
                        tvInfo.setText("下载更新文件完成" + savefile);
                        Intent installIntent = new Intent(Intent.ACTION_VIEW);
                        installIntent.setDataAndType(Uri.parse("file://" + savefile.getAbsolutePath()),
                                "application/vnd.android.package-archive");
                        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(installIntent);
                        finish();
                        break;
                    }
                    case 2: {
                        tvInfo.setText("下载更新文件失败" + downloadex);
                        break;
                    }
                    case 3: {
                        //new Date()) +
                        tvInfo.setText("下载更新中,请稍候，不要退出程序。。。");
                        tvTotal.setText("文件总大小:" + new BigDecimal((fileLength / unit) + "").setScale(2, BigDecimal.ROUND_HALF_EVEN) + "MB");
                        tvProgress.setText("已经下载:" + new BigDecimal((progress / unit) + "").setScale(2, BigDecimal.ROUND_HALF_EVEN) + "MB");
//                        tvTotal.setText("总大小:" + (fileLength / unit) + "MB");
//                        tvProgress.setText("已经下载:" + (progress / unit) + "MB");
                        break;
                    }
                    default: {
                        tvInfo.setText("下载异常" + msg.what);
                    }
                }
            }
        };
        //保存文件
        new Thread() {
            @Override
            public void run() {
                myhandler.sendEmptyMessage(3);
                try {
                    count = 0;
                    HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                    connection.setRequestMethod("GET");
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.setDoInput(true);
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        throw new Exception("获取更新文件失败!");
                    }

                    fileLength = connection.getContentLength();
                    InputStream is = connection.getInputStream();
                    OutputStream os = new FileOutputStream(savefile);
                    byte[] bytes = new byte[downLenght];
                    int len = is.read(bytes);
                    while (len > 0) {
                        progress += len;
                        if (count % 100 == 0) {
                            myhandler.sendEmptyMessage(3);
                        }
                        os.write(bytes, 0, len);
                        os.flush();
                        len = is.read(bytes);
                        count++;
                    }
                    os.close();
                    is.close();
                    myhandler.sendEmptyMessage(1);

                } catch (Exception e) {
                    e.printStackTrace();
                    downloadex = e;
                    myhandler.sendEmptyMessage(2);
                    return;
                }
            }
        }.start();

        tvInfo.setText(filename);
    }


}
