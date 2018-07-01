package com.example.okhttpplus;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import com.example.okhttpplus.model.User;
import com.example.okhttpplus.parser.JokeParser;
import com.example.okhttpplus.util.TestUrls;
import com.socks.okhttp.plus.OkHttpProxy;
import com.socks.okhttp.plus.callback.OkCallback;
import com.socks.okhttp.plus.listener.DownloadListener;
import com.socks.okhttp.plus.listener.UploadListener;
import com.socks.okhttp.plus.model.Progress;
import com.socks.okhttp.plus.parser.OkJsonParser;
import com.socks.okhttp.plus.parser.OkTextParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/********* OkHttpPlus 的使用,此时不能上传自定义的Request **************/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /***************** Get ***************/
    /**
     * 异步方式
     */
//    OkHttpPlus内部支持5种解析器，这可以解决你大部分的需求
//
//    OkBaseParser，所有解析器的基类，不可直接使用
//    OkBaseJsonParser，所有JSON解析器的基类，你可以继承它来定义自己的JSON解析
//    OkJsonParser，JSON解析器，支持JSONObject和JSONArray的解析，默认使用GSON作为解析器
//    OkTextParser，String解析器，支持将结果以String方式输出
//    OkFileParser，文件解析器，支持将结果保存为文件，你可以用来下载文件，但是不支持较大文件下载

    public void getExqueueRequest(){
        //获取json转对象
        OkHttpProxy.get()
                .url(TestUrls.URL_USER)
                .tag(this)
                .enqueue(new OkCallback<User>(new JokeParser<User>()) {
                    @Override
                    public void onSuccess(int code, User user) {
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });

        //获取json转 链表
        Call enqueue = OkHttpProxy.get()
                .url(TestUrls.URL_USER)
                .tag(this)
                .enqueue(new OkCallback<List<User>>(new OkJsonParser<List<User>>() {
                }) {
                    @Override
                    public void onSuccess(int code, List<User> users) {
//                        tv_response.setText(users.toString());
                    }

                    @Override
                    public void onFailure(Throwable e) {
//                        tv_response.setText(e.getMessage());
                    }
                });

//        enqueue.cancel();
        //获取String类型
        OkHttpProxy.get()
                .url(TestUrls.URL_USER)
                .tag(this)
                .enqueue(new OkCallback<String>(new OkTextParser()) {
                    @Override
                    public void onSuccess(int code, String s) {

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                });


    }

    /**
     * 同步方式
     */
    public void getExcuteRequest() throws IOException {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Response response = OkHttpProxy.get()
                            .url(TestUrls.URL_USER)
                            .execute();

                    //1.转化为对象
                    User user = new OkJsonParser<User>(){
                    }.parse(response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //操作UI控件
                        }
                    });
                    //2.得到String
                    response.body().string();
                    //3.得到二进制数组
                    response.body().bytes();
                    //4.得到流 (可以进行大文件的传输，但是此时要考虑到当前在子线程中，无法操作UI控件，使用handle)
                    response.body().byteStream();
                    //5.得到字节流
                    response.body().charStream();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        //添加头部，添加参数(这是联网操作要放在子线程中)
        Response response = OkHttpProxy.get()
                .tag(this)
                .url(TestUrls.URL_USER)
                .addParams("name","wusong")
                .execute();

    }

    /*********** Post (和Get的使用基本一致) ***********/
    /**
     * post 和get 使用基本一致，不同的是 post 可以添加 addHead Get没有body
     * @throws IOException
     */
    public void postRequest() throws IOException {
        OkHttpProxy.post()
                .url(TestUrls.URL_USER)
                .addHeader("head","okhttp")
                .addParams("name","wusong")
                .execute();


    }

    /************** 文件下载  *******************/
    /**
     * 此时下载的全在内存中，因此不要下载大文件，不然很容易OOM
     */
    public void downFile(){
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        OkHttpProxy.download(TestUrls.URL_USER, new DownloadListener(filePath, "mm.jar") {
            @Override
            public void onSuccess(File file) {
            }

            @Override
            public void onFailure(Exception e) {
            }

            @Override
            public void onUIProgress(Progress progress) {
                //progress.getTotalBytes() == -1 代表当前文件大小不可知，无法显示进度
                int pro = (int) (0.1f * progress.getTotalBytes() / progress.getCurrentBytes() *100);
                if (pro > 0 ){
                    //显示进度
                }
            }
        });

    }

    /***************  小文件的上传 (Pair)*************/
    public void update(){
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "aaa.txt");
        if (!file.exists()){
            //文件不能存在，清重现选择文件
            return;
        }
        Map<String, String> fileMap = new HashMap<>();
        fileMap.put("aaa", "AAA");
        fileMap.put("bbb", "BBB");

        Pair<String, File> pair = new Pair<>("file", file);
        OkHttpProxy.upload()
                .url(TestUrls.URL_USER)
                .file(pair)
                .setParams(fileMap)
                .setWriteTimeOut(20)
                .start(new UploadListener() {
                    @Override
                    public void onSuccess(Response response) {

                    }
                    @Override
                    public void onFailure(Exception e) {

                    }
                    @Override
                    public void onUIProgress(Progress progress) {
                        int pro = (int) (0.1f * progress.getCurrentBytes()/progress.getTotalBytes() *100);
                        if (pro>0){
                            //显示进度
                        }
                    }
                });
    }

/************** 注意及时取消OKhttp 防止出现异常 ****************/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkHttpProxy.cancel(this);

    }
}
