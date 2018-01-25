package net.doyouhike.app.wildbird.biz.service.database;

import android.database.sqlite.SQLiteDatabase;

import net.doyouhike.app.wildbird.app.MyApplication;
import net.doyouhike.app.wildbird.biz.db.dao.DaoMaster;
import net.doyouhike.app.wildbird.biz.db.dao.DaoSession;
import net.doyouhike.app.wildbird.biz.db.dao.WbDaoMaster;
import net.doyouhike.app.wildbird.biz.db.dao.WbDaoSession;
import net.doyouhike.app.wildbird.biz.model.Content;

/**
 * Created by zaitu on 15-12-14.
 */
public class DbHelper {

    private static DbHelper instance;
    private WbDaoSession wbDaoSession;
    private DaoSession draftDaoSession;


    private DbHelper() {
    }

    public synchronized static DbHelper getInstance() {
        if (null == instance) {
            initInstance();
        }
        return instance;
    }

    private synchronized static void initInstance() {
        if (null == instance) {
            instance = new DbHelper();
        }
    }

    public WbDaoSession getWbSession() {
        initWbSession();
        return wbDaoSession;

    }
    public DaoSession getDraftSession() {
        initDraftSession();
        return draftDaoSession;

    }

    private void initWbSession() {

        if (null == wbDaoSession) {
            WbDaoMaster.DevOpenHelper helper = new  WbDaoMaster.DevOpenHelper(MyApplication.getInstance().getApplicationContext(), Content.DB_NAME_WILDBIRD, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            WbDaoMaster daoMaster = new WbDaoMaster(db);
            wbDaoSession = daoMaster.newSession();
        }
    }

    private void initDraftSession(){
        if (null==draftDaoSession){
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(MyApplication.getInstance().getApplicationContext(), Content.DB_NAME_DRAFT, null);
            SQLiteDatabase db = helper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(db);
            draftDaoSession = daoMaster.newSession();
        }
    }

}
