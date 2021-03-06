package net.doyouhike.app.wildbird.biz.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import net.doyouhike.app.wildbird.biz.db.bean.DbRecord;
import net.doyouhike.app.wildbird.biz.db.bean.DbRecordImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig recordDaoConfig;
    private final DaoConfig recordImageDaoConfig;

    private final RecordDao recordDao;
    private final RecordImageDao recordImageDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        recordDaoConfig = daoConfigMap.get(RecordDao.class).clone();
        recordDaoConfig.initIdentityScope(type);

        recordImageDaoConfig = daoConfigMap.get(RecordImageDao.class).clone();
        recordImageDaoConfig.initIdentityScope(type);


        recordDao = new RecordDao(recordDaoConfig, this);
        recordImageDao = new RecordImageDao(recordImageDaoConfig, this);

        registerDao(DbRecord.class, recordDao);
        registerDao(DbRecordImage.class, recordImageDao);
    }
    
    public void clear() {
        recordDaoConfig.getIdentityScope().clear();
        recordImageDaoConfig.getIdentityScope().clear();
    }


    public RecordDao getRecordDao() {
        return recordDao;
    }

    public RecordImageDao getRecordImageDao() {
        return recordImageDao;
    }
}
