package net.doyouhike.app.wildbird.biz.db.dao;

import java.util.List;
import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import net.doyouhike.app.wildbird.biz.db.bean.DbRecord;

import net.doyouhike.app.wildbird.biz.db.bean.DbRecordImage;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "RECORD_IMAGE".
 */
public class RecordImageDao extends AbstractDao<DbRecordImage, Long> {

    public static final String TABLENAME = "RECORD_IMAGE";

    /**
     * Properties of entity DbRecordImage.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Record = new Property(1, Long.class, "record", false, "RECORD");
        public final static Property ImageUri = new Property(2, String.class, "imageUri", false, "IMAGE_URI");
        public final static Property ImageID = new Property(3, Integer.class, "imageID", false, "IMAGE_ID");
    }

    ;

    private DaoSession daoSession;

    private Query<DbRecordImage> record_RecordImagesQuery;

    public RecordImageDao(DaoConfig config) {
        super(config);
    }

    public RecordImageDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"RECORD_IMAGE\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT," +
                "\"RECORD\" INTEGER," + // 0: record
                "\"IMAGE_URI\" TEXT," + // 1: imageUri
                "\"IMAGE_ID\" INTEGER);"); // 2: imageID
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"RECORD_IMAGE\"";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, DbRecordImage entity) {
        stmt.clearBindings();

        Long id=entity.getId();

        if (null!=id){
            stmt.bindLong(1, id);
        }


        Long record = entity.getRecord();
        if (record != null) {
            stmt.bindLong(2, record);
        }

        String imageUri = entity.getImageUri();
        if (imageUri != null) {
            stmt.bindString(3, imageUri);
        }

        Integer imageID = entity.getImageID();
        if (imageID != null) {
            stmt.bindLong(4, imageID);
        }
    }

    @Override
    protected void attachEntity(DbRecordImage entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }


    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public DbRecordImage readEntity(Cursor cursor, int offset) {
        DbRecordImage entity = new DbRecordImage( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // record
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // imageUri
                cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // imageID
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, DbRecordImage entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setRecord(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setImageUri(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setImageID(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(DbRecordImage entity, long rowId) {
        // Unsupported or missing PK type
        return entity.getId();
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(DbRecordImage entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Internal query to resolve the "recordImages" to-many relationship of record.
     */
    public List<DbRecordImage> _queryRecord_RecordImages(long record) {
        synchronized (this) {
            if (record_RecordImagesQuery == null) {
                QueryBuilder<DbRecordImage> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Record.eq(null));
                record_RecordImagesQuery = queryBuilder.build();
            }
        }
        Query<DbRecordImage> query = record_RecordImagesQuery.forCurrentThread();
        query.setParameter(0, record);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getRecordDao().getAllColumns());
            builder.append(" FROM RECORD_IMAGE T");
            builder.append(" LEFT JOIN RECORD T0 ON T.\"RECORD\"=T0.\"RECORD\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected DbRecordImage loadCurrentDeep(Cursor cursor, boolean lock) {
        DbRecordImage entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        DbRecord recordOject = loadCurrentOther(daoSession.getRecordDao(), cursor, offset);
        entity.setRecordOject(recordOject);

        return entity;
    }

    public DbRecordImage loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[]{key.toString()};
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /**
     * Reads all available rows from the given cursor and returns a list of new ImageTO objects.
     */
    public List<DbRecordImage> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<DbRecordImage> list = new ArrayList<DbRecordImage>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }

    protected List<DbRecordImage> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }


    /**
     * A raw-style query where you can pass any WHERE clause and arguments.
     */
    public List<DbRecordImage> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }

}