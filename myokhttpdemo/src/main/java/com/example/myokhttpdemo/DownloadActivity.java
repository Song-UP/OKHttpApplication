package com.example.myokhttpdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tsy.sdk.myokhttp.download_mgr.AbstractDownloadMgr;
import com.tsy.sdk.myokhttp.download_mgr.DownloadStatus;
import com.tsy.sdk.myokhttp.download_mgr.DownloadTask;
import com.tsy.sdk.myokhttp.download_mgr.DownloadTaskListener;

import java.io.File;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener{

    DownloadMgr downloadMgr;
    DownloadTask downloadTask;
    DownloadTaskListener downloadTaskListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        if (downloadTask == null){   //还没有开启任何任务，需要开启一个新任务
            downloadMgr = MyApplication.getInstance().getDownloadMgr();
            AbstractDownloadMgr.Task task = new AbstractDownloadMgr.Task();
            task.setTaskId(downloadMgr.genTaskId());   //标识符，用于取消和开始任务
            task.setUrl("www.douloadApk.com");
            task.setFilePath("保存文件的路径.jpg");
            //        task.setCompleteBytes(0L); //如果新加任务的可以不设置 默认是0L（当恢复任务的时候需要设置已经完成的bytes 从本地读取）
            task.setDefaultStatus(DownloadMgr.DEFAULT_TASK_STATUS_START); //默认添加进对了自动开始
            downloadTask = downloadMgr.addTask(task);
        }
        else {
            switch (downloadTask.getStatus()){
                case DownloadStatus.STATUS_DOWNLOADING:  //此时是下载中，可以暂停下载
                    downloadMgr.pauseTask(downloadTask.getTaskId());  //暂停指定的任务

//                    downloadMgr.pauseAllTask();  //暂停所有任务
                    break;
                 case DownloadStatus.STATUS_PAUSE:
                     downloadMgr.startTask(downloadTask.getTaskId());
                     break;
                 default:
                     break;
            }

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        downloadTaskListener = new DownloadTaskListener() {
            @Override
            public void onStart(String taskId, long completeBytes, long totalBytes) {
                downloadTask = downloadMgr.getDownloadTask(taskId);
            }
            @Override
            public void onProgress(String taskId, long currentBytes, long totalBytes) {
                downloadTask = downloadMgr.getDownloadTask(taskId);
                //建议不要每次都刷新，使用postDelay，防止刷新过快
                int progress = (int) ((1f* currentBytes/totalBytes)*100);
            }
            @Override
            public void onPause(String taskId, long currentBytes, long totalBytes) {
                downloadTask = downloadMgr.getDownloadTask(taskId);
            }
            @Override
            public void onFinish(String taskId, File file) {
                downloadTask = downloadMgr.getDownloadTask(taskId);
                //下载完成
            }
            @Override
            public void onFailure(String taskId, String error_msg) {
                downloadTask =downloadMgr.getDownloadTask(taskId);
                //下载失败
            }
        };

        downloadMgr.addListener(downloadTaskListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //不显示时移除监听 放置内存泄露
        downloadMgr.removeListener(downloadTaskListener);
        downloadTaskListener = null;
    }
}
