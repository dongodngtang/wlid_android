package net.doyouhike.app.wildbird.app;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.squareup.okhttp.OkHttpClient;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.UserInfo;

import net.doyouhike.app.wildbird.BuildConfig;
import net.doyouhike.app.wildbird.biz.db.dao.DaoMaster;
import net.doyouhike.app.wildbird.biz.db.dao.DaoSession;
import net.doyouhike.app.wildbird.biz.net.OkHttpStack;
import net.doyouhike.app.wildbird.biz.service.BackgroundService;
import net.doyouhike.app.wildbird.util.ImageLoaderUtil;
import net.doyouhike.app.wildbird.util.PhotoUtil;
import net.gotev.uploadservice.UploadService;

import java.util.ArrayList;

public class MyApplication extends Application {

    // Singleton application sInstance
    private static MyApplication sInstance;
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private FeedbackAgent agent;
    private ArrayList<Activity> activities;


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        sInstance = this;
        MultiDex.install(this);
        SDKInitializer.initialize(getApplicationContext());
        startService(new Intent(this, BackgroundService.class));

        activities=new ArrayList<>();
//        setupDatabase();
        //注册全局异常捕获
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());

        agent = new FeedbackAgent(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                agent.setUserInfo(new UserInfo());
                agent.updateUserInfo();

            }
        }).start();

        ImageLoaderUtil.getInstance().initConfig(this);
        initUploadService();
    }



    public DaoSession getDaoSession() {
        if (daoSession == null) {
            setupDatabase();
        }
        return daoSession;
    }



    private void initUploadService() {
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }


    private void setupDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        daoMaster = new DaoMaster(helper.getWritableDatabase());
        daoSession = daoMaster.newSession();
    }

    /**
     * @return the application singleton instance
     */
    public static MyApplication getInstance() {
        return sInstance;
    }


    public void addActivity(Activity activity){
        if (!activities.contains(activity)){
            activities.add(activity);
        }
    }
    public void removeActivity(Activity activity){
        if (activities.contains(activity)){
            activities.remove(activity);
        }
    }
    public void finishApplication(){

        PhotoUtil.clear();
        stopService(new Intent(this, BackgroundService.class));

        for (Activity activity:activities){
            activity.finish();
        }

        System.exit(0);
    }

}
