package com.gingerwolfmc.gingerwolfmc;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    private TextView textView;
    private ImageButton imgbtn;
    //Set Latest News Delay
    private Integer news = 50000;
    private Handler handler = new Handler();
    public static void isNetworkAvailable(final Handler handler, final int timeout) {
        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)
        new Thread() {
            private boolean responded = false;
            @Override
            public void run() {
                // set 'responded' to TRUE if is able to connect with google mobile (responds fast)
                new Thread() {
                    @Override
                    public void run() {
                        HttpGet requestForTest = new HttpGet("http://m.google.com");
                        try {
                            new DefaultHttpClient().execute(requestForTest); // can last...
                            responded = true;
                        }
                        catch (Exception e) {
                        }
                    }
                }.start();

                try {
                    int waited = 0;
                    while(!responded && (waited < timeout)) {
                        sleep(100);
                        if(!responded ) {
                            waited += 100;
                        }
                    }
                }
                catch(InterruptedException e) {} // do nothing
                finally {
                    if (!responded) { handler.sendEmptyMessage(0); }
                    else { handler.sendEmptyMessage(1); }
                }
            }
        }.start();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text1);
        addButtonListener();
        isNetworkAvailable(h, 5000);
    }
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != 1) { // code if not connected
                textView.setText("No Network Access!!, Please check Connection!!");
                handler.postDelayed(runnable, news);
            } else { // code if connected
                DownloadWebPageTask task = new DownloadWebPageTask();
                task.execute(new String[]{"http://www.gingerwolfmc.com/a/a.html"});
                handler.postDelayed(runnable, news);
            }
        }
    };
    private Runnable runnable = new Runnable(){
        @Override
        public void run() {
            isNetworkAvailable(h,5000);
            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[]{"http://www.gingerwolfmc.com/a/a.html"});
            handler.postDelayed(this, news);
        }
    };
        private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... urls) {
                String response = "";
                for (String url : urls) {
                    DefaultHttpClient client = new DefaultHttpClient();
                    HttpGet httpGet = new HttpGet(url);
                    try {
                        HttpResponse execute = client.execute(httpGet);
                        InputStream content = execute.getEntity().getContent();

                        BufferedReader buffer = new BufferedReader(
                                new InputStreamReader(content));
                        String s = "";
                        while ((s = buffer.readLine()) != null) {
                            response += s;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return response;
            }
            @Override
            protected void onPostExecute(String result) {
                textView.setText(Html.fromHtml(result));
            }
        }
    public void addButtonListener(){
        imgbtn = (ImageButton) findViewById(R.id.inforbtn);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Toast.makeText(MainActivity.this,"Information is Working!", Toast.LENGTH_SHORT).show();
                Intent info = new Intent("com.gingerwolfmc.infoActivity");
                startActivity(info);
            }
        });
        imgbtn = (ImageButton) findViewById(R.id.combtn);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Commands is Working!", Toast.LENGTH_SHORT).show();
                Intent set = new Intent("com.gingerwolfmc.comActivity");
                startActivity(set);
            }
        });
        imgbtn = (ImageButton) findViewById(R.id.setbtn);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"Settings is Working!", Toast.LENGTH_SHORT).show();
            }
        });
     }
    }

