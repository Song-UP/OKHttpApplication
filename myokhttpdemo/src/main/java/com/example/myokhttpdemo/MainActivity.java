package com.example.myokhttpdemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.URLUtil;

import com.example.myokhttpdemo.been.Person;
import com.example.myokhttpdemo.util.UrlUtil;
import com.tsy.sdk.myokhttp.MyOkHttp;
import com.tsy.sdk.myokhttp.response.DownloadResponseHandler;
import com.tsy.sdk.myokhttp.response.GsonResponseHandler;
import com.tsy.sdk.myokhttp.response.JsonResponseHandler;
import com.tsy.sdk.myokhttp.response.RawResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    MyOkHttp myOkHttp;

    Map<String, String> headerMap = new HashMap<>();


    Map<String, String> paramMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myOkHttp = MyApplication.getInstance().getMyOkHttp();

        headerMap.put("name","wusong");
        headerMap.put("age","24");

        paramMap.put("name","wusong");
        paramMap.put("age","24");

    }

//    总结:
//    1.返回转化
    //    返回String RawResponseHandler()
    //    返回json   new JsonResponseHandler()
    //    返回Gson   GsonResponseHandler<Person>()
//    2.添加 单个参数，多个参数Map
//        headers(new Map())
//        addHeader("aaa", "AAA")
//
//        params(new Map())
//        addParam("aaa", "AAA")
//    3.设置标识符，用于取消,同时注意及时取消，避免 内存泄漏
//        tag(this)  myOkHttp.cancel(this);
//    4.文件上传 upDate
//        addFile(String key, File file)    上传文件
//        addFile(String key, String fileName, byte[] fileContent)   //上传字节
//        files(Map<String, File> files)  //多文件参数
//    5.文件下载
//    filePath(Environment.getExternalStorageDirectory() + "photo.jpg")  后面两个等于这两个
//           .fileDir("fileDir")
//          .fileName("fileName")



    /************* Post ************************/
    /**
     * post  json返回
     */
    public void doPost(){

        //返回类型是 json
        myOkHttp.post()
                .url(UrlUtil.urlPath)
                .addHeader("aaa", "AAA")  //添加单个头部
//                .headers(headerMap) //添加多个头部
                .params(paramMap)
                .tag(this)
                .enqueue(new JsonResponseHandler() {
                    //onFailure 是必须重写的，其他的可选
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(int statusCode, JSONArray response) {
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        super.onSuccess(statusCode, response);
                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {
                        super.onProgress(currentBytes, totalBytes);
                    }
                });

        //返回类型是String
        myOkHttp.post()
                .url(UrlUtil.urlPath)
                .params(paramMap)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, String response) {

                    }

                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /**
     * post 提交json
     */
    public void postJson() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", "A");
        jsonObject.put("b", "B");
        myOkHttp.post()
                .url(UrlUtil.urlPath)
                .addHeader("Content-type", "application/json")
                .jsonParams(jsonObject.toString())  //注意：jsonParams 和 params不能同时使用，如果同时使用，则以params优先
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }


    /******************** Get ************************/
    /**
     * Get请求和Post 请求使用基本一致，不同的是 没有jsonParam
     */
    public void doGet(){
        myOkHttp.get()
                .url(UrlUtil.urlPath)
                .params(paramMap)
                .headers(headerMap)
                .addHeader("head","Head")
                .tag(this)  //用于取消的标识符
                .enqueue(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });
    }

    /************ put 请求  (使用基本和Get 和 Post 一致，但是不能添加 param) ***************/
    public void doPut(){
        myOkHttp.put()
                .url(UrlUtil.urlPath)
                .addHeader("aaa", "AAA")
                .tag(this)
                .equals(new JsonResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }
                });

    }

    /*************** Patch  同put****************************/
    public void doPatch(){
        myOkHttp.patch()
                .url(UrlUtil.urlPath)
                .addHeader("aaa", "AAA")
                .tag(this)
                .equals(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                    @Override
                    public void onSuccess(int statusCode, String response) {
                    }
                });
    }
    /*********** Delete 请求 同Patch put *****************/
    /*********** upLoad() ************/
    public void doUpload(){
        myOkHttp.upload()
                .url(UrlUtil.urlPath)
                .addParam("aaa", "AAA")
                .addFile("fileName", new File(getFilesDir() + "/photo.jpg"))
        //        addFile(String key, File file)    上传文件
        //        addFile(String key, String fileName, byte[] fileContent)   //上传字节
        //        files(Map<String, File> files)  //多文件提交
                .tag(this)
                .enqueue(new GsonResponseHandler<Person>() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Person response) {

                    }
                });
    }

    /*********** Download ****************/
    public void doDownLoad(){
        myOkHttp.download()
                .url(UrlUtil.urlPath)
                .addHeader("aaa","AAAA")
                .filePath(Environment.getExternalStorageDirectory() + "photo.jpg")
//                .fileDir("fileDir")
//                .fileName("fileName")
//                .setCompleteBytes(1000*1024L)
                .tag(this)
                .enqueue(new DownloadResponseHandler() {
                    @Override
                    public void onFinish(File downloadFile) {

                    }

                    @Override
                    public void onProgress(long currentBytes, long totalBytes) {

                    }

                    @Override
                    public void onFailure(String error_msg) {

                    }
                });
    }

    /*****************  Cookie  ****************/
    /**
     * php代码：
     * if(empty($_COOKIE['mycookie'])) {
     *      setcookie('mycookie','value', time()+20);
     *      die("no cookie 'mycookie', set 'mycookie' => 'value'");
     * }
     * die("has cookie 'mycookie' => " . $_COOKIE['mycookie']);
     *
     */
    private void Cookie(){
        String url = "http://192.168.2.135/myokhttp/cookie.php";
        myOkHttp.post()
                .url(url)
                .tag(this)
                .enqueue(new RawResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, String error_msg) {
                    }
                    @Override
                    public void onSuccess(int statusCode, String response) {
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myOkHttp.cancel(this);
    }
}
