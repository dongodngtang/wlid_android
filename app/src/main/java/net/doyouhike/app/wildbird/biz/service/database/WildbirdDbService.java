package net.doyouhike.app.wildbird.biz.service.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import net.doyouhike.app.wildbird.biz.db.bean.DbComment;
import net.doyouhike.app.wildbird.biz.db.bean.DbImage;
import net.doyouhike.app.wildbird.biz.db.bean.DbWildBird;
import net.doyouhike.app.wildbird.biz.db.dao.CommentDao;
import net.doyouhike.app.wildbird.biz.db.dao.ImageDao;
import net.doyouhike.app.wildbird.biz.db.dao.WildBirdDao;
import net.doyouhike.app.wildbird.biz.model.bean.Comment;
import net.doyouhike.app.wildbird.biz.model.bean.Image;
import net.doyouhike.app.wildbird.biz.model.bean.SpeciesInfo;
import net.doyouhike.app.wildbird.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaitu on 15-12-14.
 */
public class WildbirdDbService {

    private static WildbirdDbService instance;

    private WildbirdDbService() {

    }

    public static WildbirdDbService getInstance() {
        if (null == instance) {
            instance = new WildbirdDbService();
        }
        return instance;
    }

    /**
     * 更新野鸟特征数据
     *
     * @param info
     */
    public void updateFeature(SpeciesInfo info) {

        ContentValues values = new ContentValues();
        values.put(WildBirdDao.Properties.Shape.columnName, info.getShape());
        values.put(WildBirdDao.Properties.Color.columnName, info.getColorList());
        values.put(WildBirdDao.Properties.Locate.columnName, info.getLocateList());
        values.put(WildBirdDao.Properties.Behavior.columnName, info.getBehaviorList());
        values.put(WildBirdDao.Properties.Head.columnName, info.getHeadList());
        values.put(WildBirdDao.Properties.Neck.columnName, info.getNeckList());
        values.put(WildBirdDao.Properties.Belly.columnName, info.getBellyList());
        values.put(WildBirdDao.Properties.Waist.columnName, info.getWaistList());
        values.put(WildBirdDao.Properties.Wing.columnName, info.getWingList());
        values.put(WildBirdDao.Properties.Tail.columnName, info.getTailList());
        values.put(WildBirdDao.Properties.Leg.columnName, info.getLegList());
        values.put(WildBirdDao.Properties.Newlocate.columnName, info.getNewlocate());

        getWildbirdDao().getDatabase().update(WildBirdDao.TABLENAME, values,
                WildBirdDao.Properties.SpeciesID.columnName + "=?", new String[]{info.getSpeciesID()});

    }

    /**
     * 插入一条野鸟详情记录
     *
     * @param info
     */
    public void insertWildbirdInfo(DbWildBird info) {

        getWildbirdDao().insert(info);

        for (DbComment dbComment : info.getDbComments()) {
            getCommentDao().insert(dbComment);
        }
    }

    /**
     * 查询野鸟信息
     *
     * @param selector name
     *                 color
     *                 shape
     *                 Newlocate
     *                 head
     *                 neck
     *                 belly
     *                 wing
     *                 waist
     *                 leg
     *                 tail
     */
    public List<SpeciesInfo> search(String[] selector, int skip, int count) {
        if (null == selector) {
            selector = new String[]{"%", "%", "%", "%", "%", "%", "%", "%", "%", "%", "%"};
        }

//        String sql = "where LOCAL_NAME like ? and COLOR like ? and SHAPE like ? "
//                + "and NEWLOCATE like ? and HEAD like ? and NECK like ? and BELLY like ? and WING like ? and WAIST like ? "
//                + "and LEG like ? and TAIL like ? and IMAGE is not null order by READCOUNT,READTIME desc limit " + skip + "," + count;
//        List<DbWildBird> wildbirds = getWildbirdDao().queryRawCreate(sql, selector).list();

        List<DbWildBird> wildbirds = getWildbirdDao().queryBuilder()
                .where(
                        WildBirdDao.Properties.LocalName.like(selector[0]),
                        WildBirdDao.Properties.Color.like(selector[1]),
                        WildBirdDao.Properties.Shape.like(selector[2]),
                        WildBirdDao.Properties.Newlocate.like(selector[3]),
                        WildBirdDao.Properties.Head.like(selector[4]),
                        WildBirdDao.Properties.Neck.like(selector[5]),
                        WildBirdDao.Properties.Belly.like(selector[6]),
                        WildBirdDao.Properties.Wing.like(selector[7]),
                        WildBirdDao.Properties.Waist.like(selector[8]),
                        WildBirdDao.Properties.Leg.like(selector[9]),
                        WildBirdDao.Properties.Tail.like(selector[10]),
                        WildBirdDao.Properties.Image.isNotNull())
                .orderDesc(WildBirdDao.Properties.ReadCount, WildBirdDao.Properties.ReadTime)
                .offset(skip)
                .limit(count)
                .list();

        return toSpeciesInfos(wildbirds);
    }

