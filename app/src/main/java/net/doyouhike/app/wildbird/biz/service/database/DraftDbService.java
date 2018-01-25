package net.doyouhike.app.wildbird.biz.service.database;

import net.doyouhike.app.wildbird.biz.db.bean.DbRecord;
import net.doyouhike.app.wildbird.biz.db.bean.DbRecordImage;
import net.doyouhike.app.wildbird.biz.db.dao.RecordDao;
import net.doyouhike.app.wildbird.biz.db.dao.RecordImageDao;
import net.doyouhike.app.wildbird.biz.model.bean.RecordEntity;
import net.doyouhike.app.wildbird.biz.model.bean.RecordImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaitu on 15-12-15.
 */
public class DraftDbService {

    private static DraftDbService instance;

    private DraftDbService() {

    }

    public static DraftDbService getInstance() {
        if (null == instance) {
            instance = new DraftDbService();
        }
        return instance;
    }


    public void updateRecord(RecordEntity entity) {

        DbRecord dbRecord = new DbRecord(entity);
        getRecordDao().update(dbRecord);

        delRecordImages(dbRecord.getRecord());

        //插入图片路径到草稿数据库
        for (RecordImage recordImage : dbRecord.getList()) {
            insertRecordImage(dbRecord.getRecord(), recordImage);
        }
    }

    /**
     * 插入一条草稿记录
     *
     * @param entity
     */
    public void insertRecord(RecordEntity entity) {

        DbRecord dbRecord = new DbRecord(entity);
        dbRecord.setRecord(null);
        long insertId = getRecordDao().insert(dbRecord) ;


        //插入图片路径到草稿数据库
        for (RecordImage recordImage : dbRecord.getList()) {
            insertRecordImage(insertId, recordImage);
        }

    }


    /**
     * 插入草稿图片记录
     *
     * @param record
     * @param image
     */
    private void insertRecordImage(Long record, RecordImage image) {
        DbRecordImage dbImage = new DbRecordImage(record, image);
        getRecordImageDao().insert(dbImage);
    }

    /**
     * 查询草稿图片
     *
     * @param recordId
     * @return
     */
    private List<DbRecordImage> queryRecordImage(long recordId) {
        return getRecordImageDao().queryBuilder().where(RecordImageDao.Properties.Record.eq(recordId)).list();
    }

    /**
     * 删除草稿图片
     * @param record
     */
    private void delRecordImages(long record) {

        getRecordImageDao().queryBuilder()
                .where(RecordImageDao.Properties.Record.eq(record))
                .buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public List<RecordEntity> getRecord(int skip) {

        List<DbRecord> dbRecords=getRecordDao().queryBuilder().orderDesc(RecordDao.Properties.Time).offset(skip).limit(20).list();
        return toRecordEntity(dbRecords);

    }

    /**
     * 查询草稿箱记录总数
     * @return
     */
    public int getRecordCount() {
        return Integer.valueOf(String.valueOf(getRecordDao().queryBuilder().count()));
    }

    public void updateRecordId(long record, int recordID) {

        DbRecord dbRecord=getRecordDao().load(record);
        dbRecord.setRecordID(recordID);
        getRecordDao().update(dbRecord);

    }

    /**
     * 删除草稿箱内容
     * @param record
     */
    public void deleteRecord(long record) {
        getRecordDao().deleteByKey(record);
        delRecordImages(record);
    }


    private RecordImageDao getRecordImageDao() {
        return DbHelper.getInstance().getDraftSession().getRecordImageDao();
    }

    private RecordDao getRecordDao() {
        return DbHelper.getInstance().getDraftSession().getRecordDao();
    }

    private  List<RecordEntity> toRecordEntity( List<DbRecord> dbRecords){

        List<RecordEntity> list = new ArrayList<RecordEntity>();
        for (DbRecord dbRecord:dbRecords){
            RecordEntity recordEntity=dbRecord;
            recordEntity.getList().clear();
            recordEntity.getList().addAll(dbRecord.getDbRecordImages());
            list.add(recordEntity);
        }
        return list;
    }
}
