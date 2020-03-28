package cn.cbsd.dogtag;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.Utils;
import com.squareup.leakcanary.LeakCanary;

import cn.cbsd.dogtag.greendao.DaoMaster;
import cn.cbsd.dogtag.greendao.DaoSession;
import cn.cbsd.dogtag.greendao.MyOpenHelper;

public class AppInit extends Application {


    private DaoMaster.OpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    protected static AppInit instance;

    public static AppInit getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {

        super.onCreate();

        instance = this;

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//
//        LeakCanary.install(this);

        Utils.init(getContext());

        setDatabase();

    }

    private void setDatabase() {

        mHelper = new MyOpenHelper(AppInit.getInstance(), "dogtag-db", null);//建库
        mDaoMaster = new DaoMaster(mHelper.getWritableDatabase());
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }


}