    public SpeciesInfo getSpeciesInfo(String speciesID) {
        if (TextUtils.isEmpty(speciesID)) {
            return null;
        }

        DbWildBird dbWildBird = getWildbirdDao().queryBuilder().where(WildBirdDao.Properties.SpeciesID.eq(speciesID)).unique();
        if (null == dbWildBird) {
            return null;
        }
        return new SpeciesInfo(dbWildBird);
    }

    /**
     * 查询野鸟列表
     *
     * @param selector
     * @param skip
     * @param count
     * @return
     */
    public List<SpeciesInfo> autoEdit(String selector, int skip, int count) {

        List<DbWildBird> wildbirds = getWildbirdDao().queryBuilder().
                where(WildBirdDao.Properties.LocalName.like(selector)).orderDesc(WildBirdDao.Properties.ReadCount, WildBirdDao.Properties.ReadTime).
                offset(skip).limit(count).list();

        return toSpeciesInfos(wildbirds);
    }

    /**
     * 更新浏览鸟种浏览信息
     *
     * @param speciesId 鸟种id
     */
    public void updateReadInfo(String speciesId) {
        if (TextUtils.isEmpty(speciesId)) {
            return;
        }

        DbWildBird dbWildBird = getWildbirdDao().queryBuilder().where(WildBirdDao.Properties.SpeciesID.eq(speciesId)).unique();
        if (null == dbWildBird) {
            return;
        }

        long beforeCount = dbWildBird.getReadCount();

        dbWildBird.setReadCount(++beforeCount);
        dbWildBird.setReadTime(System.currentTimeMillis());

        getWildbirdDao().update(dbWildBird);

    }

    /**
     * 获取最后鸟种ID
     */
    public String getLastSpeciesId() {

        String SQL_GET_MAX_SPECIES = "SELECT *" +
                " FROM " + WildBirdDao.TABLENAME +
                " WHERE " + WildBirdDao.Properties.SpeciesID.columnName + "=(SELECT MAX(CAST(" + WildBirdDao.Properties.SpeciesID.columnName + " AS INTEGER)) FROM " + WildBirdDao.TABLENAME + ")"+
                " ORDER BY "+WildBirdDao.Properties.SpeciesID.columnName+" DESC"+
                " LIMIT 1";
        Cursor cursor = getWildbirdDao().getDatabase().rawQuery(SQL_GET_MAX_SPECIES, null);

        String speciesId = "";


        if (null != cursor) {

            if (cursor.moveToFirst()) {

                int index = cursor.getColumnIndex(WildBirdDao.Properties.SpeciesID.columnName);
                speciesId = cursor.getString(index);
            }

            cursor.close();
        }

        return speciesId;
    }


    /**
     * 写入网络野鸟图片信息
     */
    public void insertBirdImage(String speciesID, List<Image> images) {

        getImgDao().queryBuilder().
                where(ImageDao.Properties.SpeciesID.eq(speciesID)).
                buildDelete().executeDeleteWithoutDetachingEntities();

        if (null == images) {
            return;
        }

        for (Image image : images) {
            DbImage dbImg = new DbImage(image, speciesID);
            getImgDao().insert(dbImg);
        }
    }

    /**
     * 获取野鸟图片
     */
    public List<Image> getBirdImage(String speciesID) {

        List<DbImage> dbImages = getImgDao().queryBuilder().where(ImageDao.Properties.SpeciesID.eq(speciesID)).list();

        List<Image> list = new ArrayList<>();
        list.addAll(dbImages);
        return list;
    }

    /**
     * 查询评论
     *
     * @param speciesID
     * @param skip
     * @return
     */
    public List<Comment> getComment(String speciesID, int skip) {

        List<DbComment> dbComments = getCommentDao().queryBuilder().
                where(CommentDao.Properties.SpeciesID.eq(speciesID)).offset(skip).orderDesc(CommentDao.Properties.LikeNum).limit(5).list();

        return toComments(dbComments);


    }

    /**
     * 更新评论信息喜欢个数
     *
     * @param commentID
     * @param likeNum
     */
    public void updateCommentLikeNum(Long commentID, int likeNum) {

        DbComment dbComment = getCommentDao().load(commentID);
        if (null == dbComment) {
            return;
        }
        dbComment.setLikeNum(likeNum);
        getCommentDao().update(dbComment);

    }

    private List<SpeciesInfo> toSpeciesInfos(List<DbWildBird> wildbirds) {

        List<SpeciesInfo> list = new ArrayList<SpeciesInfo>();
        for (DbWildBird wildbird : wildbirds) {
            SpeciesInfo speciesInfo = new SpeciesInfo(wildbird);
            Image image = new Image();
            image.setUrl(wildbird.getImage());
            image.setAuthor(wildbird.getAuthor());
            speciesInfo.addImage(image);
            list.add(speciesInfo);
        }
        return list;
    }

    private List<Comment> toComments(List<DbComment> dbComments) {

        List<Comment> list = new ArrayList<>();
        list.addAll(dbComments);
        return list;
    }

    private WildBirdDao getWildbirdDao() {
        return DbHelper.getInstance().getWbSession().getWildBirdDao();

    }

    private CommentDao getCommentDao() {
        return DbHelper.getInstance().getWbSession().getCommentDao();

    }

    private ImageDao getImgDao() {
        return DbHelper.getInstance().getWbSession().getImageDao();

    }


}
